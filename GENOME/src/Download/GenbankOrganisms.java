package Download;

import Utils.Logs;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class GenbankOrganisms extends Downloader implements Runnable
{

    private int downloaded;
    private int totalCount;
    private int enqueued;
    private boolean endOfStream;
    private int step;

    private JSONArray data;

    // Queue of retrieved organisms
    private LinkedList<RawOrganism> dataQueue;

    private GenbankOrganisms()
    {
        // Counters
        this.downloaded = 0;
        this.totalCount = -1;
        this.enqueued = 0;

        this.endOfStream = false;

        // TODO: Put this in option
        // Number of organisms to get on each request (max: 100000)
        this.step = 100000;

        // Initialize data
        this.data = null;
        this.dataQueue = new LinkedList<>();
    }

    /**
     * Get the url to download the next chunk of database
     * @return A JAVA URL instance for the next chunk of data
     * @throws UnsupportedEncodingException If UTF-8 isn't supported by executing system
     */
    private URL getURL() throws UnsupportedEncodingException {

        String request =
                "[display(" +
                        "id,organism,kingdom,group,subgroup,replicons,release_date," +
                        "modify_date,_version_)," +
                "hist(kingdom,group,subgroup)]" +
                ".from(GenomeAssemblies)" +
                ".usingschema(/schema/GenomeAssemblies)" +
                ".sort(lineage,asc).sort(organism,asc)";

        // Add query
        String urlStr = String.format(
                "https://www.ncbi.nlm.nih.gov/Structure/ngram?q=%s&start=%d&limit=%d",
                URLEncoder.encode(request, StandardCharsets.UTF_8.name()),
                downloaded,
                step);

        System.out.println(urlStr);

        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            Logs.exception(e);
        }

        return null;
    }

    /**
     * Get the status of the download
     * @return True if database has been completely downloaded (else false)
     */
    private boolean downloadCompleted()
    {
        return this.totalCount != -1 && this.downloaded >= this.totalCount;
    }

    private void downloadNextChunk()
    {
        if (downloadCompleted()) {
            return;
        }

        try {

            JSONObject json = toJSON(get(getURL())).getJSONObject("ngout")
                                                   .getJSONObject("data");

            // Get total number of organisms
            this.totalCount = json.getInt("totalCount");

            // Get response content
            JSONArray dataChunk = json.getJSONArray("content");

            for (Object org: dataChunk) {
                this.enqueueOrganism(new RawOrganism((JSONObject)org));
                this.enqueued++;

                if (enqueued == totalCount) {
                    endOfStream = true;
                }
            }

            // Append data to data object
            if (this.data == null)
                this.data = dataChunk;
            else
                this.data.join(dataChunk);

            // Set chunk size
            int chunkLength = dataChunk.length();

            // Increment number of retrieved objects
            this.downloaded += chunkLength;

        } catch (Download.HTTPException|IOException e) {
            Logs.exception(e);
            e.printStackTrace();
        }
    }

    private void downloadOrganisms()
    {
        while (!downloadCompleted()) {
            downloadNextChunk();
        }
    }

    /**
     * Get next downloaded organism
     *
     * @return Data retrieved from Genbank
     * @throws InterruptedException Lock wait interruption
     */
    public synchronized RawOrganism getNext() throws InterruptedException
    {
        // Wait until their is some data
        while(this.dataQueue.size() == 0) {
            System.out.println("0sized");
            wait();
        }

        // Dequeue
        return this.dataQueue.remove(0);
    }

    /**
     * Add an organism to the queue
     *
     * @param organism Organism data to enqueue
     */
    private synchronized void enqueueOrganism(RawOrganism organism)
    {
        if(this.dataQueue.size() == 1) {
            notify();
        }
        System.out.print("-");
        this.dataQueue.add(organism);
    }

    /**
     * Returns if there is more data to be
     * @return True if their is more data to be processed
     */
    public boolean hasNext()
    {
        // TODO: Don't call size() for each iteration (poor performance)
        return !this.endOfStream || this.dataQueue.size() > 0;
    }

    public static void main(String args[])
            throws MalformedURLException, UnsupportedEncodingException {

        GenbankOrganisms go = GenbankOrganisms.getInstance();

        Thread mythread = new Thread(go);
        mythread.start();


        new Thread(() -> {
            int count = 0;
            while (go.hasNext()) {
                try {
                    System.out.println(go.getNext().getOrganism());
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("Printed %d organisms%n", count);
        }).start();

    }

    // Here SingletonHolder is static so it gets loaded in memory only
    // on the first access, this mechanism ensure that the singleton is
    // thread safe.
    // Locks lacks in performance causing poor access to the instance.
    private static class GenbankOrganismsHolder
    {
        private final static GenbankOrganisms instance = new GenbankOrganisms();
    }

    /**
     * Get singleton (only one thread for the moment)
     * @return GenbankOrganism database instance
     */
    public static GenbankOrganisms getInstance()
    {
        return GenbankOrganismsHolder.instance;
    }

    @Override
    public void run() {
        getInstance().downloadOrganisms();
    }
}
