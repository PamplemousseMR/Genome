package Download;

import Utils.Logs;
import Utils.Options;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class GenbankOrganisms extends Downloader
{

    private int m_downloaded;
    private int m_totalCount;
    private int m_enqueued;
    private boolean m_endOfStream;

    // Queue of retrieved organisms
    private LinkedList<RawOrganism> m_dataQueue;

    private GenbankOrganisms() {
        // Counters
        m_downloaded = 0;
        m_totalCount = -1;
        m_enqueued = 0;

        m_endOfStream = false;

        // Initialize data
        m_dataQueue = new LinkedList<>();
    }

    /**
     * Get the url to download the next chunk of database
     * @return A JAVA URL instance for the next chunk of data
     * @throws UnsupportedEncodingException If UTF-8 isn't supported by executing system
     */
    private URL getURL() throws UnsupportedEncodingException, MalformedURLException {

        String request =
                        "[display(" +
                        "id,organism,kingdom,group,subgroup,replicons,release_date," +
                        "modify_date,_version_)," +
                        "hist(kingdom,group,subgroup)]" +
                        ".from(GenomeAssemblies)" +
                        ".usingschema(/schema/GenomeAssemblies)" +
                        ".sort(lineage,asc)";

        // Add query
        String urlStr;
        try {
            urlStr = String.format(
                        "%s?q=%s&start=%d&limit=%d",
                        Options.getBaseUrl(),
                        URLEncoder.encode(request, StandardCharsets.UTF_8.name()),
                        m_downloaded,
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
    private boolean downloadCompleted()
    {
        return m_totalCount != -1 && m_downloaded >= m_totalCount;
    }

    /**
     * Download and process next data chunk (page of step organisms)
     */
    private void downloadNextChunk()
    {
        if (downloadCompleted()) {
            return;
        }

        try {
            JSONObject json = toJSON(get(getURL())).getJSONObject("ngout").getJSONObject("data");

            // Get total number of organisms
            m_totalCount = json.getInt("totalCount");

            // Get response content
            JSONArray dataChunk = json.getJSONArray("content");

            for (Object org: dataChunk) {
                enqueueOrganism(new RawOrganism((JSONObject)org));
            }

            if (m_enqueued == m_totalCount) {
                m_endOfStream = true;
            }

            // Set chunk size
            int chunkLength = dataChunk.length();

            // Increment number of retrieved objects
            m_downloaded += chunkLength;

        } catch (Download.HTTPException|IOException e) {
            Logs.exception(e);
            e.printStackTrace();
        }
    }

    public void downloadOrganisms()
    {
        while (!downloadCompleted()) {
            downloadNextChunk();
        }
    }

    /**
     * Retrieve the total number of organisms
     * @return Total count
     */
    public int getTotalCount()
    {
        return m_totalCount;
    }

    /**
     * Get next m_downloaded organism
     *
     * @return Data retrieved from Genbank
     */
    public RawOrganism getNext()
    {
        // Dequeue
        return m_dataQueue.removeFirst();
    }

    /**
     * Add an organism to the queue
     * @param organism Organism data to enqueue
     */
    private void enqueueOrganism(RawOrganism organism)
    {
        m_dataQueue.add(organism);
        m_enqueued++;
    }

    /**
     * Returns if there is more data to be
     * @return True if their is more data to be processed
     */
    public boolean hasNext()
    {
        return !m_endOfStream || m_dataQueue.size() > 0;
    }

    // Here SingletonHolder is static so it gets loaded in memory only
    // on the first access, this mechanism ensure that the singleton is
    // thread safe.
    // Locks lacks in performance causing poor access to the instance.
    private static class GenbankOrganismsHolder
    {
        private final static GenbankOrganisms s_INSTANCE = new GenbankOrganisms();
    }

    /**
     * Get singleton (only one thread for the moment)
     * @return GenbankOrganism database instance
     */
    public static GenbankOrganisms getInstance()
    {
        return GenbankOrganismsHolder.s_INSTANCE;
    }

}
