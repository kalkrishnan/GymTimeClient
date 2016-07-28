package com.gymtime.kalyank.gymtime.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Gym implements Parcelable, Serializable{

    private final String latLong;
    private final String name;
    private final String address;
    private final List<GeneralTraffic> traffic;
 //   private final Boolean favorite;

    public Gym(String latLong, String name, String address, List<GeneralTraffic> traffic) {

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
        traffic = new ArrayList<>();
        in.readTypedList(traffic, GeneralTraffic.CREATOR);
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

    public List<GeneralTraffic> getTraffic() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latLong);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeTypedList(traffic);
        //dest.writeByte((byte) (favorite ? 1 : 0));
    }
}