package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class PassengerCountRequest {

    @SerializedName("passenger_count")
    private int passengerCount;

    public PassengerCountRequest() {
    }

    public PassengerCountRequest(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }
}
