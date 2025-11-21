package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class CreateTripRequest {
    @SerializedName("schedule_id")
    private final int scheduleId;

    @SerializedName("bus_id")
    private final int busId;

    public CreateTripRequest(int scheduleId, int busId) {
        this.scheduleId = scheduleId;
        this.busId = busId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getBusId() {
        return busId;
    }
}
