package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class DropPointRequest {

    @SerializedName("trip_id")
    private int tripId;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    public DropPointRequest() {
    }

    public DropPointRequest(int tripId, String address, double latitude, double longitude) {
        this.tripId = tripId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
