package Download;

import Exception.HTTPException;
import Exception.MissException;
import Utils.Logs;
import Utils.Options;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

public class GenbankOrganisms extends IDownloader {

    /**
     * Key to get main data
     */
    private static final String s_MAIN_DATA = "ngout";
    /**
     * Key to get data
     */
    private static final String s_DATA = "data";
    /**
     * Key to get total of organism
     */
    private static final String s_TOTAL_COUNT = "totalCount";
    /**
     * Key to get content
     */
    private static final String s_CONTENT = "content";
    /**
     * request
     */
    private static final String s_REQUEST = "[display( id,organism,kingdom,group,subgroup,replicons,release_date,modify_date,_version_),hist(kingdom,group,subgroup)].from(GenomeAssemblies).usingschema(/schema/GenomeAssemblies).sort(lineage,asc)";
    /**
     * Queue of retrieved organisms
     */
    private final LinkedList<RawOrganism> m_dataQueue;
    /**
     * Queue of failed chunk's indexes
     */
    private final ArrayList<Integer> m_failedChunks;
    /**
     * Currently downloaded
     */
    private int m_downloaded;
    /**
     * Total on database
     */
    private int m_totalCount;
    /**
     * Total on the queue
     */
    private int m_enqueued;
    /**
     * Number of failed organism
     */
    private int m_failedOrganism;

    /**
     * Class constructor
     */
    private GenbankOrganisms() {
        m_downloaded = 0;
        m_totalCount = -1;
        m_enqueued = 0;
        m_failedOrganism = 0;
        m_dataQueue = new LinkedList<>();
        m_failedChunks = new ArrayList<>();
    }

    /**
     * Get singleton (only one thread for the moment)
     *
     * @return GenbankOrganism database instance
     */
    public static GenbankOrganisms getInstance() {
        return GenbankOrganismsHolder.s_INSTANCE;
    }

    /**
     * Get the url to download the next chunk of database
     *
     * @param _index the index to begin
     * @return A JAVA URL instance for the next chunk of data
     * @throws UnsupportedEncodingException If UTF-8 isn't supported by executing system
     * @throws MalformedURLException        If the URL is malformed
     */
    private URL getURL(int _index) throws UnsupportedEncodingException, MalformedURLException {
        String urlStr;
        try {
            urlStr = String.format("%s?q=%s&start=%d&limit=%d", Options.getOrganismBaseUrl(), URLEncoder.encode(s_REQUEST, StandardCharsets.UTF_8.name()), _index, Options.getDownloadStep());
        } catch (UnsupportedEncodingException e) {
            Logs.exception(e);
            throw e;
        }

        URL res;
        try {
            res = new URL(urlStr);
        } catch (MalformedURLException e) {
            Logs.exception(e);
            throw e;
        }

        return res;
    }

    /**
     * Get the status of the download
     *
     * @return True if database has been completely m_downloaded (else false)
     */
    private boolean downloadCompleted() {
        return m_totalCount != -1 && m_downloaded >= m_totalCount;
    }

    /**
     * Download and process next data chunk (page of step organisms)
     *
     * @throws MissException if the download can't be done
     */
    private void downloadNextChunk() throws MissException {
        if (downloadCompleted()) {
            return;
        }

        int chunkLength = 0;
        try {
            chunkLength = downloadChunk(m_downloaded);
        } catch (Exception e) {
            Logs.exception(e);
            if (m_totalCount == -1) {
                throw new MissException("can't get the total numbers of organism to download");
            } else {
                m_failedChunks.add(m_downloaded);
                m_downloaded += Options.getDownloadStep();
                m_failedOrganism += Options.getDownloadStep();
            }
            return;
        }

        m_downloaded += chunkLength;

        if (m_enqueued == m_totalCount) {
            Logs.info("GenbankOrganisms: Organisms download complete");
        }
    }

    /**
     * Download a chunk
     *
     * @param _index the index to begin
     * @return thew number of organism downloaded
     * @throws IOException   if an IOException is throw
     * @throws JSONException if an JSONException is throw
     * @throws HTTPException if an HTTPException is throw
     */
    private int downloadChunk(int _index) throws IOException, JSONException, HTTPException {
        Logs.info(String.format("Requesting organisms [%d;%d]", _index, _index + Options.getDownloadStep()));

        JSONObject json;
        try {
            json = getJSON(getURL(_index)).getJSONObject(s_MAIN_DATA).getJSONObject(s_DATA);
        } catch (IOException | JSONException | HTTPException e) {
            Logs.exception(e);
            throw e;
        }

        try {
            m_totalCount = json.getInt(s_TOTAL_COUNT);
        } catch (JSONException e) {
            Logs.exception(e);
            throw e;
        }

        JSONArray dataChunk;
        try {
            dataChunk = json.getJSONArray(s_CONTENT);
        } catch (JSONException e) {
            Logs.exception(e);
            throw e;
        }

        long currentEnqueue = 0;
        for (Object org : dataChunk) {
            try {
                enqueueOrganism(new RawOrganism((JSONObject) org));
                ++currentEnqueue;
            } catch (JSONException e) {
                Logs.exception(e);
                ++m_failedOrganism;
            }
        }

        int chunkLength = dataChunk.length();
        Logs.info(String.format("%d/%d organisms enqueued of %d requested", currentEnqueue, chunkLength, Options.getDownloadStep()));
        return chunkLength;
    }

    /**
     * Download all organism
     *
     * @throws MissException if the total's number of organism can't be downloaded
     */
    public void downloadOrganisms() throws MissException {
        while (!downloadCompleted()) {
            try {
                downloadNextChunk();
            } catch (MissException e) {
                Logs.exception(e);
                throw e;
            }
        }
        disconnect();
    }

    /**
     * Returns true if there is failed chunk
     *
     * @return if there is failed chunk
     */
    public boolean hasFailedChunk() {
        return m_failedChunks.size() > 0;
    }

    /**
     * Returns the number of failed organism
     *
     * @return The number of failed chunk
     */
    public int getFailedOrganism() {
        return m_failedOrganism;
    }

    /**
     * Retrieve the total number of organisms
     *
     * @return Total count
     */
    public int getTotalCount() {
        return m_totalCount;
    }

    /**
     * Add an organism to the queue
     *
     * @param _organism Organism data to enqueue
     */
    private void enqueueOrganism(RawOrganism _organism) {
        m_dataQueue.add(_organism);
        ++m_enqueued;
    }

    /**
     * Returns true if there is more data
     *
     * @return True if their is more data to be processed
     */
    public boolean hasNext() {
        return m_dataQueue.size() > 0;
    }

    /**
     * Get next m_downloaded organism
     *
     * @return Data retrieved from Genbank
     */
    public RawOrganism getNext() {
        return m_dataQueue.removeFirst();
    }

    /**
     * Here SingletonHolder is static so it gets loaded in memory only
     * on the first access, this mechanism ensure that the singleton is
     * thread safe.
     * Locks lacks in performance causing poor access to the instance.
     */
    private static class GenbankOrganismsHolder {
        private final static GenbankOrganisms s_INSTANCE = new GenbankOrganisms();
    }

}
