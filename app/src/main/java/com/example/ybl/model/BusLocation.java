package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;


public class BusLocation {
    @SerializedName("bus")
    private Bus bus;

    @SerializedName("trip")
    private Trip trip;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("speed")
    private Double speed;

    @SerializedName("heading")
    private Double heading;

    @SerializedName("recorded_at")
    private String recordedAt;

    // Getters and Setters
    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }

    public Double getHeading() { return heading; }
    public void setHeading(Double heading) { this.heading = heading; }

    public String getRecordedAt() { return recordedAt; }
    public void setRecordedAt(String recordedAt) { this.recordedAt = recordedAt; }
}