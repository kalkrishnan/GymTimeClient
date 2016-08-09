package layout;

import android.content.Context;
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
import com.gymtime.kalyank.gymtime.R;

import java.util.ArrayList;


public class GymCommentsFragment extends Fragment {

    EditText commentText;
    ListView gymComments;
    public ArrayList<String> comments = new ArrayList<String>();
    private GymCommentAdapter commentAdapter;

    public GymCommentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                Log.d(GymCommentsFragment.class.getCanonicalName(), "Adding Comment: "+comment);
                comments.add(comment);
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
