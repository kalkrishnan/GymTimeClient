package com.gymtime.kalyank.gymtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gymtime.kalyank.gymtime.dao.GeneralTraffic;
import com.gymtime.kalyank.gymtime.dao.Gym;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class GymDetailActivityFragment extends Fragment {

    private GymItemAdapter gymAdapter;

    public GymDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gym_detail, container, false);

        Bundle extras = this.getActivity().getIntent().getExtras();


        if (!extras.getParcelableArrayList(getString(R.string.gym_bundle)).isEmpty()) {
            gymAdapter = new GymItemAdapter(this.getContext(), new ArrayList<Gym>());
            ListView gymView = ((ListView) rootView.findViewById(R.id.gym_detail));
            gymView.setAdapter(gymAdapter);
            List<Gym> gymJson = extras.getParcelableArrayList(getString(R.string.gym_bundle));
            gymAdapter.addAll(gymJson);
            gymView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GymTrafficFragment frag = new GymTrafficFragment();

                    Bundle bundles = new Bundle();
                    Gym item = (Gym) parent.getItemAtPosition(position);
                    Log.d(GymDetailActivityFragment.class.getSimpleName(), Integer.toString(item.getTraffic().size()));
                    if (item != null) {

                        bundles.putSerializable("gym", item);
                        Log.e("gym", "is valid");

                    } else {
                        Log.e("gym", "is null");

                    }

                    frag.setArguments(bundles);

                    frag.show(getFragmentManager(), "traffic");
                }
            });
        } else {
            rootView.setVisibility(View.INVISIBLE);
        }
        return rootView;

    }
}
