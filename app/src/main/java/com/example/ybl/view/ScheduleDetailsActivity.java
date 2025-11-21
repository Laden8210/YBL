package com.example.ybl.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ybl.R;
import com.example.ybl.fragments.ReportIssueDialog;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.CreateTripRequest;
import com.example.ybl.model.CreateTripResponse;
import com.example.ybl.model.Schedule;
import com.example.ybl.repository.DriverRepository;
import com.example.ybl.util.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;

public class ScheduleDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_SCHEDULE = "extra_schedule";

    private MaterialToolbar toolbar;
    private TextView tvRouteName, tvStatus, tvDepartureTime, tvArrivalTime, tvDuration;
    private TextView tvBusNumber, tvLicensePlate, tvBusModel, tvBusCapacity;
    private TextView tvStartPoint, tvEndPoint, tvDistance, tvEstimatedDuration;
    private TextView tvDayOfWeek, tvRecurring, tvEffectiveDate, tvEndDate;
    private Button btnStartTrip, btnReportIssue, btnNavigate;
    private DriverRepository driverRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        initViews();
        setupToolbar();
        loadScheduleData();
        setupClickListeners();

        driverRepository = new DriverRepository(this);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvRouteName = findViewById(R.id.tvRouteName);
        tvStatus = findViewById(R.id.tvStatus);
        tvDepartureTime = findViewById(R.id.tvDepartureTime);
        tvArrivalTime = findViewById(R.id.tvArrivalTime);
        tvDuration = findViewById(R.id.tvDuration);
        tvBusNumber = findViewById(R.id.tvBusNumber);
        tvLicensePlate = findViewById(R.id.tvLicensePlate);
        tvBusModel = findViewById(R.id.tvBusModel);
        tvBusCapacity = findViewById(R.id.tvBusCapacity);
        tvStartPoint = findViewById(R.id.tvStartPoint);
        tvEndPoint = findViewById(R.id.tvEndPoint);
        tvDistance = findViewById(R.id.tvDistance);
        tvEstimatedDuration = findViewById(R.id.tvEstimatedDuration);
        tvDayOfWeek = findViewById(R.id.tvDayOfWeek);
        tvRecurring = findViewById(R.id.tvRecurring);
        tvEffectiveDate = findViewById(R.id.tvEffectiveDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btnStartTrip = findViewById(R.id.btnStartTrip);
        btnReportIssue = findViewById(R.id.btnReportIssue);
        btnNavigate = findViewById(R.id.btnNavigate);


        if(SessionManager.getInstance(this).getUserDetails().getRole().equalsIgnoreCase("supervisor")){
            btnStartTrip.setVisibility(View.GONE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadScheduleData() {
        Schedule schedule = (Schedule) getIntent().getSerializableExtra(EXTRA_SCHEDULE);
        if (schedule != null) {
            updateUI(schedule);
        } else {
            Toast.makeText(this, "Schedule data not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateUI(Schedule schedule) {
        // Set route information
        if (schedule.getRoute() != null) {
            tvRouteName.setText(schedule.getRoute().getRouteName());
            tvStartPoint.setText(schedule.getRoute().getStartPoint());
            tvEndPoint.setText(schedule.getRoute().getEndPoint());
            tvDistance.setText(schedule.getRoute().getDistance());
            tvEstimatedDuration.setText(formatDuration(schedule.getRoute().getEstimatedDuration()));
        }

        // Set bus information
        if (schedule.getBus() != null) {
            tvBusNumber.setText(schedule.getBus().getBusNumber());
            tvLicensePlate.setText(schedule.getBus().getLicensePlate());
            tvBusModel.setText(schedule.getBus().getModel());
            tvBusCapacity.setText(getString(R.string.capacity_format, schedule.getBus().getCapacity()));
        }

        // Set schedule times
        tvDepartureTime.setText(formatTime(schedule.getDepartureTime()));
        tvArrivalTime.setText(formatTime(schedule.getArrivalTime()));
        tvDuration.setText(calculateDuration(schedule.getDepartureTime(), schedule.getArrivalTime()));

        // Set schedule metadata
        tvDayOfWeek.setText(schedule.getDayOfWeek());
        tvRecurring.setText(schedule.isRecurring() ? "Yes" : "No");
        tvEffectiveDate.setText(formatDate(schedule.getEffectiveDate()));
        tvEndDate.setText(schedule.getEndDate() != null ? formatDate(schedule.getEndDate()) : "N/A");

        // Set status with appropriate styling
        setStatusUI(schedule.getStatus());
    }

    private void setStatusUI(String status) {
        int statusColor;
        int statusIcon;
        String statusText = status;

        switch (status.toLowerCase()) {
            case "completed":
                statusColor = R.color.success_green;
                statusIcon = R.drawable.ic_check_circle;
                statusText = "Completed";
                btnStartTrip.setVisibility(View.GONE);
                break;
            case "in_progress":
                statusColor = R.color.primary_color;
                statusIcon = R.drawable.ic_in_progress;
                statusText = "In Progress";
                btnStartTrip.setText("Continue Trip");
                break;
            case "cancelled":
                statusColor = R.color.error_red;
                statusIcon = R.drawable.ic_cancel;
                statusText = "Cancelled";
                btnStartTrip.setVisibility(View.GONE);
                break;
            default: // pending
                statusColor = R.color.warning_orange;
                statusIcon = R.drawable.ic_pending;
                statusText = "Pending";
                btnStartTrip.setText("Start Trip");
                break;
        }

        tvStatus.setText(statusText);
        tvStatus.setTextColor(ContextCompat.getColor(this, statusColor));
        tvStatus.setBackgroundResource(getStatusBackground(status));
    }

    private int getStatusBackground(String status) {
        switch (status.toLowerCase()) {
            case "completed":
                return R.drawable.bg_status_completed;
            case "in_progress":
                return R.drawable.bg_status_in_progress;
            case "cancelled":
                return R.drawable.bg_status_cancelled;
            default:
                return R.drawable.bg_status_pending;
        }
    }

    private void setupClickListeners() {
        btnStartTrip.setOnClickListener(v -> startTrip());
        btnReportIssue.setOnClickListener(v -> reportIssue());
        btnNavigate.setOnClickListener(v -> navigateToRoute());
    }

    private void startTrip() {
        Schedule schedule = (Schedule) getIntent().getSerializableExtra(EXTRA_SCHEDULE);
        if (schedule != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Start Trip")
                    .setMessage("Are you sure you want to start this trip?")
                    .setPositiveButton("Start", (dialog, which) -> {
                        createTrip(schedule);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void createTrip(Schedule schedule) {
        CreateTripRequest request = new CreateTripRequest(schedule.getId(), schedule.getBusId());
        driverRepository.createTrip(request, new ApiCallback<CreateTripResponse>() {
            @Override
            public void onSuccess(CreateTripResponse response) {
                runOnUiThread(() -> {
                    Toast.makeText(ScheduleDetailsActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    // Optionally, update the UI or navigate to another screen
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(ScheduleDetailsActivity.this, "Failed to create trip: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(ScheduleDetailsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void reportIssue() {
        ReportIssueDialog dialog = new ReportIssueDialog();
        dialog.show(getSupportFragmentManager(), "ReportIssueDialog");
    }

    private void navigateToRoute() {
        Schedule schedule = (Schedule) getIntent().getSerializableExtra(EXTRA_SCHEDULE);
        if (schedule != null && schedule.getRoute() != null) {
            Intent intent = new Intent(ScheduleDetailsActivity.this, RouteMapActivity.class);
            intent.putExtra(RouteMapActivity.EXTRA_SCHEDULE, schedule);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Route information not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Utility methods for formatting
    private String formatTime(String time) {
        // Implement time formatting logic
        return time; // You can use SimpleDateFormat for proper formatting
    }

    private String formatDate(String date) {
        // Implement date formatting logic
        return date; // You can use SimpleDateFormat for proper formatting
    }

    private String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int mins = minutes % 60;
            return hours + "h " + mins + "m";
        }
    }

    private String calculateDuration(String departureTime, String arrivalTime) {
        // Implement duration calculation logic
        return "1h 30m"; // Placeholder
    }
}
