package Download;

import Utils.Logs;
import Utils.Options;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Downloader {

    private HttpURLConnection m_connection;

    /**
     * Make an HTTP GET request and parse result as json
     * @param _url The request URL to retrieve data from
     * @return The JSONObject parsed from server response
     * @throws IOException An error occurred while connecting to the server
     * @throws JSONException Invalid JSON
     * @throws HTTPException Invalid response code
     */
    protected JSONObject getJSON(URL _url) throws IOException, HTTPException, JSONException {
        // Response buffer
        StringBuilder responseText = new StringBuilder("");
        // Line buffer
        String line;

        // Make request
        try (BufferedReader in = get(_url)) {
            while ((line = in.readLine()) != null)
                responseText.append(line);
        } catch(HTTPException e){
            Logs.exception(e);
            throw e;
        } catch(IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Parse result and return it
        JSONObject obj = null;
        try{
            obj = new JSONObject(responseText.toString());
        }catch(JSONException e){
            Logs.exception(e);
            throw e;
        }
        return obj;
    }

    /**
     * Make an HTTP GET request
     * @param _url The request URL to retrieve data from
     * @return The BufferedReader from which to read result
     * @throws IOException An error occurred while connecting to the server
     * @throws HTTPException Invalid response code
     */
    private BufferedReader get(URL _url) throws HTTPException, IOException {

        BufferedReader in;

        // Close previous connection if any
        if (m_connection != null)
            m_connection.disconnect();

        // Initialize request
        try {
            m_connection = (HttpURLConnection) _url.openConnection();

            // Set connection timeout
            m_connection.setConnectTimeout(Options.getConnectionTimeout());

            m_connection.setRequestMethod("GET");
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Execute request
        int responseCode;
        try {
            // Get response code (executes request)
            responseCode = m_connection.getResponseCode();
        } catch (java.net.SocketTimeoutException | java.net.UnknownHostException e) {
            Logs.info("Downloader: Unable to establish connection with server.");
            Logs.exception(e);
            throw e;
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Check response code
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Get response stream
            try {
                in = new BufferedReader(new InputStreamReader(m_connection.getInputStream()));
            } catch (IOException e) {
                Logs.exception(e);
                throw e;
            }
        } else {
            m_connection.disconnect();
            // HttpException
            throw new HTTPException(responseCode);
        }

        return in;
    }

    /**
     * Disconnect current connection and destroy socket
     */
    protected void disconnect() {
        if (m_connection != null) m_connection.disconnect();
    }
}
