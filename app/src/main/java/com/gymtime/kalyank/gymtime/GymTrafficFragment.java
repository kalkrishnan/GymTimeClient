package com.gymtime.kalyank.gymtime;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class GymTrafficFragment extends Fragment implements TimePickerFragment.OnTimerSelectionListener {


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
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        Gym gym = (Gym) bundle.getSerializable("gym");

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_gym_traffic, null);
        Button timerButton = (Button) rootView.findViewById(R.id.check_in);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new TimePickerFragment();
//                getChildFragmentManager().beginTransaction().add(timePickerFragment, timePickerFragment.getTag()).commit();
//                timePickerFragment.show(getFragmentManager(), "timePicker");

            }
        });
        TextView gymName = (TextView) rootView.findViewById(R.id.gymName);
        TextView gymAddress = (TextView) rootView.findViewById(R.id.gymAddress);
        TextView gymDistance = (TextView) rootView.findViewById(R.id.gymDistance);
        gymName.setText(gym.getName());
        gymAddress.setText(gym.getAddress());

        gymDistance.setText(getDistance(gym.getLatLong()));

        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        ratingBar.setRating((float) gym.getTraffic().get(0).getHowHeavyTrafficIs());

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
//        speedometer.setSpeed((float)gym.getTraffic().get(0).getHowHeavyTrafficIs()*100);
        return rootView;

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

    @Override
    public void onTimerSelection(int hourOfDay, int minute) {
        Log.d(GymTrafficFragment.class.getCanonicalName(), "HOUR: " + hourOfDay + " MINUTE: " + minute);
    }
}
