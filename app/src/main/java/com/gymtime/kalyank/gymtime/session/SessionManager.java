package com.gymtime.kalyank.gymtime.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kalyanak on 5/22/2016.
 */
public class SessionManager {

    public void setPreference(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("GymTime", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void setPreferences(Context context, String key, ArrayList<String> values) {

        SharedPreferences.Editor editor = context.getSharedPreferences("GymTime", Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, new HashSet<String>(values));
        editor.commit();

    }

    public Set<String> getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("GymTime", Context.MODE_PRIVATE);
        Set<String> preferences =  prefs.getStringSet(key, null);
        return preferences;
    }

    public String getPreference(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("GymTime", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void clearPreferences(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("GymTime", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
