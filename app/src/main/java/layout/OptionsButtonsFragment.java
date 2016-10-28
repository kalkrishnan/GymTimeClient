package layout;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.GsonBuilder;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.TimePickerFragment;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.dao.User;
import com.gymtime.kalyank.gymtime.session.SessionManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class OptionsButtonsFragment extends Fragment {

    private SessionManager sessionManager;
    private Gym gym;
    Set<Gym> updateFavorites = new HashSet<Gym>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_options_buttons, null);
        sessionManager = new SessionManager();
        final Bundle bundle = getArguments();
        gym = (Gym) bundle.getSerializable("gym");

        final User user = new GsonBuilder().create().fromJson(sessionManager.getPreference(this.getContext(), Constants.USER.toString()), User.class);
        final Set<Gym> favoriteGyms = user.getFavorites() != null ? user.getFavorites() : new HashSet<Gym>();
        final ImageButton timerButton = (ImageButton) rootView.findViewById(R.id.check_in);

        timerButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!timerButton.isSelected()) {
                    timerButton.setSelected(true);
                    DialogFragment timePickerFragment = new TimePickerFragment();
                    timePickerFragment.setArguments(bundle);
                    getChildFragmentManager().beginTransaction().add(timePickerFragment, timePickerFragment.getTag()).commit();
                    timePickerFragment.show(getFragmentManager(), "timePicker");
                    v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.checkin_done, null));

                } else {
                }

            }
        });
        final ImageButton gymFavoriteButton = (ImageButton) rootView.findViewById(R.id.favorite);
        if (favoriteGyms.contains(gym)) {
            Log.d(OptionsButtonsFragment.class.getCanonicalName(), "Favorite Gym Found");
            gymFavoriteButton.setSelected(true);
            gymFavoriteButton.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_on_pressed, null));
        }

        gymFavoriteButton.setOnTouchListener(new View.OnTouchListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    User updatedUser = new GsonBuilder().create().fromJson(sessionManager.getPreference(getContext(), Constants.USER.toString()), User.class);
                    updateFavorites.addAll(updatedUser.getFavorites());
                    if (!gymFavoriteButton.isSelected()) {
                        gymFavoriteButton.setSelected(true);
                        updateFavorites.add(gym);
                        v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_on_pressed, null));

                    } else {
                        gymFavoriteButton.setSelected(false);
                        updateFavorites.remove(gym);
                        v.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.btn_star_big_off, null));
                    }
                    updatedUser = User.builder().name(user.getName()).email(user.getEmail()).password(user.getPassword()).favorites(updateFavorites).build();
                    sessionManager.setPreference(getContext(), Constants.USER.toString(),
                            new GsonBuilder().create().toJson(updatedUser));
                    final String userJson = new GsonBuilder().create().toJson(updatedUser);
                    new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                        @Override
                        public void processFinish(String output) {

                        }
                    }).execute
                            (new HashMap.SimpleEntry<String, String>("method", "POST"),
                                    new HashMap.SimpleEntry<String, String>("url", getContext().getString(R.string.gym_signup_url)),
                                    new HashMap.SimpleEntry<String, String>("user", userJson));

                }
                return true;

            }
        });
        return rootView;
    }

}
