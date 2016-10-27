package com.gymtime.kalyank.gymtime;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.dao.Gym;

import java.text.DecimalFormat;

import layout.FavoriteButtonFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this parentFragment must implement the
 * {@link GymTrafficFragment} interface
 * to handle interaction events.
 * Use the {@link GymTrafficFragment#newInstance} factory method to
 * create an instance of this parentFragment.
 */
public class GymTrafficFragment extends Fragment {


    private Gym gym;

    public static GymTrafficFragment newInstance(Gym gym) {
        Bundle bundles = new Bundle();
        bundles.putSerializable("gym", gym);
        GymTrafficFragment f = new GymTrafficFragment();
        f.setArguments(bundles);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this parentFragment
        final Bundle bundle = getArguments();
        gym = (Gym) bundle.getSerializable("gym");

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gym_traffic, null);
//        final ImageButton timerButton = (ImageButton) rootView.findViewById(R.id.check_in);
//
//        timerButton.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View v) {
//                if (!timerButton.isSelected()) {
//                    timerButton.setSelected(true);
//                    DialogFragment timePickerFragment = new TimePickerFragment();
//                    timePickerFragment.setArguments(bundle);
//                    getChildFragmentManager().beginTransaction().add(timePickerFragment, timePickerFragment.getTag()).commit();
//                    timePickerFragment.show(getFragmentManager(), "timePicker");
//                    v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.checkin_done, null));
//
//                } else {
//                }
//
//            }
//        });

        addFavoriteButton(bundle, gym);
        TextView gymName = (TextView) rootView.findViewById(R.id.gymName);
        TextView gymAddress = (TextView) rootView.findViewById(R.id.gymAddress);
        TextView gymDistance = (TextView) rootView.findViewById(R.id.gymDistance);
        gymName.setText(gym.getName());
        gymAddress.setText(gym.getAddress());

        gymDistance.setText(getDistance(gym.getLatLong()));

        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating(gym.getTraffic().get(0).floatValue());

//        SpeedometerGauge speedometer = (SpeedometerGauge) rootView.findViewById(R.id.trafficmeter);
//        speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter() {
//            @Override
//            public String getLabelFor(double progress, double maxProgress) {
//                return String.valueOf((int) Math.round(progress));
//            }
//        });
//        // configure value range and ticks
//        speedometer.setMaxSpeed(100);
//        speedometer.setMajorTickStep(10);
//        speedometer.setMinorTicks(2);
//
//        // Configure value range colors
//        speedometer.addColoredRange(0, 35, Color.GREEN);
//        speedometer.addColoredRange(35 , 70, Color.YELLOW);
//        speedometer.addColoredRange(70, 100, Color.RED);
//        speedometer.setSpeed((float)gym.getTraffic().get(0).getHowHeavyTraffic()*100);
        return rootView;

    }

    private void addFavoriteButton(Bundle bundle, Gym gym) {
        FavoriteButtonFragment fragment = new FavoriteButtonFragment();
        bundle.putSerializable("gym", this.gym);
        fragment.setArguments(bundle);
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.favorite_button_traffic, fragment);
        transaction.commit();
    }


    private String getDistance(String latLong) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        Location home = new Location("Home");
        home.setLatitude(33.646356);
        home.setLongitude(-117.834450);

        Double endLat = Double.parseDouble(latLong.split("_")[0]);
        Double endLong = Double.parseDouble(latLong.split("_")[1]);

        Location gymLocation = new Location("Gym");
        gymLocation.setLatitude(endLat);
        gymLocation.setLongitude(endLong);
        return twoDForm.format(gymLocation.distanceTo(home) / 1600) + "mi";
    }


}
