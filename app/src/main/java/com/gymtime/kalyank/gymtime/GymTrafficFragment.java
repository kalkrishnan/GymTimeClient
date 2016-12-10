package com.gymtime.kalyank.gymtime;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gymtime.kalyank.gymtime.common.Traffic.TrafficDataSet;
import com.gymtime.kalyank.gymtime.common.Traffic.TrafficValueFormatter;
import com.gymtime.kalyank.gymtime.common.Traffic.TrafficXAxisValueFormatter;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Gym;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import layout.OptionsButtonsFragment;


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

//        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
//        ratingBar.setRating(gym.getTraffic().get(0).floatValue());

        BarChart chart = (BarChart) rootView.findViewById(R.id.trafficChart);
        updateTraffic(gym.getLatLong(), chart);
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
        OptionsButtonsFragment fragment = new OptionsButtonsFragment();
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

    public void updateTraffic(String gymId, final BarChart chart) {


        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
            @Override
            public void processFinish(String output) {
                Log.d(GymTrafficFragment.class.getCanonicalName(), output);
                JsonElement jelement = new JsonParser().parse(output);

                if (jelement.isJsonArray() && jelement.getAsJsonArray().size() > 0) {
                    JsonArray jarray = jelement.getAsJsonArray();

                    List<BarEntry> entries = new ArrayList<BarEntry>();
                    for (JsonElement trafficEntry : jarray) {
                        final JsonArray trafficArray = trafficEntry.getAsJsonArray();
                        final BarEntry barEntry = new BarEntry(trafficArray.get(0).getAsInt(), trafficArray.get(1).getAsInt());
                        entries.add(barEntry);

                    }
                    BarDataSet dataSet = new TrafficDataSet(entries, "Traffic Count");
                    dataSet.setColors(Color.GREEN, Color.MAGENTA, Color.CYAN, Color.RED);
                    dataSet.setValueTextColor(Color.CYAN);
                    dataSet.setValueTextSize(12f);
                    dataSet.setHighLightColor(Color.RED);
                    BarData barData = new BarData(dataSet);
                    barData.setValueFormatter(new TrafficValueFormatter());
                    barData.setBarWidth(0.9f);
                    chart.setData(barData);
                    chart.setFitBars(true);
                    chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    chart.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                    chart.setExtraBottomOffset(30f);
                    chart.setHorizontalScrollBarEnabled(true);

                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setTextSize(10f);
                    // xAxis.setLabelCount(24)
                    xAxis.setLabelRotationAngle(90f);
                    xAxis.setTextColor(Color.CYAN);
                    xAxis.setDrawAxisLine(true);
                    xAxis.setDrawGridLines(false);
                    xAxis.setAxisMinimum(0f);
                    xAxis.setAxisMaximum(24f);
                    xAxis.setLabelCount(24, true);
                    xAxis.setValueFormatter(new TrafficXAxisValueFormatter());

                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setDrawAxisLine(true);
                    yAxis.setDrawGridLines(false);
                    yAxis.setDrawLabels(false);
                    chart.getAxisRight().setEnabled(false);
                    chart.getLegend().setTextColor(Color.CYAN);
                    chart.getDescription().setEnabled(false);
                    chart.invalidate();
                }

            }
        }).execute
                (new HashMap.SimpleEntry<String, String>("method", "GET"),
                        new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_gettraffic_url)),
                        new HashMap.SimpleEntry<String, String>("gymId", gymId));
    }


}
