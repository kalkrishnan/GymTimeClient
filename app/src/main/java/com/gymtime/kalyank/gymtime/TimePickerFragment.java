package com.gymtime.kalyank.gymtime;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;
import com.google.gson.GsonBuilder;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.dao.User;
import com.gymtime.kalyank.gymtime.session.SessionManager;

import java.sql.Timestamp;
import java.util.HashMap;

public class TimePickerFragment extends DialogFragment
        implements DatePickerCallback, TimePickerCallback, AdapterView.OnItemSelectedListener {

    SessionManager sessionManager = new SessionManager();
    private Boolean calendarModRequestApproved = false;
    private final int MY_PERMISSIONS_REQUEST_MODIFY_CALENDAR = 111;
    private Timestamp checkInTime;
    private int reminderTime;
    private Gym gym;
    private User user;
    private TextView checkInTimeText;

    @Override
    public void onStart() {
        super.onStart();
        this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDateSet(long date) {
        Log.d(TimePickerFragment.class.getCanonicalName(), "On Date Set: " + Long.toString(date));
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {
        checkInTime = new Timestamp(dateWithTime);
        checkInTimeText.setText(checkInTime.toString());


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        reminderTime = GymTimeHelper.getReminderTime((String) parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        reminderTime = GymTimeHelper.getReminderTime(getString(R.string.no_reminders));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.content_time_picker, null);
        DatePickerFragmentDialog.newInstance(true).show(getChildFragmentManager(), "DatePickerFragmentDialog");
        Spinner spinner = (Spinner) rootView.findViewById(R.id.checkin_reminder);
        checkInTimeText = (TextView) rootView.findViewById(R.id.checkin_text);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.reminder_times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(this);
        Button okButton = (Button) rootView.findViewById(R.id.checkin_done_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checkIn();


            }
        });

        Button cancelButton = (Button) rootView.findViewById(R.id.checkin_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        return rootView;

    }

    public void checkIn() {
        user = new GsonBuilder().create().fromJson(sessionManager.getPreference(getContext(), Constants.USER.toString()), User.class);
        Bundle bundle = getArguments();
        gym = (Gym) bundle.getSerializable("gym");

        requestPermissions(
                new String[]{Manifest.permission.WRITE_CALENDAR},
                MY_PERMISSIONS_REQUEST_MODIFY_CALENDAR);

        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
            @Override
            public void processFinish(String output) {
            }
        }).execute
                (new HashMap.SimpleEntry<String, String>("method", "GET"),
                        new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_checkin_url)),
                        new HashMap.SimpleEntry<String, String>("gymId", gym.getLatLong()),
                        new HashMap.SimpleEntry<String, String>("gymName", gym.getName()),
                        new HashMap.SimpleEntry<String, String>("userId", user.getEmail()),
                        new HashMap.SimpleEntry<String, String>("checkInTime", checkInTime.toString()));
        Log.d(GymTrafficFragment.class.getCanonicalName(), checkInTime.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(GymTrafficFragment.class.getCanonicalName(), "Request Approved " + requestCode);

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_MODIFY_CALENDAR: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    insertCheckInEvent();
                }
                return;
            }

        }
    }

    private void insertCheckInEvent() {
        Log.d(GymTrafficFragment.class.getCanonicalName(), "Check In");

        ContentResolver cr = getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, checkInTime.getTime());
        values.put(CalendarContract.Events.DTEND, checkInTime.getTime() + 3600 * 1000);
        values.put(CalendarContract.Events.TITLE, "Check In");
        values.put(CalendarContract.Events.DESCRIPTION, "Check In at " + gym.getName());
        values.put(CalendarContract.Events.EVENT_LOCATION, gym.getAddress());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        AsyncQueryHandler handler = new MyCheckInHandler(cr, reminderTime);
        handler.startInsert(0, null, CalendarContract.Events.CONTENT_URI, values);
    }

    private class MyCheckInHandler extends AsyncQueryHandler {

        int reminderTime;

        public MyCheckInHandler(ContentResolver cr, int reminderTime) {
            super(cr);
            this.reminderTime = reminderTime;
        }

        @Override
        protected void onInsertComplete(int token, Object cookie, Uri uri) {

            if (uri != null) {
                long eventId = Long.parseLong(uri.getLastPathSegment());
                Log.d(GymTrafficFragment.class.getCanonicalName(), "Event Id: " + eventId);
                if (reminderTime > 0) {
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Reminders.EVENT_ID, eventId);
                    values.put(CalendarContract.Reminders.MINUTES, reminderTime);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    this.startInsert(0, null, CalendarContract.Reminders.CONTENT_URI, values);

                }
            }
            dismiss();
        }
    }

}