package com.example.ybl.repository;

import android.content.Context;

import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.BaseResponse;
import com.example.ybl.model.CompleteTripRequest;
import com.example.ybl.model.CreateTripRequest;
import com.example.ybl.model.CreateTripResponse;
import com.example.ybl.model.DriverDashboard;
import com.example.ybl.model.DropPoint;
import com.example.ybl.model.ReportIssueRequest;
import com.example.ybl.model.Schedule;
import com.example.ybl.model.StartTripRequest;
import com.example.ybl.model.TodayScheduleResponse;
import com.example.ybl.model.Trip;
import com.example.ybl.model.UpdateLocationRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRepository extends BaseRepository {

    public static final String TAG = DriverRepository.class.getSimpleName();

    public DriverRepository(Context context) {
        super(context);
    }

    public void getDashboard(ApiCallback<DriverDashboard> callback) {
        executeCall(apiService.getDriverDashboard(), callback);
    }

    public void getTodaySchedule(ApiCallback<TodayScheduleResponse> callback) {
        executeCall(apiService.getTodaySchedule(), callback);
    }

    public void createTrip(CreateTripRequest createTripRequest, ApiCallback<CreateTripResponse> callback) {
        executeCall(apiService.createTrip(createTripRequest), callback);
    }

    public void getCurrentTrip(ApiCallback<Trip> callback) {
        executeCall(apiService.getDriverCurrentTrip(), callback);
    }

    public void startTrip(StartTripRequest request, ApiCallback<Trip> callback) {
        executeCall(apiService.startTrip(request), callback);
    }

    public void completeTrip(CompleteTripRequest request, ApiCallback<Trip> callback) {
        executeCall(apiService.completeTrip(request), callback);
    }

    public void updateTripLocation(UpdateLocationRequest request, ApiCallback<Void> callback) {
        executeCall(apiService.updateTripLocation(request), callback);
    }

    public void getDropPoints(ApiCallback<List<DropPoint>> callback) {
        executeCall(apiService.getDriverDropPoints(), callback);
    }

    public void confirmDropPoint(int dropPointId, ApiCallback<DropPoint> callback) {
        executeCall(apiService.confirmDropPoint(dropPointId), callback);
    }

    public void reportIssue(ReportIssueRequest request, ApiCallback<Void> callback) {
        executeCall(apiService.reportIssue(request), callback);
    }
}
