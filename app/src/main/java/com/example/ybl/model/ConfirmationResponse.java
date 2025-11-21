package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class ConfirmationResponse {
    
    @SerializedName("trip_id")
    private int tripId;
    
    @SerializedName("confirmed_by")
    private String confirmedBy;
    
    @SerializedName("confirmed_at")
    private String confirmedAt;

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(String confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public String getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(String confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
