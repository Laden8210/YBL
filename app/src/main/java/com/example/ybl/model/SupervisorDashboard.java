package com.example.ybl.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SupervisorDashboard {
    @SerializedName("active_trips_count")
    private int activeTripsCount;

    @SerializedName("today_schedules_count")
    private int todaySchedulesCount;

    @SerializedName("active_buses_count")
    private int activeBusesCount;

    @SerializedName("active_trips")
    private List<Trip> activeTrips;

    @SerializedName("today_schedules")
    private List<Schedule> todaySchedules;

    // Getters and Setters
    public int getActiveTripsCount() { return activeTripsCount; }
    public void setActiveTripsCount(int activeTripsCount) { this.activeTripsCount = activeTripsCount; }

    public int getTodaySchedulesCount() { return todaySchedulesCount; }
    public void setTodaySchedulesCount(int todaySchedulesCount) { this.todaySchedulesCount = todaySchedulesCount; }

    public int getActiveBusesCount() { return activeBusesCount; }
    public void setActiveBusesCount(int activeBusesCount) { this.activeBusesCount = activeBusesCount; }

    public List<Trip> getActiveTrips() { return activeTrips; }
    public void setActiveTrips(List<Trip> activeTrips) { this.activeTrips = activeTrips; }

    public List<Schedule> getTodaySchedules() { return todaySchedules; }
    public void setTodaySchedules(List<Schedule> todaySchedules) { this.todaySchedules = todaySchedules; }
}