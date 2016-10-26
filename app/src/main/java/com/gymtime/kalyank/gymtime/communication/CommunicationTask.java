package com.gymtime.kalyank.gymtime.communication;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.gymtime.kalyank.gymtime.GymTimeActivity;

import java.util.Map;

public class CommunicationTask extends AsyncTask<Map.Entry, Void, String> {

    public interface CommunicationResponse {
        void processFinish(String output);
    }

    public CommunicationResponse delegate = null;

    public CommunicationTask(CommunicationResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Map.Entry... urls) {

        final String method = urls[0].getValue().toString();
        switch (method) {
            case "GET":
                return HTTPClient.getData(urls).getMessage();
            case "POST":

                return callHTTPPost(urls);

        }
        return null;
    }

    private String callHTTPPost(Map.Entry... urls) {
        String body = "";
        Log.d(CommunicationTask.class.getCanonicalName(), urls.toString());
        if (urls.length > 3) {
            body = "{";
            for (int i = 2; i < urls.length; i++) {
                Map.Entry part = urls[i];
                body += "\"" + part.getKey() + "\":\"" + part.getValue() + "\",";
            }
            body = body.substring(0, body.length() - 1);
            body += "}";
        } else if (urls.length > 1) {
            body = urls[2].getValue().toString();
        }
        Log.d(CommunicationTask.class.getCanonicalName(), body);
        return HTTPClient.postData(urls[1].getValue().toString(), body).getMessage();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}

