package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class DriverDashboard {

    @SerializedName("current_trip")
    private Trip currentTrip;

    @SerializedName("today_trips")
    private int todayTrips;

    @SerializedName("assigned_bus")
    private AssignedBus assignedBus;

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(Trip currentTrip) {
        this.currentTrip = currentTrip;
    }

    public int getTodayTrips() {
        return todayTrips;
    }

    public void setTodayTrips(int todayTrips) {
        this.todayTrips = todayTrips;
    }

    public AssignedBus getAssignedBus() {
        return assignedBus;
    }

    public void setAssignedBus(AssignedBus assignedBus) {
        this.assignedBus = assignedBus;
    }

    public static class AssignedBus {
        @SerializedName("id")
        private int id;

        @SerializedName("bus_number")
        private String busNumber;

        @SerializedName("license_plate")
        private String licensePlate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBusNumber() {
            return busNumber;
        }

        public void setBusNumber(String busNumber) {
            this.busNumber = busNumber;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }
    }
}
