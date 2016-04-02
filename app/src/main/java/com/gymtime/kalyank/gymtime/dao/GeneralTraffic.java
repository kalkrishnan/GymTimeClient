package com.gymtime.kalyank.gymtime.dao;

public class GeneralTraffic{

    private double trafficStrength;

    public GeneralTraffic(double trafficStrength) {

        this.trafficStrength = trafficStrength;
    }
    public double getHowHeavyTrafficIs() {
        return this.trafficStrength;
    }

    @Override
    public String toString() {
        return "Traffic: "+this.getHowHeavyTrafficIs();
    }
}