package com.gymtime.kalyank.gymtime.common.Traffic;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

/**
 * Created by kalyanak on 12/10/2016.
 */
public class TrafficChartHelper {
    public static void configureTrafficChart(BarChart chart) {
        chart.setFitBars(true);
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        chart.setExtraBottomOffset(30f);
        chart.setHorizontalScrollBarEnabled(true);
        chart.getRendererXAxis().getPaintAxisLabels().setTextAlign(Paint.Align.LEFT);
        chart.centerViewTo(0f, 0f, YAxis.AxisDependency.LEFT);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setTextColor(Color.CYAN);
        chart.getDescription().setEnabled(false);
    }

    public static void configureTrafficYAxis(YAxis yAxis) {
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);
        yAxis.setAxisMinimum(0f);

    }

    public static void configureTrafficXAxis(XAxis xAxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setLabelRotationAngle(90f);
        xAxis.setTextColor(Color.CYAN);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(24f);
        xAxis.setLabelCount(24, true);
        xAxis.setValueFormatter(new TrafficXAxisValueFormatter());
    }
}
