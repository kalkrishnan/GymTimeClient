package com.gymtime.kalyank.gymtime.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Gym implements Serializable {

    private final String latLong;
    private final String name;
    private final String address;
    private final List<GeneralTraffic> traffic;

    public Gym(String latLong, String name, String address, List<GeneralTraffic> traffic) {

        this.latLong = latLong;
        this.name = name;
        this.address = address;
        this.traffic = traffic;
    }


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

    @Override
    public String toString() {
        return "LatLong: " + this.getLatLong() + " Name: " + this.getName() + " Address" + this.getAddress() + " Traffic" + this.getTraffic().toString();
    }
}