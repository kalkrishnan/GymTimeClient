package com.gymtime.kalyank.gymtime.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gymtime.kalyank.gymtime.dao.GeneralTraffic;
import com.gymtime.kalyank.gymtime.dao.Gym;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    private static Gym parseGym(JsonObject gymJson) {

        String name = gymJson.get("name").toString().replace("\"", "");
        String address = gymJson.get("address").toString().replace("\"", "");
        String latLong = gymJson.get("id").toString().replace("\"", "");
        final double trafficStrength = parseTrafficStrength(gymJson.get("traffic").getAsJsonArray().get(0).getAsJsonObject());

        return new Gym(latLong, name, address, new ArrayList() {{
            add(new GeneralTraffic(trafficStrength));
        }});
    }


    private static double parseTrafficStrength(JsonObject traffic) {
        return traffic.get("trafficStrength").getAsDouble();
    }


    @NonNull
    public static String generateId(Gym gym) {
        return gym.getName() + gym.getAddress();
    }

    public static ArrayList<String> generateFavoriteGymIds(ArrayList<Gym> listGyms) {

        ArrayList<String> favoriteGyms = new ArrayList<>();
        for (Gym gym : listGyms) {
            favoriteGyms.add(generateId(gym));
        }
        return favoriteGyms;
    }

    public static Bitmap getBitmapFromPath(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap imageBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    }
}
