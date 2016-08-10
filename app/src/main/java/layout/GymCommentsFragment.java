package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.gymtime.kalyank.gymtime.GymCommentAdapter;
import com.gymtime.kalyank.gymtime.GymDetailActivity;
import com.gymtime.kalyank.gymtime.GymDetailPagerAdapter;
import com.gymtime.kalyank.gymtime.GymDetailTabs;
import com.gymtime.kalyank.gymtime.GymListFragment;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.session.SessionManager;
import com.mobsandgeeks.adapters.CircularListAdapter;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GymCommentsFragment extends Fragment {

    EditText commentText;
    ListView gymComments;
    SessionManager sessionManager = new SessionManager();
    public ArrayList<String> comments = new ArrayList<String>();
    private GymCommentAdapter commentAdapter;

    public static GymCommentsFragment newInstance(Gym gym) {
        Bundle bundles = new Bundle();
        bundles.putSerializable("gym", gym);
        GymCommentsFragment f = new GymCommentsFragment();
        f.setArguments(bundles);

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Gym gym = (Gym) getArguments().getSerializable("gym");
        View rootView = inflater.inflate(R.layout.fragment_gym_comments, container, false);
        gymComments = ((ListView) rootView.findViewById(R.id.gym_comments));
        commentAdapter = new GymCommentAdapter(this.getContext(), comments);
        commentAdapter.addAll(comments);
        gymComments.setAdapter(commentAdapter);
        Button gymButton = ((Button) rootView.findViewById(R.id.comment_send));
        commentText = (EditText) rootView.findViewById(R.id.comment_text);
        gymButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String comment = commentText.getText().toString();
                commentText.getText().clear();
                Log.d(GymCommentsFragment.class.getCanonicalName(), "Adding Comment: " + comment);
                if (comments.size() == commentAdapter.getViewTypeCount())
                    comments.remove(0);
                comments.add(comment);
                new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                    @Override
                    public void processFinish(String output) {

                    }
                }).execute
                        (new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_add_comments_url)),
                                new HashMap.SimpleEntry<String, String>("gymId", GymTimeHelper.generateId(gym)),
                                new HashMap.SimpleEntry<String, String>("userId", sessionManager.getPreference(getContext(), Constants.USER_ID.toString())),
                                new HashMap.SimpleEntry<String, String>("comment", comment),
                                new HashMap.SimpleEntry<String, String>("timestamp", new Date().toString()));

                commentAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
