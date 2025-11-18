package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class TodayScheduleResponse {
    @SerializedName("schedule")
    private Schedule schedule;

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
