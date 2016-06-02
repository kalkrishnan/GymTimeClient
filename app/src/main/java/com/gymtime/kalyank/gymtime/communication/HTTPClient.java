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

    public static HTTPResponse getData(Map.Entry... urls) {

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

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            return new HTTPResponse(urlConnection.getResponseCode(), sb.toString());


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}