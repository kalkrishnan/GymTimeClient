package com.gymtime.kalyank.gymtime.common.Traffic;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by kalyanak on 12/8/2016.
 */
public class TrafficXAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        final int hour = (int) value;
        if (hour > 12)
           return hour-12+" PM";
        else if(hour == 12)
            return hour + " PM  ";
        return hour + " AM";
    }

}
