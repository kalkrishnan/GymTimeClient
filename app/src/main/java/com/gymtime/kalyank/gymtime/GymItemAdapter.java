package com.gymtime.kalyank.gymtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.communication.HTTPClient;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.session.SessionManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kalyank on 2/20/2016.
 */
public class GymItemAdapter extends ArrayAdapter<Gym> {
    public GymItemAdapter(Context context, ArrayList<Gym> gyms) {
        super(context, 0, gyms);
    }

    SessionManager sessionManager;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Gym gym = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gym_item, parent, false);
        }
        TextView gymName = (TextView) convertView.findViewById(R.id.gymName);
        TextView gymAddress = (TextView) convertView.findViewById(R.id.gymAddress);
        TextView gymDistance = (TextView) convertView.findViewById(R.id.gymDistance);
        gymName.setText(gym.getName());
        gymAddress.setText(gym.getAddress());

        gymDistance.setText(getDistance(gym.getLatLong()));
        final String gymId = GymTimeHelper.generateId(gym);
        sessionManager = new SessionManager();
        final String userId = sessionManager.getPreference(this.getContext(), Constants.USER_ID.toString());
        Set<String> favoriteGyms = sessionManager.getPreferences(this.getContext(), Constants.FAVORITE_GYM_IDS.toString());

        final ImageButton gymFavoriteButton = (ImageButton) convertView.findViewById(R.id.favorite);
        Log.d(GymItemAdapter.class.getCanonicalName(), "List Gym: " + gym.getName());
        if (favoriteGyms.contains(GymTimeHelper.generateId(gym))) {
            Log.d(GymItemAdapter.class.getCanonicalName(), "Favorite Gym: " + gym.getName());
            gymFavoriteButton.setSelected(true);
            gymFavoriteButton.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_on_pressed, null));
        }

        gymFavoriteButton.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!gymFavoriteButton.isSelected()) {
                        gymFavoriteButton.setSelected(true);
                        v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_on_pressed, null));
                        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                            @Override
                            public void processFinish(String output) {

                            }
                        }).execute
                                (new HashMap.SimpleEntry<String, String>("url", getContext().getString(R.string.gym_favorite_url)),
                                        new HashMap.SimpleEntry<String, String>("gymId", gymId),
                                        new HashMap.SimpleEntry<String, String>("userId", userId));
                    } else {
                        gymFavoriteButton.setSelected(false);
                        v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_off, null));
                        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                            @Override
                            public void processFinish(String output) {

                            }
                        }).execute
                                (new HashMap.SimpleEntry<String, String>("url", getContext().getString(R.string.gym_unfavorite_url)),
                                        new HashMap.SimpleEntry<String, String>("gymId", gymId),
                                        new HashMap.SimpleEntry<String, String>("userId", userId));
                    }
                }
                return true;

            }
        });
        return convertView;
    }


    @Override
    public Gym getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    private String getDistance(String latLong) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        Location home = new Location("Home");
        home.setLatitude(33.646356);
        home.setLongitude(-117.834450);

        Double endLat = Double.parseDouble(latLong.split("_")[0]);
        Double endLong = Double.parseDouble(latLong.split("_")[1]);

        Location gym = new Location("Gym");
        gym.setLatitude(endLat);
        gym.setLongitude(endLong);
        return twoDForm.format(gym.distanceTo(home) / 1600) + "mi";
    }


}
