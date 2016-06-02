package com.gymtime.kalyank.gymtime.communication;

/**
 * Created by kalyanak on 6/1/2016.
 */
public class HTTPResponse {

    private final int code;
    private final String message;

    HTTPResponse(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HTTPResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
