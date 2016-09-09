package com.gymtime.kalyank.gymtime.common;

import com.google.gson.Gson;
import com.gymtime.kalyank.gymtime.dao.Comment;

import rx.functions.Func1;

/**
 * Created by kalyanak on 8/31/2016.
 */
public class JsonStringToComment implements Func1<String, Comment> {
    protected final Gson jsonParser = new Gson();

    @Override
    public Comment call(String jsonString) {
        return jsonParser.fromJson(jsonString, Comment.class);
    }
}