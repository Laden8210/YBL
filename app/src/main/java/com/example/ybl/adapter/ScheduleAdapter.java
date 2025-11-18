package com.example.ybl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ybl.R;
import com.example.ybl.model.Schedule;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;
    private ScheduleClickListener clickListener;

    public interface ScheduleClickListener {
        void onStartTripClick(Schedule schedule);
        void onViewDetailsClick(Schedule schedule);
    }

    public ScheduleAdapter(List<Schedule> scheduleList, ScheduleClickListener clickListener) {
        this.scheduleList = scheduleList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.bind(schedule, clickListener);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRouteName, tvStatus, tvDepartureTime, tvArrivalTime, tvBusInfo, tvStartPoint, tvEndPoint;
        private Button btnStartTrip, btnViewDetails;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvBusInfo = itemView.findViewById(R.id.tvBusInfo);
            tvStartPoint = itemView.findViewById(R.id.tvStartPoint);
            tvEndPoint = itemView.findViewById(R.id.tvEndPoint);
            btnStartTrip = itemView.findViewById(R.id.btnStartTrip);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

        public void bind(Schedule schedule, ScheduleClickListener clickListener) {
            // Set route information
            if (schedule.getRoute() != null) {
                tvRouteName.setText(schedule.getRoute().getRouteName());
                tvStartPoint.setText(schedule.getRoute().getStartPoint());
                tvEndPoint.setText(schedule.getRoute().getEndPoint());
            }

            // Set bus information
            if (schedule.getBus() != null) {
                String busInfo = "Bus #" + schedule.getBus().getBusNumber() +
                        " (" + schedule.getBus().getLicensePlate() + ")";
                tvBusInfo.setText(busInfo);
            }

            // Set times
            tvDepartureTime.setText(formatTime(schedule.getDepartureTime()));
            tvArrivalTime.setText(formatTime(schedule.getArrivalTime()));

            // Set status with appropriate background
            tvStatus.setText(schedule.getStatus());
            setStatusBackground(schedule.getStatus());

            // Set button visibility based on status
            setupButtons(schedule, clickListener);
        }

        private String formatTime(String time) {
            // Implement time formatting logic
            return time; // You can format this as needed
        }

        private void setStatusBackground(String status) {
            int backgroundRes = R.drawable.bg_status_pending;
            if ("completed".equalsIgnoreCase(status)) {
                backgroundRes = R.drawable.bg_status_completed;
            } else if ("in_progress".equalsIgnoreCase(status)) {
                backgroundRes = R.drawable.bg_status_in_progress;
            } else if ("cancelled".equalsIgnoreCase(status)) {
                backgroundRes = R.drawable.bg_status_cancelled;
            }
            tvStatus.setBackgroundResource(backgroundRes);
        }

        private void setupButtons(Schedule schedule, ScheduleClickListener clickListener) {

            btnViewDetails.setOnClickListener(v -> clickListener.onViewDetailsClick(schedule));
        }
    }
}