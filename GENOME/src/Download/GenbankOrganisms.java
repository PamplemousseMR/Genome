package Download;

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
import java.util.LinkedList;

public class GenbankOrganisms extends Downloader {

    private static final String s_MAIN_DATA = "ngout";
    private static final String s_DATA = "data";
    private static final String s_TOTAL_COUNT = "totalCount";
    private static final String s_CONTENT = "content";

    /**
     * Currently downloaded
     */
    private int m_downloaded;
    /**
     *Total on database
     */
    private int m_totalCount;
    /**
     * Total on the queue
     */
    private int m_enqueued;
    /**
     * Queue of retrieved organisms
     */
    private LinkedList<RawOrganism> m_dataQueue;
    /**
     * Queue of failed chunk's indexes
     */
    private LinkedList<Integer> m_failedChunks;

    private static final String s_REQUEST =
            "[display( id,organism,kingdom,group,subgroup,replicons,release_date,modify_date,_version_)," +
            "hist(kingdom,group,subgroup)].from(GenomeAssemblies)" +
            ".usingschema(/schema/GenomeAssemblies).sort(lineage,asc)";

    /**
     * Class constructor
     */
    private GenbankOrganisms() {
        // Counters
        m_downloaded = 0;
        m_totalCount = -1;
        m_enqueued = 0;

        // Initialize data
        m_dataQueue = new LinkedList<>();
        m_failedChunks = new LinkedList<>();
    }

    /**
     * Get the url to download the next chunk of database
     * @param _index, the index to begin
     * @return A JAVA URL instance for the next chunk of data
     * @throws Exception If UTF-8 isn't supported by executing system
     */
    private URL getURL(int _index) throws UnsupportedEncodingException, MalformedURLException {

        String urlStr;
        try {
            // Forge request
            urlStr = String.format("%s?q=%s&start=%d&limit=%d",
                        Options.getBaseUrl(),
                        URLEncoder.encode(s_REQUEST, StandardCharsets.UTF_8.name()),
                        _index,
                        Options.getDownloadStep());
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
     * @return True if database has been completely m_downloaded (else false)
     */
    private boolean downloadCompleted() {
        return m_totalCount != -1 && m_downloaded >= m_totalCount;
    }

    /**
     * Download and process next data chunk (page of step organisms)
     * @throws Exception if the download can't be done
     */
    private void downloadNextChunk() throws Exception {
        if (downloadCompleted()) {
            return;
        }

        int chunkLength = 0;
        try{
            chunkLength = downloadChunk(m_downloaded);
        } catch (Exception e){
            Logs.exception(e);
            if(m_totalCount == -1){
                throw new Exception("can't get the total numbers of organism to download");
            } else{
                m_failedChunks.add(m_downloaded);
                m_downloaded += Options.getDownloadStep();
            }
            return;
        }

        // Increment number of retrieved objects
        m_downloaded += chunkLength;

        if (m_enqueued == m_totalCount) {
            Logs.info("GenbankOrganisms: Organisms download complete");
        }
    }

    /**
     * Download a chunk
     * @param _index, the index to begin
     * @return thew number of organism downloaded
     * @throws Exception if an error occurred
     */
    public int downloadChunk(int _index) throws Exception {
        Logs.info(String.format("Requesting organisms [%d;%d]", _index, _index + Options.getDownloadStep()));

        // Request json
        JSONObject json;
        try {
            json = getJSON(getURL(_index)).getJSONObject(s_MAIN_DATA).getJSONObject(s_DATA);
        } catch (Download.HTTPException|IOException|JSONException e) {
            Logs.exception(e);
            throw e;
        }

        // Get total number of organisms
        try {
            m_totalCount = json.getInt(s_TOTAL_COUNT);
        }catch(JSONException e) {
            Logs.exception(e);
            throw e;
        }

        // Get organism
        JSONArray dataChunk;
        try {
            dataChunk = json.getJSONArray(s_CONTENT);
        }catch(JSONException e) {
            Logs.exception(e);
            throw e;
        }

        // Enqueue organisms
        long currentEnqueue = 0;
        for (Object org: dataChunk) {
            try {
                enqueueOrganism(new RawOrganism((JSONObject) org));
                ++currentEnqueue;
            }catch (JSONException e){
                Logs.exception(e);
            }
        }

        int chunkLength = dataChunk.length();
        Logs.info(String.format("%d/%d organisms enqueued of %d requested", currentEnqueue, chunkLength, Options.getDownloadStep()));
        return chunkLength;
    }

    /**
     * Download all organism
     * @throws Exception if the total's number of organism can't be downloaded
     */
    public void downloadOrganisms() throws Exception {
        while (!downloadCompleted()) {
            try {
                downloadNextChunk();
            } catch (Exception e) {
                Logs.exception(e);
                throw e;
            }
        }

        // Disconnect from server
        disconnect();
    }

    /**
     * Retrieve the total number of organisms
     * @return Total count
     */
    public int getTotalCount() {
        return m_totalCount;
    }

    /**
     * Add an organism to the queue
     * @param organism Organism data to enqueue
     */
    private void enqueueOrganism(RawOrganism organism) {
        m_dataQueue.add(organism);
        m_enqueued++;
    }

    /**
     * Returns true if there is more data
     * @return True if their is more data to be processed
     */
    public boolean hasNext() {
        return m_dataQueue.size() > 0;
    }

    /**
     * Get next m_downloaded organism
     * @return Data retrieved from Genbank
     */
    public RawOrganism getNext() {
        return m_dataQueue.removeFirst();
    }

    /**
     * Returns true if there is more failed chunk
     * @return True if their is more failed chunk to be processed
     */
    public boolean hasFailedChunk(){
        return m_failedChunks.size() > 0;
    }

    /**
     * Get the next failed chunk
     * @return the next failed chunk
     */
    public int getNextFailedChunk(){
        return m_failedChunks.removeFirst();
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

    /**
     * Get singleton (only one thread for the moment)
     * @return GenbankOrganism database instance
     */
    public static GenbankOrganisms getInstance() {
        return GenbankOrganismsHolder.s_INSTANCE;
    }

}
