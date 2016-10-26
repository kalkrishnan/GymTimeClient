package com.gymtime.kalyank.gymtime.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.primitives.Doubles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gym implements Parcelable, Serializable {

    private final String latLong;
    private final String name;
    private final String address;
    private final List<Double> traffic;
    //   private final Boolean favorite;

    public Gym(String latLong, String name, String address, List<Double> traffic) {

        this.latLong = latLong;
        this.name = name;
        this.address = address;
        this.traffic = traffic;
        //this.favorite = favorite;
    }


    protected Gym(Parcel in) {
        latLong = in.readString();
        name = in.readString();
        address = in.readString();
        double[] _traffic = in.createDoubleArray();
        traffic = Doubles.asList(_traffic);
        ;
        //favorite = in.readByte() != 0;
    }

    public static final Creator<Gym> CREATOR = new Creator<Gym>() {
        @Override
        public Gym createFromParcel(Parcel in) {
            return new Gym(in);
        }

        @Override
        public Gym[] newArray(int size) {
            return new Gym[size];
        }
    };

    public String getLatLong() {
        return latLong;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Double> getTraffic() {
        return traffic;
    }

    /*public Boolean getFavorite() {
        return favorite;
    }*/

    @Override
    public String toString() {
        return "{LatLong: " + this.getLatLong() + " Name: " + this.getName() + " Address: " + this.getAddress() + " Traffic: " + this.getTraffic().toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Gym) {
            Gym gym = (Gym) o;
            return this.getName().replaceAll("\\s+", "").equals(gym.getName().replaceAll("\\s+", "")) & this.getAddress().replaceAll("\\s+", "").equals(gym.getAddress().replaceAll("\\s+", "")) & this.getLatLong().replaceAll("\\s+", "").equals(gym.getLatLong().replaceAll("\\s+", "")) & this.getTraffic().equals(gym.getTraffic());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getLatLong().hashCode() + this.getName().hashCode() + this.getTraffic().hashCode() + this.getAddress().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latLong);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDoubleArray(Doubles.toArray(traffic));
        //dest.writeByte((byte) (favorite ? 1 : 0));
    }
}