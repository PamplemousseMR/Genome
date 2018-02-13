package Download;

import Utils.Logs;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Downloader {

    protected JSONObject toJSON(BufferedReader in) throws IOException, JSONException {
        StringBuilder responseText = new StringBuilder("");
        String line;
        try {
            while ((line = in.readLine()) != null)
                responseText.append(line);
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        } finally {
            in.close();
        }
        return new JSONObject(responseText.toString());
    }

    protected BufferedReader get(URL url) throws HTTPException, IOException {

        BufferedReader in;

        // Initialize request
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Execute it
        int responseCode = -1;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }
        if (responseCode == 200) {
            // Get response stream
            try {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                Logs.exception(e);
                throw e;
            }
        } else {
            connection.disconnect();
            // HttpException
            throw new HTTPException(responseCode);
        }

        return in;

    }
}
