package com.gymtime.kalyank.gymtime;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.dao.Gym;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by kalyank on 2/20/2016.
 */
public class GymAdapter extends ArrayAdapter<Gym> {
    public GymAdapter(Context context, ArrayList<Gym> gyms) {
        super(context, 0, gyms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Gym gym = getItem(position);
        Log.d(GymAdapter.class.getSimpleName(), gym.toString());
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

        return convertView;
    }

    @Override
    public Gym getItem(int position) {
        return super.getItem(position);
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
        return twoDForm.format(gym.distanceTo(home) / 1600)+"mi";
    }
}
