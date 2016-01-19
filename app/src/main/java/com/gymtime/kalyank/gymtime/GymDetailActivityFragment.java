package com.gymtime.kalyank.gymtime;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class GymDetailActivityFragment extends Fragment {

    public GymDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_gym_detail, container, false);;
        Bundle extras = this.getActivity().getIntent().getExtras();
        String gymJson = extras.getString(Intent.EXTRA_TEXT);

        ((TextView) rootView.findViewById(R.id.detail_text)).setText(gymJson);
        return rootView;

    }
}
