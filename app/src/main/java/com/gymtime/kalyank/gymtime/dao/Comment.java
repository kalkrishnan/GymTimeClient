package com.gymtime.kalyank.gymtime.dao;

import java.io.Serializable;

/**
 * Created by kalyanak on 8/13/2016.
 */
public class Comment implements Serializable {

    private final String comment;
    private final String user;
    private final String time;
    private final byte[] commentImageBytes;

    public Comment(String _comment, String _user, String _time, byte[] _commentImagePath) {
        this.comment = _comment;
        this.user = _user;
        this.time = _time;
        this.commentImageBytes = _commentImagePath;
    }

    public Comment(String _comment, String _user, String _time) {
        this(_comment, _user, _time, null);
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public byte[] getCommentImageBytes() {
        return commentImageBytes;
    }
}
