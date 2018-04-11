package Download;

import Exception.HTTPException;
import Exception.OutOfMemoryException;
import Utils.Logs;
import Utils.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class GenbankCDS extends IDownloader {

    /**
     * Request
     */
    private static final String s_REQUEST = "db=nuccore&retmode=raw&withparts=on&basic_feat=on";
    /**
     * ID of the CDS
     */
    private final String m_refseqId;
    /**
     * String buffer used to store result
     */
    private final StringBuilder m_data;

    /**
     * Constructor
     *
     * @param _refseqId the id of the CDS
     */
    public GenbankCDS(String _refseqId) {
        m_refseqId = _refseqId;
        m_data = new StringBuilder();
    }

    /**
     * Get the url to download the next chunk of database
     *
     * @return A JAVA URL instance for the next chunk of data
     * @throws MalformedURLException On malformed Url
     */
    private URL getURL() throws MalformedURLException {
        final String urlStr = String.format("%s?%s&id=%s", Options.getCDSBaseUrl(), s_REQUEST, m_refseqId);

        final URL res;
        try {
            res = new URL(urlStr);
        } catch (MalformedURLException e) {
            Logs.warning("Unable to create an url : " + urlStr);
            Logs.exception(e);
            throw e;
        }

        return res;
    }

    /**
     * Downloads refseq file from genbank
     *
     * @throws IOException          if an IOException is throw
     * @throws HTTPException        if an HTTPException is throw
     * @throws OutOfMemoryException A savage out of memory appear
     */
    public void download() throws HTTPException, IOException, OutOfMemoryException {
        Logs.info(String.format("Requesting sequence file [%s]", m_refseqId), true);
        final BufferedReader reader;
        URL url = null;
        try {
            url = getURL();
            reader = get(url);
            for (int c; (c = reader.read()) != -1; ) {
                m_data.append((char) c);
            }
        } catch (HTTPException | IOException e) {
            Logs.warning("Unable to get data : " + m_refseqId + " : " + url);
            Logs.exception(e);
            throw e;
        } catch (OutOfMemoryError e) {
            Logs.warning("Out of memory : " + m_refseqId + " : " + url);
            Logs.exception(new Exception(e));
            throw new OutOfMemoryException(e.getMessage());
        }
        Logs.info(String.format("Sequence [%s] : Request ended successfully (%d Bytes)", m_refseqId, m_data.length()), true);
    }

    /**
     * Get refseq data downloading it from genbank if necessary
     * Note: If you plan to use this data, prefer using this method instead of download() as it prevents downloading data multiple times.
     *
     * @return Refseq data as a string
     */
    public StringBuilder getRefseqData() {
        return m_data;
    }

}
