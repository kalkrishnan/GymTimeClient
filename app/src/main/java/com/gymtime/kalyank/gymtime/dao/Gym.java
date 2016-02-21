package com.gymtime.kalyank.gymtime.dao;

import java.util.ArrayList;
import java.util.List;

public class Gym {

    private final String latLong;
    private final String name;
    private final String address;
    private final List<Traffic> traffic;

    public Gym(String latLong, String name, String address, List<Traffic> traffic) {

        this.latLong = latLong;
        this.name = name;
        this.address = address;
        this.traffic = new ArrayList<Traffic>();
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

    public List<Traffic> getTraffic() {
        return traffic;
    }

}