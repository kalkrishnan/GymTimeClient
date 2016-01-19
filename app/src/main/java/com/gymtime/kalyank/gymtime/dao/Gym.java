package com.gymtime.kalyank.gymtime.dao;

public class Gym {

    private final String id;
    private final String name;
    private final String address;
    private final Traffic traffic;

    public Gym(String id, String name, String address, Traffic traffic) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.traffic = traffic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Traffic getTraffic() {
        return traffic;
    }

}