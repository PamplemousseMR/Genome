package Download;

import Exception.HTTPException;
import Utils.Logs;
import Utils.Options;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class IDownloader {

    /**
     * Connection
     */
    private HttpURLConnection m_connection;

    /**
     * Make an HTTP GET request and parse result as json
     *
     * @param _url The request URL to retrieve data from
     * @return The JSONObject parsed from server response
     * @throws IOException   An error occurred while connecting to the server
     * @throws JSONException Invalid JSON
     */
    protected JSONObject getJSON(URL _url) throws IOException, JSONException, HTTPException {
        // Response buffer
        StringBuilder responseText = new StringBuilder("");
        // Line buffer
        String line;

        // Make request
        try (BufferedReader in = get(_url)) {
            while ((line = in.readLine()) != null)
                responseText.append(line);
        } catch (IOException | HTTPException e) {
            Logs.exception(e);
            throw e;
        }

        // Parse result and return it
        JSONObject obj;
        try {
            obj = new JSONObject(responseText.toString());
        } catch (JSONException e) {
            Logs.exception(e);
            throw e;
        }
        return obj;
    }

    /**
     * Make an HTTP GET request
     *
     * @param _url The request URL to retrieve data from
     * @return The BufferedReader from which to read result
     * @throws IOException An error occurred while connecting to the server
     */
    protected BufferedReader get(URL _url) throws IOException, HTTPException {
        BufferedReader in;

        // Close previous connection if any
        if (m_connection != null)
            m_connection.disconnect();

        // Initialize request
        try {
            m_connection = (HttpURLConnection) _url.openConnection();
            m_connection.setConnectTimeout(Options.getConnectionTimeout());
            m_connection.setRequestMethod("GET");
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Execute request
        int statusCode;
        try {
            // Get response code (executes request)
            statusCode = m_connection.getResponseCode();
        } catch (java.net.SocketTimeoutException | java.net.UnknownHostException e) {
            Logs.info("IDownloader: Unable to establish connection with server.");
            Logs.exception(e);
            throw e;
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Check response code
        if (statusCode == HttpURLConnection.HTTP_OK) {
            // Get response stream
            try {
                in = new BufferedReader(new InputStreamReader(m_connection.getInputStream()));
            } catch (IOException e) {
                Logs.exception(e);
                throw e;
            }
        } else {
            m_connection.disconnect();
            throw new HTTPException(String.valueOf(statusCode));
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
