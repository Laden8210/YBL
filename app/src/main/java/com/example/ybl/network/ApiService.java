package com.example.ybl.network;

import com.example.ybl.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface ApiService {

    @POST("login")
    Call<BaseResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<BaseResponse<LoginResponse>> register(@Body RegisterRequest registerRequest);

    @GET("profile")
    Call<BaseResponse<User>> getProfile();

    @PUT("profile")
    Call<BaseResponse<User>> updateProfile(@Body UpdateProfileRequest request);

    @POST("logout")
    Call<BaseResponse<Void>> logout();

    // Supervisor Endpoints
    @GET("supervisor/dashboard")
    Call<BaseResponse<SupervisorDashboard>> getSupervisorDashboard();

    @GET("supervisor/buses")
    Call<BaseResponse<List<Bus>>> getBuses();

    @GET("supervisor/schedules")
    Call<BaseResponse<List<Schedule>>> getSchedules();

    @GET("supervisor/bus-locations")
    Call<BaseResponse<List<BusLocation>>> getBusLocations();

    @GET("supervisor/transportation-details/{tripId}")
    Call<BaseResponse<TransportationDetails>> getTransportationDetails(@Path("tripId") int tripId);

    @POST("supervisor/confirm-transportation/{tripId}")
    Call<BaseResponse<ConfirmationResponse>> confirmTransportation(
            @Path("tripId") int tripId,
            @Body ConfirmationRequest request
    );

    @GET("supervisor/trips")
    Call<BaseResponse<List<Trip>>> getTrips(@QueryMap Map<String, String> filters);

    // Driver Endpoints
    @GET("driver/dashboard")
    Call<BaseResponse<DriverDashboard>> getDriverDashboard();

    @GET("driver/current-trip")
    Call<BaseResponse<Trip>> getDriverCurrentTrip();

    @GET("driver/today-schedule")
    Call<BaseResponse<TodayScheduleResponse>> getTodaySchedule();

    @POST("driver/start-trip")
    Call<BaseResponse<Trip>> startTrip(@Body StartTripRequest request);

    @POST("driver/update-trip-location")
    Call<BaseResponse<Void>> updateTripLocation(@Body UpdateLocationRequest request);

    @POST("driver/complete-trip")
    Call<BaseResponse<Trip>> completeTrip(@Body CompleteTripRequest request);

    @GET("driver/drop-points")
    Call<BaseResponse<List<DropPoint>>> getDriverDropPoints();

    @POST("driver/confirm-drop-point/{dropPointId}")
    Call<BaseResponse<DropPoint>> confirmDropPoint(@Path("dropPointId") int dropPointId);

    @POST("driver/issues")
    Call<BaseResponse<Void>> reportIssue(@Body ReportIssueRequest request);

    @POST("driver/create-trip")
    Call<BaseResponse<CreateTripResponse>> createTrip(@Body CreateTripRequest createTripRequest);

    // Conductor Endpoints
    @GET("conductor/dashboard")
    Call<BaseResponse<ConductorDashboard>> getConductorDashboard();

    @GET("conductor/current-trip")
    Call<BaseResponse<Trip>> getConductorCurrentTrip();

    @POST("conductor/update-passenger-count")
    Call<BaseResponse<Void>> updatePassengerCount(@Body PassengerCountRequest request);

    @GET("conductor/drop-points")
    Call<BaseResponse<List<DropPoint>>> getConductorDropPoints();

    @POST("conductor/add-drop-point")
    Call<BaseResponse<DropPoint>> addDropPoint(@Body AddDropPointRequest request);

    @POST("conductor/forward-drop-point/{dropPointId}")
    Call<BaseResponse<DropPoint>> forwardDropPoint(@Path("dropPointId") int dropPointId);

    // Passenger Endpoints
    @GET("passenger/routes")
    Call<BaseResponse<List<Route>>> getRoutes();

    @GET("passenger/schedules")
    Call<BaseResponse<List<Schedule>>> getPassengerSchedules();

    @GET("passenger/bus-locations")
    Call<BaseResponse<List<BusLocation>>> getPassengerBusLocations();

    @POST("passenger/request-drop-point")
    Call<BaseResponse<DropPoint>> requestDropPoint(@Body DropPointRequest request);

    @GET("passenger/my-requests")
    Call<BaseResponse<List<DropPoint>>> getMyRequests();
}