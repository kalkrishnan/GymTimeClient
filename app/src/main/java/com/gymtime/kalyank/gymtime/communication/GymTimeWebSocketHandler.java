package com.gymtime.kalyank.gymtime.communication;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by kalyanak on 8/31/2016.
 */
public class GymTimeWebSocketHandler implements
        AsyncHttpClient.WebSocketConnectCallback,
        rx.Observable.OnSubscribe<String>,
        WebSocket.StringCallback {


    protected List<Subscriber<? super String>> subscriberList = new ArrayList<>();


    @Override
    public void onCompleted(Exception error, WebSocket webSocket) {
        if (error != null) {
            for (Subscriber<? super String> subscriber : subscriberList) {
                subscriber.onError(error);
            }
        } else {
            webSocket.setStringCallback(this);
        }
    }


    @Override
    public void onStringAvailable(String s) {
        for (Subscriber<? super String> subscriber : subscriberList) {
            subscriber.onNext(s);
        }
    }


    @Override
    public void call(Subscriber<? super String> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            subscriberList.add(subscriber);
        } else {
            subscriber.onCompleted();
            subscriberList.remove(subscriber);
        }
    }
}
