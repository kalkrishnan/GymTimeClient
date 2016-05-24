package com.gymtime.kalyank.gymtime.communication;

import android.net.Uri;
import android.util.Log;

import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by kalyanak on 5/22/2016.
 */
public class HTTPClient {

    public static String getData(Map.Entry... urls) {

        Log.d(HTTPClient.class.getCanonicalName(), "in HTTPClient");
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String output = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String URL = urls[0].getValue().toString();
            Uri.Builder uriBuilder = Uri.parse(URL).buildUpon();
            for (int i = 1; i < urls.length; i++) {
                uriBuilder.appendQueryParameter(urls[i].getKey().toString(), urls[i].getValue().toString());
            }

            URL url = new URL(uriBuilder.build().toString());
            Log.d(HTTPClient.class.getSimpleName(), "URL: " + url);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            output = buffer.toString();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}