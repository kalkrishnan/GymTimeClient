package com.gymtime.kalyank.gymtime.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.dao.User;
import com.gymtime.kalyank.gymtime.session.SessionManager;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.os.Process;

public class LocationService extends Service {
    private static final String TAG = "GYMTIMEGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private Map<String, Gym> gymLocations = new HashMap<String, Gym>();
    SessionManager sessionManager = new SessionManager();
    private User user;
    Location mLastLocation;
    Boolean initialLocationSet = false;
    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            if (!initialLocationSet) {
                mLastLocation.set(location);
                initialLocationSet = true;
                searchGyms(GymTimeHelper.getLatLongFromLocation(location));
            } else {
                final String latLongFromLocation = GymTimeHelper.getLatLongFromLocation(location);
                Log.d(TAG, "latLong: " + latLongFromLocation);
                Log.d(TAG, Arrays.toString(gymLocations.keySet().toArray()));
                if (gymLocations.containsKey(latLongFromLocation)) {
                    Log.d(TAG, "latLong: " + latLongFromLocation);
                    checkIn(gymLocations.get(latLongFromLocation));
                }
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }

    }

    private void checkIn(Gym gym) {
        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
            @Override
            public void processFinish(String output) {
            }
        }).execute
                (new HashMap.SimpleEntry<String, String>("method", "POST"),
                        new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_checkin_url)),
                        new HashMap.SimpleEntry<String, String>("gymId", gym.getLatLong()),
                        new HashMap.SimpleEntry<String, String>("gymName", gym.getName()),
                        new HashMap.SimpleEntry<String, String>("userId", user.getEmail()),
                        new HashMap.SimpleEntry<String, String>("checkInTime", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(new Timestamp(System.currentTimeMillis()))));

    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        user = new GsonBuilder().create().fromJson(sessionManager.getPreference(this.getBaseContext(), Constants.USER.toString()), User.class);

        initializeLocationManager();
        try {
            Log.d(TAG, "onCreate");

            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);

        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
        try {
            Boolean isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            Boolean isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Location location = null;
            if (isNetworkEnabled) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (isGPSEnabled) {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location != null) {
                mLastLocation.set(location);
                initialLocationSet = true;
                Log.d(TAG, "Setting Location: "+GymTimeHelper.getLatLongFromLocation(location));
                searchGyms(GymTimeHelper.getLatLongFromLocation(location));
            }
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
    }


    private void searchGyms(String latLong) {

        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
            @Override
            public void processFinish(String output) {
                for (Gym gym : GymTimeHelper.parseGyms(output)) {
                    Log.d(TAG, gym.getLatLong());
                    gymLocations.put(gym.getLatLong(), gym);
                }
            }
        }).execute
                (new HashMap.SimpleEntry<String, String>("method", "GET"),
                        new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_service_latlong_url)),
                        new HashMap.SimpleEntry<String, String>("latlong", latLong),
                        new HashMap.SimpleEntry<String, String>("radius", "10"));
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.e(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}