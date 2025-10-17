package com.example.ybl.model;
public class Bus {
    private String busId;
    private String route;
    private String departureTime;
    private String status;
    private String passengerCount;
    private String driverName;
    private String conductorName;
    private boolean isActive;

    public Bus(String busId, String route, String departureTime, String status,
               String passengerCount, String driverName, String conductorName, boolean isActive) {
        this.busId = busId;
        this.route = route;
        this.departureTime = departureTime;
        this.status = status;
        this.passengerCount = passengerCount;
        this.driverName = driverName;
        this.conductorName = conductorName;
        this.isActive = isActive;
    }

    // Getters and setters
    public String getBusId() { return busId; }
    public String getRoute() { return route; }
    public String getDepartureTime() { return departureTime; }
    public String getStatus() { return status; }
    public String getPassengerCount() { return passengerCount; }
    public String getDriverName() { return driverName; }
    public String getConductorName() { return conductorName; }
    public boolean isActive() { return isActive; }
}