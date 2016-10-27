package layout;

import android.app.Activity;
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

import com.google.gson.GsonBuilder;
import com.gymtime.kalyank.gymtime.GymCommentAdapter;
import com.gymtime.kalyank.gymtime.R;
import com.gymtime.kalyank.gymtime.common.Constants;
import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.communication.CommunicationTask;
import com.gymtime.kalyank.gymtime.dao.Comment;
import com.gymtime.kalyank.gymtime.dao.Gym;
import com.gymtime.kalyank.gymtime.dao.User;
import com.gymtime.kalyank.gymtime.session.SessionManager;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;


public class GymCommentsFragment extends Fragment {

    EditText commentText;
    ImageView commentImage;
    ListView gymComments;
    byte[] imageBytes;
    SessionManager sessionManager = new SessionManager();
    public List<Comment> comments = Collections.synchronizedList(new ArrayList<Comment>());
    private GymCommentAdapter commentAdapter;
    private int REQUEST_CODE_PICKER = 2000;
    private ArrayList<Image> commentImages = new ArrayList<>();
    private StompClient mStompClient;

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
        getLatestComments(gym.getLatLong());
        commentAdapter.addAll(comments);
        gymComments.setAdapter(commentAdapter);
        ImageButton picButton = ((ImageButton) rootView.findViewById(R.id.comment_image_button));
        registerObservable(gym.getLatLong());
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
                final User user = new GsonBuilder().create().fromJson(sessionManager.getPreference(getContext(), Constants.USER.toString()), User.class);

                new CommunicationTask(new CommunicationTask.CommunicationResponse() {
                    @Override
                    public void processFinish(String output) {

                    }
                }).execute
                        (new HashMap.SimpleEntry<String, String>("method", "POST"),
                                new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_add_comments_url)),
                                new HashMap.SimpleEntry<String, String>("gymId", gym.getLatLong()),
                                new HashMap.SimpleEntry<String, String>("userId", user.getEmail()),
                                new HashMap.SimpleEntry<String, String>("comment", comment),
                                new HashMap.SimpleEntry<String, String>("commentImage", imageBytes == null ? "" : Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE)),
                                new HashMap.SimpleEntry<String, String>("time", new Date().toString()));

                commentAdapter.notifyDataSetChanged();
                clearResources();
            }
        });
        return rootView;
    }

    private void registerObservable(String gymId) {
        mStompClient = Stomp.over(WebSocket.class, "ws://10.0.2.2:8080/messages/websocket");
        mStompClient.connect();
        mStompClient.topic("/topic/comments/"+gymId).subscribe(new Action1<StompMessage>() {

            @Override
            public void call(StompMessage stompMessage) {
                final Comment comment = new GsonBuilder().create().fromJson(stompMessage.getPayload(), Comment.class);
                Log.d(GymCommentsFragment.class.getCanonicalName(), comment.getComment());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

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

    public void getLatestComments(String gymId) {
        new CommunicationTask(new CommunicationTask.CommunicationResponse() {
            @Override
            public void processFinish(String output) {
                final Collection<? extends Comment> latestComments = GymTimeHelper.parseComments(output);
                comments.addAll(latestComments);
            }
        }).execute
                (new HashMap.SimpleEntry<String, String>("method", "GET"),
                        new HashMap.SimpleEntry<String, String>("url", getString(R.string.gym_comments_url)),
                        new HashMap.SimpleEntry<String, String>("gymId", gymId));
    }

}
