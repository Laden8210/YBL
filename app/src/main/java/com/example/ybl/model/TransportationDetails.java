package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TransportationDetails {
    @SerializedName("trip")
    private Trip trip;
    
    @SerializedName("bus_details")
    private BusDetails busDetails;
    
    @SerializedName("assigned_staff")
    private AssignedStaff assignedStaff;
    
    @SerializedName("passenger_info")
    private PassengerInfo passengerInfo;
    
   @SerializedName("route_details")
    private RouteDetails routeDetails;
    
    @SerializedName("recent_locations")
    private List<TripLocation> recentLocations;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public BusDetails getBusDetails() {
        return busDetails;
    }

    public void setBusDetails(BusDetails busDetails) {
        this.busDetails = busDetails;
    }

    public AssignedStaff getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(AssignedStaff assignedStaff) {
        this.assignedStaff = assignedStaff;
    }

    public PassengerInfo getPassengerInfo() {
        return passengerInfo;
    }

    public void setPassengerInfo(PassengerInfo passengerInfo) {
        this.passengerInfo = passengerInfo;
    }

    public RouteDetails getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(RouteDetails routeDetails) {
        this.routeDetails = routeDetails;
    }

    public List<TripLocation> getRecentLocations() {
        return recentLocations;
    }

    public void setRecentLocations(List<TripLocation> recentLocations) {
        this.recentLocations = recentLocations;
    }
}
