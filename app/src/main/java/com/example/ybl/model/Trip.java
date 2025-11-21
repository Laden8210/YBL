package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trip implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("trip_date")
    private String tripDate;

    @SerializedName("status")
    private String status;

    @SerializedName("route")
    private Route route;

    @SerializedName("bus")
    private Bus bus;

    @SerializedName("passenger_count")
    private int passengerCount;

    @SerializedName("actual_departure_time")
    private String actualDepartureTime;

    @SerializedName("actual_arrival_time")
    private String actualArrivalTime;

    // Getters and Setters
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(String actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }

    public String getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(String actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
}
