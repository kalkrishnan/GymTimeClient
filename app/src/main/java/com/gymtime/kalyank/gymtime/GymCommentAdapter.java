package com.gymtime.kalyank.gymtime;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gymtime.kalyank.gymtime.dao.Gym;

import java.util.List;

/**
 * Created by kalyanak on 8/8/2016.
 */
public class GymCommentAdapter extends ArrayAdapter<String> {
    public GymCommentAdapter(Context context, List<String> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String comment = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_gym_comment, parent, false);
        }
        Log.d(GymCommentAdapter.class.getCanonicalName(),"Comment: "+comment);
        TextView gymComment = (TextView) convertView.findViewById(R.id.gym_comment);
        gymComment.setText(comment);

        return convertView;
    }

    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getViewTypeCount() {

        return 100;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
