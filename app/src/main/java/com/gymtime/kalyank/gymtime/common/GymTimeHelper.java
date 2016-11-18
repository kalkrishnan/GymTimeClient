package com.gymtime.kalyank.gymtime.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.util.Base64;

import com.gymtime.kalyank.gymtime.dao.Comment;
import com.gymtime.kalyank.gymtime.dao.Gym;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by kalyanak on 7/26/2016.
 */
public class GymTimeHelper {


    public static ArrayList<Gym> parseGyms(String gymJson) {
        JsonElement jelement = new JsonParser().parse(gymJson);
        JsonObject jobject;
        JsonArray jarray = jelement.getAsJsonArray();
        ArrayList<Gym> gyms = new ArrayList<Gym>();
        for (JsonElement jsonElement : jarray) {

            jobject = jsonElement.getAsJsonObject();
            gyms.add(parseGym(jobject));

        }
        return gyms;
    }

    private static Gym parseGym(final JsonObject gymJson) {

        String name = gymJson.get("name").toString().replace("\"", "");
        String address = gymJson.get("address").toString().replace("\"", "");
        String latLong = gymJson.get("latLong").toString().replace("\"", "");
        Log.d(GymTimeHelper.class.getCanonicalName(), latLong);
        return new Gym(latLong, name, address, new ArrayList() {{
            add(Double.parseDouble(gymJson.get("traffic").getAsJsonArray().get(0).getAsString()));
        }});
    }

    @NonNull
    public static String getLatLongFromLocation(Location location) {
        return String.format("%.2f", location.getLatitude()) + "_" + String.format("%.2f", location.getLongitude());
    }

    public static Bitmap getBitmapFromPath(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    }

    public static Collection<? extends Comment> parseComments(String output) {
        if (output != null) {
            JsonElement jelement = new JsonParser().parse(output);
            JsonObject jobject;
            jelement = jelement.getAsJsonObject().getAsJsonObject("_embedded").getAsJsonArray("comments");
            if (jelement.isJsonArray()) {
                JsonArray jarray = jelement.getAsJsonArray();
                ArrayList<Comment> gyms = new ArrayList<Comment>();
                for (JsonElement jsonElement : jarray) {

                    jobject = jsonElement.getAsJsonObject();
                    gyms.add(parseComment(jobject));

                }
                return gyms;
            }
            return Collections.EMPTY_LIST;
        }
        return Collections.EMPTY_LIST;
    }

    private static Comment parseComment(JsonObject commentJson) {

        String comment = commentJson.get("comment").toString().replace("\"", "");
        String userId = commentJson.get("userId").toString().replace("\"", "");
        String time = commentJson.get("time").toString().replace("\"", "");
        Log.d(GymTimeHelper.class.getCanonicalName(), commentJson.get("commentImage").toString());
        byte[] commentImage = commentJson.get("commentImage").toString().isEmpty() ? null : Base64.decode(commentJson.get("commentImage").toString().replace("\"", ""),
                Base64.NO_WRAP | Base64.URL_SAFE);

        return new Comment(comment, userId, time, commentImage);
    }

    public static int getReminderTime(String reminderTime) {
        switch (reminderTime) {
            case "15 minutes":
                return 15;
            case "30 minutes":
                return 30;
            case "45 minutes":
                return 45;
            case "1 hour":
                return 60;
            case "2 hours":
                return 120;
            case "12 hours":
                return 720;
            case "24 hours":
                return 1440;
            default:
                return 0;
        }
    }
}
