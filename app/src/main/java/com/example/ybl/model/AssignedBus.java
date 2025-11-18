package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class AssignedBus {

    @SerializedName("id")
    private int id;
    @SerializedName("bus_number")
    private String busNumber;
    @SerializedName("license_plate")
    private String licensePlate;

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
