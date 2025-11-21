package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class ConductorDashboard {

    @SerializedName("current_trip")
    private CurrentTrip currentTrip;

    @SerializedName("today_stats")
    private TodayStats todayStats;

    public CurrentTrip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(CurrentTrip currentTrip) {
        this.currentTrip = currentTrip;
    }

    public TodayStats getTodayStats() {
        return todayStats;
    }

    public void setTodayStats(TodayStats todayStats) {
        this.todayStats = todayStats;
    }

    public static class CurrentTrip {
        @SerializedName("id")
        private int id;

        @SerializedName("trip_date")
        private String tripDate;

        @SerializedName("status")
        private String status;

        @SerializedName("passenger_count")
        private int passengerCount;

        @SerializedName("bus")
        private SimpleBus bus;

        @SerializedName("route")
        private Route route;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTripDate() {
            return tripDate;
        }

        public void setTripDate(String tripDate) {
            this.tripDate = tripDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPassengerCount() {
            return passengerCount;
        }

        public void setPassengerCount(int passengerCount) {
            this.passengerCount = passengerCount;
        }

        public SimpleBus getBus() {
            return bus;
        }

        public void setBus(SimpleBus bus) {
            this.bus = bus;
        }

        public Route getRoute() {
            return route;
        }

        public void setRoute(Route route) {
            this.route = route;
        }
    }

    public static class SimpleBus {
        @SerializedName("number")
        private String number;

        @SerializedName("plate")
        private String plate;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }
    }

    public static class TodayStats {
        @SerializedName("trips_count")
        private int tripsCount;

        public int getTripsCount() {
            return tripsCount;
        }

        public void setTripsCount(int tripsCount) {
            this.tripsCount = tripsCount;
        }
    }
}
