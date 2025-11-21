package com.example.ybl.fragments.driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ybl.R;
import com.example.ybl.adapter.ScheduleAdapter;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.CreateTripRequest;
import com.example.ybl.model.CreateTripResponse;
import com.example.ybl.model.Schedule;
import com.example.ybl.model.StartTripRequest;
import com.example.ybl.model.TodayScheduleResponse;
import com.example.ybl.model.Trip;
import com.example.ybl.repository.DriverRepository;
import com.example.ybl.view.ScheduleDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverScheduleFragment extends Fragment {
    private DriverRepository driverRepository;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private CardView emptyState;
    private TextView tvScheduleDate, tvTotalTrips, tvCompletedTrips, tvPendingTrips;
    private RecyclerView rvSchedule;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverRepository = new DriverRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_schedule, container, false);

        initViews(view);
        setupRecyclerView();
        setCurrentDate();
        fetchAndDisplaySchedule();

        return view;
    }

    private void initViews(View view) {
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        emptyState = view.findViewById(R.id.emptyState);
        tvScheduleDate = view.findViewById(R.id.tvScheduleDate);
        tvTotalTrips = view.findViewById(R.id.tvTotalTrips);
        tvCompletedTrips = view.findViewById(R.id.tvCompletedTrips);
        tvPendingTrips = view.findViewById(R.id.tvPendingTrips);
        rvSchedule = view.findViewById(R.id.rvSchedule);

        swipeRefresh.setOnRefreshListener(this::fetchAndDisplaySchedule);
    }

    private void setupRecyclerView() {
        scheduleAdapter = new ScheduleAdapter(getContext(), scheduleList, new ScheduleAdapter.ScheduleClickListener() {
            @Override
            public void onStartTripClick(Schedule schedule) {
                startTrip(schedule);
            }

            @Override
            public void onViewDetailsClick(Schedule schedule) {
                viewScheduleDetails(schedule);
            }
        });

        rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSchedule.setAdapter(scheduleAdapter);
    }

    private void setCurrentDate() {
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());
        tvScheduleDate.setText(currentDate);
    }

    private void fetchAndDisplaySchedule() {
        showLoading();

        driverRepository.getTodaySchedule(new ApiCallback<TodayScheduleResponse>() {
            @Override
            public void onSuccess(TodayScheduleResponse response) {
                hideLoading();
                if (response != null && response.getSchedule() != null && !response.getSchedule().isEmpty()) {
                    scheduleList.clear();
                    scheduleList.addAll(response.getSchedule());
                    updateUI();
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onError(String errorMessage) {
                hideLoading();
                Toast.makeText(getContext(), "Failed to load schedule: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI() {
        if (scheduleList.isEmpty()) {
            showEmptyState();
        } else {
            showScheduleList();
            updateSummaryCards();
        }
        scheduleAdapter.notifyDataSetChanged();
    }

    private void updateSummaryCards() {
        int totalTrips = scheduleList.size();
        int completedTrips = 0;
        int pendingTrips = 0;

        for (Schedule schedule : scheduleList) {
            if ("completed".equalsIgnoreCase(schedule.getStatus())) {
                completedTrips++;
            } else if ("pending".equalsIgnoreCase(schedule.getStatus())) {
                pendingTrips++;
            }
        }

        tvTotalTrips.setText(String.valueOf(totalTrips));
        tvCompletedTrips.setText(String.valueOf(completedTrips));
        tvPendingTrips.setText(String.valueOf(pendingTrips));
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
        rvSchedule.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);
    }

    private void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        rvSchedule.setVisibility(View.GONE);

        // Reset stats to 0 when empty
        tvTotalTrips.setText("0");
        tvCompletedTrips.setText("0");
        tvPendingTrips.setText("0");
    }

    private void showScheduleList() {
        rvSchedule.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
    }

    private void startTrip(Schedule schedule) {
        if (schedule != null) {
            createTripDialog(schedule);
        }

    }


    private void createTripDialog(Schedule schedule) {
        new AlertDialog.Builder(getContext())
                .setTitle("Create Trip")
                .setMessage("Are you sure you want to create this trip?")
                .setPositiveButton("Create", (dialog, which) -> {
                    createTrip(schedule);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void createTrip(Schedule schedule) {
        CreateTripRequest request = new CreateTripRequest(schedule.getId(), schedule.getBusId());
        driverRepository.createTrip(request, new ApiCallback<CreateTripResponse>() {
            @Override
            public void onSuccess(CreateTripResponse response) {
                Toast.makeText(getContext(), "Trip started successfully", Toast.LENGTH_SHORT).show();
                fetchAndDisplaySchedule();
                Log.d("DriverScheduleFragment", "Trip started successfully");

                // Start location service
                Intent serviceIntent = new Intent(getContext(), com.example.ybl.service.LocationUpdateService.class);
                serviceIntent.putExtra("trip_id", response.getTripId());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    getContext().startForegroundService(serviceIntent);
                } else {
                    getContext().startService(serviceIntent);
                }

                // Open Map
                Intent mapIntent = new Intent(getContext(), com.example.ybl.view.DriverNavigationActivity.class);
                mapIntent.putExtra(com.example.ybl.view.DriverNavigationActivity.EXTRA_SCHEDULE, schedule);
                mapIntent.putExtra(com.example.ybl.view.DriverNavigationActivity.EXTRA_TRIP_ID, response.getTripId());
                startActivity(mapIntent);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Failed to start trip: " + errorMessage, Toast.LENGTH_LONG).show();
                Log.e("DriverScheduleFragment", "Failed to start trip: " + errorMessage);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("DriverScheduleFragment", "Network error: " + t.getMessage());
            }
        });
    }

    private void viewScheduleDetails(Schedule schedule) {
        Intent intent = new Intent(getContext(), ScheduleDetailsActivity.class);
        intent.putExtra(ScheduleDetailsActivity.EXTRA_SCHEDULE, schedule);
        startActivity(intent);
    }
}