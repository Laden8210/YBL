package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bus implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("bus_number")
    private String busNumber;

    @SerializedName("license_plate")
    private String licensePlate;

    @SerializedName("capacity")
    private int capacity;

    @SerializedName("model")
    private String model;

    @SerializedName("color")
    private String color;

    @SerializedName("status")
    private String status;

    @SerializedName("features")
    private String features;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("current_assignment")
    private AssignedStaff currentAssignment;

    @SerializedName("latest_location")
    private TripLocation latestLocation;

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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AssignedStaff getCurrentAssignment() {
        return currentAssignment;
    }

    public void setCurrentAssignment(AssignedStaff currentAssignment) {
        this.currentAssignment = currentAssignment;
    }

    public TripLocation getLatestLocation() {
        return latestLocation;
    }

    public void setLatestLocation(TripLocation latestLocation) {
        this.latestLocation = latestLocation;
    }
}
