package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class CreateTripResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("trip_id")
    private int tripId;

    public String getMessage() {
        return message;
    }

    public int getTripId() {
        return tripId;
    }
}
