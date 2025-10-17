package com.example.ybl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ybl.R;
import com.example.ybl.model.Bus;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<Bus> busList;
    private OnBusClickListener listener;

    public interface OnBusClickListener {
        void onTrackLocation(Bus bus);
        void onViewDetails(Bus bus);
        void onConfirmInfo(Bus bus);
    }

    public BusAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    public void updateData(List<Bus> newBusList) {
        this.busList = newBusList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.bind(bus);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void setOnBusClickListener(OnBusClickListener listener) {
        this.listener = listener;
    }

    class BusViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView busId, route, departureTime, status, passengerCount;
        private TextView driverName, conductorName;
        private MaterialButton btnTrack, btnDetails, btnConfirm;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardBus);
            busId = itemView.findViewById(R.id.textBusId);
            route = itemView.findViewById(R.id.textRoute);
            departureTime = itemView.findViewById(R.id.textDepartureTime);
            status = itemView.findViewById(R.id.textStatus);
            passengerCount = itemView.findViewById(R.id.textPassengerCount);
            driverName = itemView.findViewById(R.id.textDriverName);
            conductorName = itemView.findViewById(R.id.textConductorName);
            btnTrack = itemView.findViewById(R.id.btnTrack);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
        }

        public void bind(Bus bus) {
            busId.setText(bus.getBusId());
            route.setText(bus.getRoute());
            departureTime.setText(bus.getDepartureTime());
            status.setText(bus.getStatus());
            passengerCount.setText(bus.getPassengerCount());
            driverName.setText(bus.getDriverName());
            conductorName.setText(bus.getConductorName());

            // Set status color
            int statusColor = getStatusColor(bus.getStatus());
            status.setTextColor(statusColor);

            // Set up button listeners
            btnTrack.setOnClickListener(v -> {
                if (listener != null) listener.onTrackLocation(bus);
            });

            btnDetails.setOnClickListener(v -> {
                if (listener != null) listener.onViewDetails(bus);
            });

            btnConfirm.setOnClickListener(v -> {
                if (listener != null) listener.onConfirmInfo(bus);
            });

            // Enable/disable track button based on bus activity
            btnTrack.setEnabled(bus.isActive());
        }

        private int getStatusColor(String status) {
            if (status.contains("Delayed")) {
                return itemView.getContext().getColor(R.color.error_color);
            } else if (status.contains("On Time")) {
                return itemView.getContext().getColor(R.color.success_color);
            } else {
                return itemView.getContext().getColor(R.color.gray);
            }
        }
    }
}