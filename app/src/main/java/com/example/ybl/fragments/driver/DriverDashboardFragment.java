package com.example.ybl.fragments.driver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ybl.R;
import com.example.ybl.fragments.ReportIssueDialog;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.CompleteTripRequest;
import com.example.ybl.model.DriverDashboard;
import com.example.ybl.model.StartTripRequest;
import com.example.ybl.model.Trip;
import com.example.ybl.repository.DriverRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.card.MaterialCardView;

public class DriverDashboardFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private SwipeRefreshLayout swipeRefresh;
    private MaterialCardView currentTripCard, noTripCard, assignedBusCard;
    private TextView textViewRouteName, textViewRouteDetails, textViewBusInfo;
    private TextView textViewPassengerCount, textViewDepartureTime, textViewTripStatus;
    private TextView textViewBusNumber, textViewLicensePlate, textViewTripsCount;
    private Button buttonStartTrip, buttonCompleteTrip, buttonReportIssue;
    private ProgressBar progressBar;

    private DriverRepository repository;
    private FusedLocationProviderClient fusedLocationClient;
    private Handler handler = new Handler();
    private Runnable refreshRunnable;
    private Trip currentTrip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_dashboard, container, false);

        initViews(view);
        setupSwipeRefresh();
        setupButtons();

        repository = new DriverRepository(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        loadDashboard();
        startAutoRefresh();

        return view;
    }

    private void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        currentTripCard = view.findViewById(R.id.currentTripCard);
        noTripCard = view.findViewById(R.id.noTripCard);
        assignedBusCard = view.findViewById(R.id.assignedBusCard);
        textViewRouteName = view.findViewById(R.id.textViewRouteName);
        textViewRouteDetails = view.findViewById(R.id.textViewRouteDetails);
        textViewBusInfo = view.findViewById(R.id.textViewBusInfo);
        textViewPassengerCount = view.findViewById(R.id.textViewPassengerCount);
        textViewDepartureTime = view.findViewById(R.id.textViewDepartureTime);
        textViewTripStatus = view.findViewById(R.id.textViewTripStatus);
        textViewBusNumber = view.findViewById(R.id.textViewBusNumber);
        textViewLicensePlate = view.findViewById(R.id.textViewLicensePlate);
        textViewTripsCount = view.findViewById(R.id.textViewTripsCount);
        buttonStartTrip = view.findViewById(R.id.buttonStartTrip);
        buttonCompleteTrip = view.findViewById(R.id.buttonCompleteTrip);

        buttonReportIssue = view.findViewById(R.id.buttonReportIssue);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(this::loadDashboard);
    }

    private void setupButtons() {
        buttonStartTrip.setOnClickListener(v -> handleStartTrip());
        buttonCompleteTrip.setOnClickListener(v -> handleCompleteTrip());
        buttonReportIssue.setOnClickListener(v -> showReportIssueDialog());
    }

    private void loadDashboard() {
        showLoading();
        repository.getDashboard(new ApiCallback<DriverDashboard>() {
            @Override
            public void onSuccess(DriverDashboard data) {
                hideLoading();
                displayDashboard(data);
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                Toast.makeText(requireContext(), "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDashboard(DriverDashboard dashboard) {
        currentTrip = dashboard.getCurrentTrip();

        // Display current trip
        if (currentTrip != null) {
            currentTripCard.setVisibility(View.VISIBLE);
            noTripCard.setVisibility(View.GONE);
           
            if (currentTrip.getRoute() != null) {
                textViewRouteName.setText(currentTrip.getRoute().getRouteName());
                String routeDetails = currentTrip.getRoute().getStartPoint() + " → " +
                        currentTrip.getRoute().getEndPoint();
                textViewRouteDetails.setText(routeDetails);
            }

            if (currentTrip.getBus() != null) {
                String busInfo = "Bus: " + currentTrip.getBus().getBusNumber();
                textViewBusInfo.setText(busInfo);
            }

            textViewPassengerCount.setText("Passengers: " + currentTrip.getPassengerCount());

            // Show departure time if available
            if (currentTrip.getActualDepartureTime() != null) {
                textViewDepartureTime.setVisibility(View.VISIBLE);
                textViewDepartureTime.setText("Departed: " + currentTrip.getActualDepartureTime());
            } else {
                textViewDepartureTime.setVisibility(View.GONE);
            }

            // Update status and buttons based on trip status
            String status = currentTrip.getStatus();
            textViewTripStatus.setText(status != null ? status.toUpperCase() : "UNKNOWN");

            if ("scheduled".equalsIgnoreCase(status)) {
                buttonStartTrip.setVisibility(View.VISIBLE);
                buttonCompleteTrip.setVisibility(View.GONE);
            } else if ("in_progress".equalsIgnoreCase(status) || "loading".equalsIgnoreCase(status)) {
                buttonStartTrip.setVisibility(View.VISIBLE);
                buttonCompleteTrip.setVisibility(View.VISIBLE);
            } else {
                buttonStartTrip.setVisibility(View.GONE);
                buttonCompleteTrip.setVisibility(View.GONE);
            }
        } else {
            currentTripCard.setVisibility(View.GONE);
            noTripCard.setVisibility(View.VISIBLE);
        }

        // Display assigned bus
        if (dashboard.getAssignedBus() != null) {
            assignedBusCard.setVisibility(View.VISIBLE);
            textViewBusNumber.setText(dashboard.getAssignedBus().getBusNumber());
            textViewLicensePlate.setText(dashboard.getAssignedBus().getLicensePlate());
        } else {
            assignedBusCard.setVisibility(View.GONE);
        }

        // Display today's stats
        textViewTripsCount.setText("Trips Completed: " + dashboard.getTodayTrips());
    }

    private void handleStartTrip() {
        if (checkLocationPermissions()) {
            getCurrentLocationAndStartTrip();
        } else {
            requestLocationPermissions();
        }
    }

    private void handleCompleteTrip() {
        if (checkLocationPermissions()) {
            getCurrentLocationAndCompleteTrip();
        } else {
            requestLocationPermissions();
        }
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getCurrentLocationAndStartTrip() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                startTrip(currentTrip.getId() ,location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocationAndCompleteTrip() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                completeTrip(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startTrip(int  tripId, double latitude, double longitude) {
        showLoading();
        StartTripRequest request = new StartTripRequest(tripId,latitude, longitude);

        repository.startTrip(request, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip data) {
                hideLoading();
                Toast.makeText(requireContext(), "Trip started successfully", Toast.LENGTH_SHORT).show();
                
                // Start location service
                Intent serviceIntent = new Intent(requireContext(), com.example.ybl.service.LocationUpdateService.class);
                serviceIntent.putExtra("trip_id", data.getId());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(serviceIntent);
                } else {
                    requireContext().startService(serviceIntent);
                }
                
                loadDashboard();

                // Open Map
                if (data != null && data.getRoute() != null) {
                    com.example.ybl.model.Schedule schedule = new com.example.ybl.model.Schedule();
                    schedule.setRoute(data.getRoute());
                    schedule.setBus(data.getBus());
                    
                    Intent mapIntent = new Intent(requireContext(), com.example.ybl.view.DriverNavigationActivity.class);
                    mapIntent.putExtra(com.example.ybl.view.DriverNavigationActivity.EXTRA_SCHEDULE, schedule);
                    mapIntent.putExtra(com.example.ybl.view.DriverNavigationActivity.EXTRA_TRIP_ID, data.getId());
                    startActivity(mapIntent);
                }
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                Toast.makeText(requireContext(), "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void completeTrip(double latitude, double longitude) {
        showLoading();
        CompleteTripRequest request = new CompleteTripRequest(
                currentTrip.getId(),
                latitude, longitude);

        repository.completeTrip(request, new ApiCallback<Trip>() {
            @Override
            public void onSuccess(Trip data) {
                hideLoading();
                Toast.makeText(requireContext(), "Trip completed successfully", Toast.LENGTH_SHORT).show();
                
                // Stop location service
                Intent serviceIntent = new Intent(requireContext(), com.example.ybl.service.LocationUpdateService.class);
                requireContext().stopService(serviceIntent);
                
                loadDashboard();
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                Toast.makeText(requireContext(), "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showReportIssueDialog() {
        ReportIssueDialog dialog = new ReportIssueDialog();
        dialog.show(getParentFragmentManager(), "ReportIssueDialog");
    }

    private void startAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                loadDashboard();
                handler.postDelayed(this, 30000); // 30 seconds
            }
        };
        handler.postDelayed(refreshRunnable, 30000);
    }

    private void showLoading() {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshRunnable != null) {
            handler.removeCallbacks(refreshRunnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshDashboard() {
        loadDashboard();
    }
}