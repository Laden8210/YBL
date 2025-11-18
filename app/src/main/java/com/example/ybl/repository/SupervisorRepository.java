package com.example.ybl.repository;


import android.content.Context;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.*;
import java.util.List;
import java.util.Map;

public class SupervisorRepository extends BaseRepository {

    public SupervisorRepository(Context context) {
        super(context);
    }

    public void getDashboard(ApiCallback<SupervisorDashboard> callback) {
        executeCall(apiService.getSupervisorDashboard(), callback);
    }

    public void getBuses(ApiCallback<List<Bus>> callback) {
        executeCall(apiService.getBuses(), callback);
    }

    public void getSchedules(ApiCallback<List<Schedule>> callback) {
        executeCall(apiService.getSchedules(), callback);
    }

    public void getBusLocations(ApiCallback<List<BusLocation>> callback) {
        executeCall(apiService.getBusLocations(), callback);
    }

    public void getTransportationDetails(int tripId, ApiCallback<TransportationDetails> callback) {
        executeCall(apiService.getTransportationDetails(tripId), callback);
    }

    public void confirmTransportation(int tripId, ConfirmationRequest request,
                                      ApiCallback<ConfirmationResponse> callback) {
        executeCall(apiService.confirmTransportation(tripId, request), callback);
    }

    public void getTrips(Map<String, String> filters, ApiCallback<List<Trip>> callback) {
        executeCall(apiService.getTrips(filters), callback);
    }
}