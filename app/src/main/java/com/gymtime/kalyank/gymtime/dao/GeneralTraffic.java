package com.gymtime.kalyank.gymtime.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class GeneralTraffic implements Parcelable{

    private double trafficStrength;

    public GeneralTraffic(double trafficStrength) {

        this.trafficStrength = trafficStrength;
    }

    protected GeneralTraffic(Parcel in) {
        trafficStrength = in.readDouble();
    }

    public static final Creator<GeneralTraffic> CREATOR = new Creator<GeneralTraffic>() {
        @Override
        public GeneralTraffic createFromParcel(Parcel in) {
            return new GeneralTraffic(in);
        }

        @Override
        public GeneralTraffic[] newArray(int size) {
            return new GeneralTraffic[size];
        }
    };

    public double getHowHeavyTrafficIs() {
        return this.trafficStrength;
    }

    @Override
    public String toString() {
        return "Traffic: "+this.getHowHeavyTrafficIs();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(trafficStrength);
    }
}