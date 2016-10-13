package layout;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.gymtime.kalyank.gymtime.R;

import java.util.Date;


public class CalendarFragment extends DialogFragment {

    private CalendarView calendar;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        getActivity().startActivity(intent);
        final Date date = new Date();
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,date);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
        return inflater.inflate(R.layout.fragment_calendar, container, false);

    }


}
