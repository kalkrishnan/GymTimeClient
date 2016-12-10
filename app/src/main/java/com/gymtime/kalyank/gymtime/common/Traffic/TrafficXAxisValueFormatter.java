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

//        if ((int) value == GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY))
//            return "NOW";
        return Integer.toString((int) value) + ":00 Hrs";
    }

}
