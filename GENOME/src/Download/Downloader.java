package Download;

import Utils.Logs;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Downloader {

    public JSONObject toJSON(BufferedReader in)
    {
        StringBuilder responseText = new StringBuilder("");
        String line;
        try {
            while ((line = in.readLine()) != null)
                responseText.append(line);
            in.close();
        } catch (IOException e) {
            Logs.exception(e);
            e.printStackTrace();
        }

        return new JSONObject(responseText.toString());
    }

    public BufferedReader get(URL url) throws HTTPException, IOException {

        BufferedReader in = null;

        // Initialize request
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Execute it
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {

            // Get response stream
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            //connection.disconnect();

        } else {
            connection.disconnect();
            // HttpException
            throw new HTTPException(responseCode);
        }

        return in;

    }
}
