package com.gymtime.kalyank.gymtime;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.gymtime.kalyank.gymtime.communication.HTTPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GymTimeActivity extends AppCompatActivity {

    private EditText location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) findViewById(R.id.menu_search);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                new FetchGymTasks().execute
                        (new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_service_url)),
                                new HashMap.SimpleEntry<String, String>("location", query));
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gym_time, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchGymTasks extends AsyncTask<Map.Entry, Void, String> {

        @Override
        protected String doInBackground(Map.Entry... urls) {

            return HTTPClient.getData(urls);

        }


        @Override
        protected void onPostExecute(String gymJson) {
            Intent myIntent = new Intent(GymTimeActivity.this, GymDetailActivity.class);
            myIntent.putExtra(Intent.EXTRA_TEXT, gymJson); //Optional parameters
            GymTimeActivity.this.startActivity(myIntent);
        }

    }
}
