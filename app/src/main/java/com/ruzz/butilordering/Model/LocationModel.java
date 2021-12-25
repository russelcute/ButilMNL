package com.ruzz.butilordering.Model;

public class LocationModel {
    private String address;
    private double latitude, longitude;

    public LocationModel(String address, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
