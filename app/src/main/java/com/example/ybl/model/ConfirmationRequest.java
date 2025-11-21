package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class ConfirmationRequest {
    
    @SerializedName("confirmed_details")
    private Object confirmedDetails;
    
    @SerializedName("notes")
    private String notes;

    public ConfirmationRequest(Object confirmedDetails, String notes) {
        this.confirmedDetails = confirmedDetails;
        this.notes = notes;
    }

    public ConfirmationRequest(Object confirmedDetails) {
        this.confirmedDetails = confirmedDetails;
    }

    public Object getConfirmedDetails() {
        return confirmedDetails;
    }

    public void setConfirmedDetails(Object confirmedDetails) {
        this.confirmedDetails = confirmedDetails;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
