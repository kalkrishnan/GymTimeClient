package com.gymtime.kalyank.gymtime;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.gymtime.kalyank.gymtime.dao.Gym;

import layout.GymCommentsFragment;

/**
 * Created by kalyanak on 7/31/2016.
 */
public class GymDetailPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Gym gym;

    public GymDetailPagerAdapter(FragmentManager fm, int NumOfTabs, Gym gym) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.gym = gym;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d(GymDetailPagerAdapter.class.getCanonicalName(), Integer.toString(position));
                GymTrafficFragment tab1 = GymTrafficFragment.newInstance(gym);
                return tab1;
            case 1:
                GymCommentsFragment tab2 = GymCommentsFragment.newInstance(gym);
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }
}
