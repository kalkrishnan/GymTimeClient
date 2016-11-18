package com.gymtime.kalyank.gymtime;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.dao.Gym;

import java.text.DecimalFormat;
import java.util.ArrayList;

import layout.OptionsButtonsFragment;

/**
 * Created by kalyank on 2/20/2016.
 */
public class GymItemAdapter extends ArrayAdapter<Gym> {

    GymListFragment parentFragment;

    public GymItemAdapter(GymListFragment _fragment, Context context, ArrayList<Gym> gyms) {

        super(context, 0, gyms);
        parentFragment = _fragment;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Gym gym = getItem(position);
        Bundle bundle = new Bundle();

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gym_item, parent, false);
            int newId = (new Double(Math.random()* 1000)).intValue() + position;
            FrameLayout layout = (FrameLayout) convertView.findViewById(R.id.favorite_button_fragment);
            layout.setId(newId);
            convertView.setTag(newId);
            Log.d(GymItemAdapter.class.getCanonicalName(), "Container Id: " + newId);
            OptionsButtonsFragment fragment = new OptionsButtonsFragment();
            bundle.putSerializable("gym", gym);
            fragment.setArguments(bundle);
            FragmentManager manager = parentFragment.getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(newId, fragment, "" + newId);
            transaction.commit();
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
