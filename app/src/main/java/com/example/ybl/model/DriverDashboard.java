package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class DriverDashboard {


    @SerializedName("current_trip")
    private int currentTrip;
    @SerializedName("today_trips")
    private int todayTrip;
    @SerializedName("assigned_bus")
    private AssignedBus assignBus;

    public AssignedBus getAssignBus() {
        return assignBus;
    }

    public void setAssignBus(AssignedBus assignBus) {
        this.assignBus = assignBus;
    }

    public int getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(int currentTrip) {
        this.currentTrip = currentTrip;
    }

    public int getTodayTrip() {
        return todayTrip;
    }

    public void setTodayTrip(int todayTrip) {
        this.todayTrip = todayTrip;
    }
}
