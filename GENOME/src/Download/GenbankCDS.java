package Download;

import Utils.Logs;
import Exception.HTTPException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GenbankCDS extends Downloader {

    private static final String s_URL_BASE = "https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi";
    private static final String s_URL_DEFAULT_PARAMS = "db=nuccore&retmode=raw";

    private final String m_refseqId;
    private StringBuffer m_data;

    public GenbankCDS(String refseqId) {
        // Lazy instanciation
        // To get data you have to call getRepliconData
        m_refseqId = refseqId;
        m_data = null;
    }

    /**
     * Get the url to download the next chunk of database
     * @return A JAVA URL instance for the next chunk of data
     * @throws UnsupportedEncodingException If UTF-8 isn't supported by executing system
     * @throws MalformedURLException On malformed Url
     */
    private URL getURL() throws UnsupportedEncodingException, MalformedURLException {

        String urlStr;
        try {
            // Forge request
            urlStr = String.format("%s?%s&id=%s",
                    s_URL_BASE,
                    URLEncoder.encode(s_URL_DEFAULT_PARAMS, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(m_refseqId, StandardCharsets.UTF_8.name()));
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
     * Downloads refseq file from genbank
     * Note: If you plan to use this data consider using getRefseqData() method instead of this one.
     * @see GenbankCDS ::getRefseqData
     * @return The refseq file as a string
     * @throws IOException If output is too big or on request error
     */
    private StringBuffer download () throws HTTPException, IOException {
        BufferedReader reader;
        StringBuffer data = new StringBuffer();

        // Send GET Request
        Logs.info(String.format("Requesting refseq file [%s]", m_refseqId));
        try {
            reader = get(getURL());
            for (int c; (c = reader.read()) != -1;) {
                data.append((char) c);
            }
        } catch (HTTPException|IOException e) {
            Logs.exception(e);
            throw e;
        }

        Logs.info(String.format("Refseq [%s] : Request ended successfully (%d Bytes)", m_refseqId, data.length() ));

        return data;
    }

    /**
     * Get refseq data downloading it from genbank if necessary
     * Note: If you plan to use this data, prefer using this method instead of download() as it prevents downloading data multiple times.
     * @return Refseq data as a string
     * @throws IOException if an IOException is throw
     * @throws HTTPException if an HTTPException is throw
     */
    public StringBuffer getRefseqData () throws HTTPException, IOException {
        return m_data == null ? m_data = download() : m_data;
    }

    public int getRefseqDataSize () {
        // Assuming 1 char is one Byte
        return m_data == null ? 0 : m_data.length();
    }

}
