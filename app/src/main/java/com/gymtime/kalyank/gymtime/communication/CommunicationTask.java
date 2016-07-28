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

    public CommunicationTask(CommunicationResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(Map.Entry... urls) {

        return HTTPClient.getData(urls).getMessage();

    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}

