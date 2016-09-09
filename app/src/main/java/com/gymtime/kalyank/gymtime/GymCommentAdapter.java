package com.gymtime.kalyank.gymtime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.common.GymTimeHelper;
import com.gymtime.kalyank.gymtime.dao.Comment;

import java.util.List;

/**
 * Created by kalyanak on 8/8/2016.
 */
public class GymCommentAdapter extends ArrayAdapter<Comment> {
    public GymCommentAdapter(Context context, List<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gym_comment, parent, false);
        }
        Log.d(GymCommentAdapter.class.getCanonicalName(), "Comment: " + comment);
        TextView gymComment = (TextView) convertView.findViewById(R.id.gym_comment);
        gymComment.setText(comment.getComment());
        TextView commentUser = (TextView) convertView.findViewById(R.id.comment_user);
        commentUser.setText(comment.getUserId());
        TextView commentTime = (TextView) convertView.findViewById(R.id.comment_time);
        commentTime.setText(comment.getTime());
        final byte[] imageBytes = comment.getCommentImageBytes();
        if (imageBytes != null) {
            ImageView commentImage = (ImageView) convertView.findViewById(R.id.comment_image);
            commentImage.setImageBitmap(GymTimeHelper.getBitmapFromBytes(imageBytes));
            ;
            commentImage.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public Comment getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {

        return 1000;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
