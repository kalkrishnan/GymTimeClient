package com.gymtime.kalyank.gymtime.communication;

import android.net.Uri;
import android.util.Log;

import com.gymtime.kalyank.gymtime.GymTimeActivity;
import com.gymtime.kalyank.gymtime.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by kalyanak on 5/22/2016.
 */
public class HTTPClient {

    public static HTTPResponse getData(Map.Entry... urls) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String output = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            String URL = urls[1].getValue().toString();
            Log.d(HTTPClient.class.getCanonicalName(), "in HTTPClient: " + URL);

            Uri.Builder uriBuilder = Uri.parse(URL).buildUpon();
            for (int i = 2; i < urls.length; i++) {
                uriBuilder.appendQueryParameter(urls[i].getKey().toString(), urls[i].getValue().toString());
            }

            URL url = new URL(uriBuilder.build().toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int code = urlConnection.getResponseCode();
            Log.d(HTTPClient.class.getCanonicalName(), Integer.toString(code));
            if (code == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                Log.d(HTTPClient.class.getCanonicalName(), sb.toString());
                return new HTTPResponse(code, sb.toString());
            } else
                return new HTTPResponse(code, "");


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HTTPResponse postData(String _url, String body) {

        HttpURLConnection urlConnection = null;

        // Will contain the raw JSON response as a string.
        String output = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            Log.d(HTTPClient.class.getCanonicalName(), "in HTTPClient: " + _url);
            Log.d(HTTPClient.class.getCanonicalName(), "in HTTPClient: " + body);

            Uri.Builder uriBuilder = Uri.parse(_url).buildUpon();
            URL url = new URL(uriBuilder.build().toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.connect();
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(body);
            wr.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            Log.d(HTTPClient.class.getCanonicalName(), sb.toString());
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