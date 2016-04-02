package com.gymtime.kalyank.gymtime;

import android.app.Dialog;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.dao.Gym;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GymTrafficFragment} interface
 * to handle interaction events.
 * Use the {@link GymTrafficFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GymTrafficFragment extends DialogFragment {


    // TODO: Rename and change types and number of parameters
    public static GymTrafficFragment newInstance() {
        GymTrafficFragment f = new GymTrafficFragment();

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),  R.style.CustomDialog);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Gym gym = (Gym) bundle.getSerializable("gym");

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gym_traffic, null);
        TextView gymName = (TextView) rootView.findViewById(R.id.gymName);
        TextView gymAddress = (TextView) rootView.findViewById(R.id.gymAddress);
        TextView gymDistance = (TextView) rootView.findViewById(R.id.gymDistance);
        gymName.setText(gym.getName());
        gymAddress.setText(gym.getAddress());

        gymDistance.setText(getDistance(gym.getLatLong()));

        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating((int)Math.round(gym.getTraffic().get(0).getHowHeavyTrafficIs() * 10));
        dialog.setContentView(rootView);

        return dialog;
    }

    private void addListenerOnRatingBar() {
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
