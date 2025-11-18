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

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ybl.R;
import com.example.ybl.adapter.ScheduleAdapter;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.Schedule;
import com.example.ybl.repository.DriverRepository;
import com.example.ybl.view.ScheduleDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverScheduleFragment extends Fragment {
    private DriverRepository driverRepository;
    private ProgressBar progressBar;
    private CardView emptyState, errorState;
    private TextView tvScheduleDate, tvTotalTrips, tvCompletedTrips, tvPendingTrips, tvError;
    private Button btnRetry;
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
        progressBar = view.findViewById(R.id.progressBar);
        emptyState = view.findViewById(R.id.emptyState);
        errorState = view.findViewById(R.id.errorState);
        tvScheduleDate = view.findViewById(R.id.tvScheduleDate);
        tvTotalTrips = view.findViewById(R.id.tvTotalTrips);
        tvCompletedTrips = view.findViewById(R.id.tvCompletedTrips);
        tvPendingTrips = view.findViewById(R.id.tvPendingTrips);
        tvError = view.findViewById(R.id.tvError);
        btnRetry = view.findViewById(R.id.btnRetry);
        rvSchedule = view.findViewById(R.id.rvSchedule);

        btnRetry.setOnClickListener(v -> fetchAndDisplaySchedule());
    }

    private void setupRecyclerView() {
        scheduleAdapter = new ScheduleAdapter(scheduleList, new ScheduleAdapter.ScheduleClickListener() {
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

        driverRepository.getTodaySchedule(new ApiCallback<Schedule>() {
            @Override
            public void onSuccess(Schedule response) {
                hideLoading();
                if (response != null) {
                    scheduleList.clear();
                    scheduleList.add(response);
                    updateUI();
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onError(String errorMessage) {
                hideLoading();
                showError("Failed to load schedule: " + errorMessage);
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showError("Network error: " + t.getMessage());
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
        errorState.setVisibility(View.GONE);
        rvSchedule.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        emptyState.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
        rvSchedule.setVisibility(View.GONE);

        // Reset stats to 0 when empty
        tvTotalTrips.setText("0");
        tvCompletedTrips.setText("0");
        tvPendingTrips.setText("0");
    }

    private void showError(String errorMessage) {
        Log.d("DriverScheduleFragment", "showError: " + errorMessage);
        errorState.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
        rvSchedule.setVisibility(View.GONE);
        tvError.setText(errorMessage);

        // Reset stats to 0 on error
        tvTotalTrips.setText("0");
        tvCompletedTrips.setText("0");
        tvPendingTrips.setText("0");
    }

    private void showScheduleList() {
        rvSchedule.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
        errorState.setVisibility(View.GONE);
    }

    private void startTrip(Schedule schedule) {
        Toast.makeText(getContext(), "Starting trip: " + schedule.getRoute().getRouteName(), Toast.LENGTH_SHORT).show();
        // Implement start trip logic
    }

    private void viewScheduleDetails(Schedule schedule) {
        Intent intent = new Intent(getContext(), ScheduleDetailsActivity.class);
        intent.putExtra(ScheduleDetailsActivity.EXTRA_SCHEDULE, schedule);
        startActivity(intent);
    }
}