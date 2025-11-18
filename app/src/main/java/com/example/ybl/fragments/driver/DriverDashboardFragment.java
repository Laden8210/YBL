package com.example.ybl.fragments.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybl.R;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.AssignedBus;
import com.example.ybl.model.DriverDashboard;
import com.example.ybl.repository.DriverRepository;
import com.example.ybl.util.SessionManager;

public class DriverDashboardFragment extends Fragment {

    private DriverRepository driverRepository;
    private ProgressBar progressBar;
    private TextView  tvTodayTrips, tvBusNumber, tvLicensePlate, tvBusId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driverRepository = new DriverRepository(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_dashboard, container, false);

        initViews(view);
        retrieveData();

        return view;
    }

    private void initViews(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        tvTodayTrips = view.findViewById(R.id.tvTodayTrips);
        tvBusNumber = view.findViewById(R.id.tvBusNumber);
        tvLicensePlate = view.findViewById(R.id.tvLicensePlate);
        tvBusId = view.findViewById(R.id.tvBusId);

        // Set up button click listeners
        view.findViewById(R.id.btnStartTrip).setOnClickListener(v -> startTrip());
        view.findViewById(R.id.btnReportIssue).setOnClickListener(v -> reportIssue());
    }

    private void retrieveData() {
        progressBar.setVisibility(View.VISIBLE);

        driverRepository.getDashboard(new ApiCallback<DriverDashboard>() {
            @Override
            public void onSuccess(DriverDashboard response) {
                progressBar.setVisibility(View.GONE);
                updateUI(response);
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(DriverDashboard dashboard) {
        tvTodayTrips.setText(String.valueOf(dashboard.getTodayTrip()));

        if (dashboard.getAssignBus() != null) {
            AssignedBus bus = dashboard.getAssignBus();
            tvBusNumber.setText(bus.getBusNumber());
            tvLicensePlate.setText(bus.getLicensePlate());
            tvBusId.setText(String.valueOf(bus.getId()));
        }
    }

    private void startTrip() {
        // Handle start trip action
        Toast.makeText(getContext(), "Starting trip...", Toast.LENGTH_SHORT).show();
    }

    private void reportIssue() {
        // Handle report issue action
        Toast.makeText(getContext(), "Reporting issue...", Toast.LENGTH_SHORT).show();
    }
}