package com.gymtime.kalyank.gymtime;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.fivehundredpx.android.blur.BlurringView;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.service.LocationService;

import java.util.HashMap;

public class GymTimeActivity extends BaseActivity {

    private EditText location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final LinearLayout root = (LinearLayout) findViewById(R.id.gymtime_view);
        BlurringView blurringView = (BlurringView) findViewById(R.id.gymtime_blurringview);
        blurringView.setBlurredView(root);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) findViewById(R.id.menu_search);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                    @Override
                    public void processFinish(String output) {
                        Intent myIntent = new Intent(GymTimeActivity.this, GymDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(getString(R.string.gym_bundle), GymTimeHelper.parseGyms(output));
                        myIntent.putExtras(bundle);
                        GymTimeActivity.this.startActivity(myIntent);
                    }
                }).execute
                        (new HashMap.SimpleEntry<String, String>("method", "GET"),
                                new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_service_url)),
                                new HashMap.SimpleEntry<String, String>("location", query));
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gym_base, menu);


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
}
