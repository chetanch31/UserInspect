package visual.camp.sample.app.activity.ui.home;

import android.os.AsyncTask;
import android.util.Log;

import java.net.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CheckUrlTask extends AsyncTask<String, Void, Boolean> {
    private static final String API_KEY = "";
    private static final String API_URL = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + API_KEY;

    @Override
    protected Boolean doInBackground(String... urls) {
        String url = urls[0];
        try {
            // Encode the URL
            String encodedUrl = URLEncoder.encode(url, "UTF-8");
            Log.i("encoded_url", encodedUrl);

            // Create the JSON request body
            String requestBody = "{\n" +
                    "  \"client\": {\n" +
                    "    \"clientId\":      \"UsabilityApp\",\n" +
                    "    \"clientVersion\": \"1.0.0\"\n" +
                    "  },\n" +
                    "  \"threatInfo\": {\n" +
                    "    \"threatTypes\":      [\"MALWARE\", \"SOCIAL_ENGINEERING\", \"UNWANTED_SOFTWARE\", \"POTENTIALLY_HARMFUL_APPLICATION\"],\n" +
                    "    \"platformTypes\":    [\"WINDOWS\", \"LINUX\", \"ANDROID\", \"OSX\", \"IOS\"],\n" +
                    "    \"threatEntryTypes\": [\"URL\"],\n" +
                    "    \"threatEntries\": [\n" +
                    "      {\"url\": \"" + encodedUrl + "\"}\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

            // Send the HTTP POST request to the API
            URL apiEndpoint = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) apiEndpoint.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(requestBody);
            writer.flush();

            // Get the API response
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            String response = responseBuilder.toString();

            // Parse the API response JSON
            Gson gson = new GsonBuilder().create();
            SafeBrowsingResponse safeBrowsingResponse = gson.fromJson(response, SafeBrowsingResponse.class);

            // Check if the URL is safe
            return safeBrowsingResponse.matches.length == 0;
        } catch (Exception e) {
            // Handle any errors that occur
            e.printStackTrace();
            return false;
        }
    }

    private static class SafeBrowsingResponse {
        Match[] matches;
    }

    private static class Match {
        String threatType;
    }
}
