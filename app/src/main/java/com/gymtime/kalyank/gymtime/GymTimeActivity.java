package com.gymtime.kalyank.gymtime;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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
        final ImageButton okButton = (ImageButton) findViewById(R.id.button);
        setSupportActionBar(toolbar);

        location = (EditText) findViewById(R.id.view_location_id);
        location.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() > getApplicationContext().getResources().getInteger(R.integer.min_location_length)) {
                    okButton.setEnabled(true);
                    okButton.setVisibility(View.VISIBLE);
                } else {
                    okButton.setEnabled(false);
                    okButton.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(GymTimeActivity.class.getSimpleName(), s.toString());
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(GymTimeActivity.class.getSimpleName(), s.toString());
            }

        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchGymTasks().execute
                        (new HashMap.SimpleEntry<String, String>("location", location.getText().toString().replace(" ", "+")));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gym_time, menu);
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
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String gymJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                String FORECAST_BASE_URL = getString(R.string.gym_service_url);
                Uri.Builder uriBuilder = Uri.parse(FORECAST_BASE_URL).buildUpon();
                for (int i = 0; i < urls.length; i++) {
                    uriBuilder.appendQueryParameter(urls[i].getKey().toString(), urls[i].getValue().toString());
                }

                URL url = new URL(uriBuilder.build().toString());
                Log.d(GymTimeActivity.class.getSimpleName(), "URL: " + url);
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
                gymJsonStr = buffer.toString();
                Log.d(GymTimeActivity.class.getSimpleName(), gymJsonStr);
                return gymJsonStr;

            } catch (IOException e) {
                Log.e("GymTimeActivity", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("GymTimeActivity", "Error closing stream", e);
                    }
                }
            }

        }


        @Override
        protected void onPostExecute(String gymJson) {
            Intent myIntent = new Intent(GymTimeActivity.this, GymDetailActivity.class);
            myIntent.putExtra(Intent.EXTRA_TEXT, gymJson); //Optional parameters
            GymTimeActivity.this.startActivity(myIntent);
        }

    }
}
