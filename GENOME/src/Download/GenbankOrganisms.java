package Download;

import Exception.HTTPException;
import Exception.MissException;
import Exception.OutOfMemoryException;
import Json.JSONArray;
import Json.JSONException;
import Json.JSONObject;
import Utils.Logs;
import Utils.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public final class GenbankOrganisms extends IDownloader {

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
    private static final String s_REQUEST = "[display( id,organism,kingdom,group,subgroup,replicons,release_date,modify_date,_version_)].from(GenomeAssemblies).sort(lineage,asc)";
    /**
     * Queue of retrieved organisms
     */
    private final LinkedList<OrganismParser> m_dataQueue;
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
    public GenbankOrganisms() {
        m_downloaded = 0;
        m_totalCount = -1;
        m_enqueued = 0;
        m_failedOrganism = 0;
        m_dataQueue = new LinkedList<>();
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
        return m_enqueued;
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
    public OrganismParser getNext() {
        return m_dataQueue.removeFirst();
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
        String urlStr = "";
        try {
            urlStr = String.format("%s?q=%s&start=%d&limit=%d", Options.getOrganismBaseUrl(), URLEncoder.encode(s_REQUEST, StandardCharsets.UTF_8.name()), _index, Options.getDownloadStep());
        } catch (UnsupportedEncodingException e) {
            Logs.warning("Unable to encode url : " + urlStr);
            Logs.exception(e);
            throw e;
        }

        final URL res;
        try {
            res = new URL(urlStr);
        } catch (MalformedURLException e) {
            Logs.warning("Unable to create a malformed url : " + urlStr);
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

        int chunkLength;
        try {
            chunkLength = downloadChunk(m_downloaded);
        } catch (Throwable e) {
            if (m_totalCount == -1) {
                Logs.warning("Unable to find the total number of organism");
                throw new MissException("Unable to find the total number of organism");
            } else {
                m_downloaded += Options.getDownloadStep();
                m_failedOrganism += Options.getDownloadStep();
            }
            Logs.exception(e);
            return;
        }

        m_downloaded += chunkLength;

        if (m_enqueued == m_totalCount) {
            Logs.info("GenbankOrganisms: Organisms download complete", true);
        }
        m_enqueued = m_dataQueue.size();
    }

    /**
     * Download a chunk
     *
     * @param _index the index to begin
     * @return thew number of organism downloaded
     * @throws IOException          if an IOException is throw
     * @throws JSONException        if an JSONException is throw
     * @throws HTTPException        if an HTTPException is throw
     * @throws OutOfMemoryException A savage out of memory appear
     */
    private int downloadChunk(int _index) throws IOException, JSONException, HTTPException, OutOfMemoryException {
        Logs.info(String.format("Requesting organisms [%d;%d]", _index, _index + Options.getDownloadStep()), true);

        final JSONObject json;
        try {
            json = getJSON(getURL(_index)).getJSONObject(s_MAIN_DATA).getJSONObject(s_DATA);
        } catch (IOException | JSONException | HTTPException e) {
            Logs.warning("Unable to get data");
            Logs.exception(e);
            throw e;
        } catch (OutOfMemoryError e) {
            Logs.warning("Out of memory");
            Logs.exception(new Exception(e));
            throw new OutOfMemoryException(e.getMessage());
        }

        try {
            m_totalCount = json.getInt(s_TOTAL_COUNT);
        } catch (JSONException e) {
            final String message = "Unable to find the total number of organism : " + json.toString();
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        final JSONArray dataChunk;
        try {
            dataChunk = json.getJSONArray(s_CONTENT);
        } catch (JSONException e) {
            final String message = "Unable to find the content : " + json.toString();
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }

        long currentEnqueue = 0;
        for (Object org : dataChunk) {
            try {
                OrganismParser currentOrg = new OrganismParser((JSONObject) org);
                OrganismParser last = null;
                if (m_dataQueue.size() > 0) {
                    last = m_dataQueue.getLast();
                }
                if (last != null && currentOrg.getName().compareTo(last.getName()) == 0) {
                    if (currentOrg.getReplicons().size() > last.getReplicons().size()) {
                        m_dataQueue.removeLast();
                        enqueueOrganism(currentOrg);
                        ++currentEnqueue;
                    }
                } else {
                    enqueueOrganism(currentOrg);
                    ++currentEnqueue;
                }
                ++m_enqueued;

            } catch (JSONException e) {
                final String message = "Unable create OrganismParser";
                Logs.warning(message);
                Logs.exception(new JSONException(message, e));
                ++m_failedOrganism;
            }
        }

        int chunkLength = dataChunk.length();
        Logs.info(String.format("%d/%d organisms enqueued of %d requested", currentEnqueue, chunkLength, Options.getDownloadStep()), true);
        return chunkLength;
    }

    /**
     * Make an HTTP GET request and parse result as json
     *
     * @param _url The request URL to retrieve data from
     * @return The JSONObject parsed from server response
     * @throws IOException          An error occurred while connecting to the server
     * @throws JSONException        Invalid JSON
     * @throws HTTPException        An error occurred while downloading data
     * @throws OutOfMemoryException A savage out of memory appear
     */
    private JSONObject getJSON(URL _url) throws IOException, JSONException, HTTPException, OutOfMemoryException {
        final StringBuilder responseText = new StringBuilder();
        String line;

        try (BufferedReader in = get(_url)) {
            while ((line = in.readLine()) != null)
                responseText.append(line);
        } catch (IOException | HTTPException e) {
            Logs.warning("Unable create data");
            Logs.exception(e);
            throw e;
        } catch (OutOfMemoryError e) {
            Logs.warning("Out of memory");
            Logs.exception(new Exception(e));
            throw new OutOfMemoryException(e.getMessage());
        }

        final JSONObject obj;
        try {
            obj = new JSONObject(responseText.toString());
        } catch (JSONException e) {
            final String message = "Unable create json";
            Logs.warning(message);
            Logs.exception(new JSONException(message, e));
            throw e;
        }
        return obj;
    }

    /**
     * Add an organism to the queue
     *
     * @param _organism Organism data to enqueue
     */
    private void enqueueOrganism(OrganismParser _organism) {
        m_dataQueue.add(_organism);
    }

}
