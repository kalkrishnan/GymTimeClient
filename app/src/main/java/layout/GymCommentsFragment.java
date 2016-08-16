package layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.gymtime.kalyank.gymtime.GymCommentAdapter;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Comment;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.session.SessionManager;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GymCommentsFragment extends Fragment {

    EditText commentText;
    ImageView commentImage;
    ListView gymComments;
    byte[] imageBytes;
    SessionManager sessionManager = new SessionManager();
    public ArrayList<Comment> comments = new ArrayList<Comment>();
    private GymCommentAdapter commentAdapter;
    private int REQUEST_CODE_PICKER = 2000;
    private ArrayList<Image> commentImages = new ArrayList<>();

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
        ImageButton picButton = ((ImageButton) rootView.findViewById(R.id.comment_image_button));

        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GymCommentsFragment.this.getContext(), ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 1);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, commentImages);
                startActivityForResult(intent, REQUEST_CODE_PICKER);
            }
        });
        Button gymButton = ((Button) rootView.findViewById(R.id.comment_send));
        commentText = (EditText) rootView.findViewById(R.id.comment_text);
        commentImage = (ImageView) rootView.findViewById(R.id.user_comment_image);
        gymButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String comment = commentText.getText().toString();
                if (comments.size() == commentAdapter.getViewTypeCount())
                    comments.remove(0);
                comments.add(new Comment(comment, "@user", new Date().toString(), imageBytes));

                new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                    @Override
                    public void processFinish(String output) {

                    }
                }).execute
                        (new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_add_comments_url)),
                                new HashMap.SimpleEntry<String, String>("gymId", GymTimeHelper.generateId(gym)),
                                new HashMap.SimpleEntry<String, String>("userId", sessionManager.getPreference(getContext(), Constants.USER_ID.toString())),
                                new HashMap.SimpleEntry<String, String>("comment", comment),
                                new HashMap.SimpleEntry<String, String>("commentImage", imageBytes == null ? "" : Base64.encodeToString(imageBytes, Base64.DEFAULT)),
                                new HashMap.SimpleEntry<String, String>("timestamp", new Date().toString()));

                commentAdapter.notifyDataSetChanged();
                clearResources();
            }
        });
        return rootView;
    }

    private void clearResources() {
        commentText.getText().clear();
        commentImage.setImageResource(android.R.color.transparent);
        commentImage.setVisibility(View.GONE);
        imageBytes = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == ImagePickerActivity.RESULT_OK && data != null) {
            commentImages = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            for (int i = 0, l = commentImages.size(); i < l; i++) {
                final Bitmap imageBitmap = GymTimeHelper.getBitmapFromPath(commentImages.get(i).getPath());
                commentImage.setImageBitmap(imageBitmap);
                imageBytes = GymTimeHelper.getBytesFromBitmap(imageBitmap);

            }
            commentImage.setVisibility(View.VISIBLE);
            // commentText.setVisibility(View.GONE);

        }
    }

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
