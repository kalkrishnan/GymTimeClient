package com.gymtime.kalyank.gymtime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    public interface OnTimerSelectionListener {
        void onTimerSelection(int hourOfDay, int minute);
    }

    private OnTimerSelectionListener timerSelectionListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        timerSelectionListener = (OnTimerSelectionListener) getParentFragment();
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_DARK, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timerSelectionListener.onTimerSelection(hourOfDay, minute);
    }
}