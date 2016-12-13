package com.gymtime.kalyank.gymtime.common.Traffic;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by kalyanak on 12/10/2016.
 */
public class TrafficDataSet extends BarDataSet {
    public TrafficDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        Log.d(TrafficDataSet.class.getCanonicalName(), Integer.toString(index));
        Log.d(TrafficDataSet.class.getCanonicalName(), Integer.toString(GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY)));
        if (getEntryForIndex(index).getY() > 15)
            return mColors.get(3);
        else if (getEntryForIndex(index).getX() == GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

            return mColors.get(1);
        } else if (getEntryForIndex(index).getY() > 10)
            return mColors.get(2);
        else
            return mColors.get(0);


    }

}
