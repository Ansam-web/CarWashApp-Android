package com.example.carwash.activities.employee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.models.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class EmployeeBookingsAdapter extends RecyclerView.Adapter<EmployeeBookingsAdapter.VH> {

    public interface OnBookingClick {
        void onClick(Booking booking);
    }

    private final List<Booking> list;
    private final OnBookingClick listener;

    public EmployeeBookingsAdapter(List<Booking> list, OnBookingClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_employee, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Booking b = list.get(pos);
        h.tvService.setText("Service: " + b.getServiceName());
        h.tvDateTime.setText(b.getBookingDate() + " | " + b.getBookingTime());
        h.tvStatus.setText("Status: " + b.getStatus());
        h.tvAddress.setText("Address: " + b.getLocationAddress());
        h.btnDetails.setOnClickListener(v -> listener.onClick(b));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvService, tvDateTime, tvStatus, tvAddress;
        MaterialButton btnDetails;

        VH(@NonNull View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvService);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}