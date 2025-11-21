package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class AddDropPointRequest {
    @SerializedName("route_id")
    private int route_id;

    @SerializedName("address")
    private String address;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("passenger_name")
    private String passengerName;

    public AddDropPointRequest() {
    }

    public AddDropPointRequest(int route_id, String address, double latitude, double longitude) {
        this.route_id = route_id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddDropPointRequest(int route_id, String address, double latitude, double longitude, String passengerName) {
        this.route_id = route_id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.passengerName = passengerName;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
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

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}
