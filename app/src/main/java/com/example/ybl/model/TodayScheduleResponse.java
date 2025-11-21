package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayScheduleResponse {
    @SerializedName("schedule")
    private List<Schedule> schedule;

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
