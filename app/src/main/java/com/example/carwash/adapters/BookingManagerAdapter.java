package com.example.carwash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.models.Booking;
import com.example.carwash.models.Team;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class BookingManagerAdapter extends RecyclerView.Adapter<BookingManagerAdapter.VH> {

    public interface OnAssignClickListener {
        void onAssignClicked(Booking booking, Team selectedTeam);
    }

    private final List<Booking> bookings;
    private List<Team> teams = new ArrayList<>();
    private final OnAssignClickListener listener;

    public BookingManagerAdapter(List<Booking> bookings, List<Team> teams, OnAssignClickListener listener) {
        this.bookings = (bookings != null) ? bookings : new ArrayList<>();
        this.teams = (teams != null) ? teams : new ArrayList<>();
        this.listener = listener;
    }

    public void setTeams(List<Team> teams) {
        this.teams = (teams != null) ? teams : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Booking b = bookings.get(position);

        // ✅ Mapping display (من API):
        // tvServiceName = service_type
        // tvCarModel = customer_name (بدل car model)
        String serviceType = safe(b.getServiceType());
        String customer = safe(b.getCustomerName());

        h.tvServiceName.setText(serviceType.isEmpty() ? "Service" : serviceType);
        h.tvCarModel.setText(customer.isEmpty() ? "Customer" : customer);

        h.tvBookingDate.setText(safe(b.getBookingDate()));
        h.tvBookingTime.setText(safe(b.getBookingTime()));
        h.tvAddress.setText(safe(b.getLocationAddress()));

        String status = safe(b.getStatus()).toLowerCase();
        if (status.isEmpty()) status = "pending";
        h.tvBookingStatus.setText(status);

        h.tvBookingPrice.setText(String.valueOf(b.getTotalPrice()));

        String teamName = safe(b.getTeamName());
        if (!teamName.isEmpty()) {
            h.tvTeam.setText("Team: " + teamName);
        } else if (b.getTeamId() != null && !b.getTeamId().trim().isEmpty()) {
            h.tvTeam.setText("Team: #" + b.getTeamId());
        } else {
            h.tvTeam.setText("Team: -");
        }

        // Accent bar (اختياري) حسب الحالة
        if ("completed".equals(status)) {
            h.statusIndicator.setBackgroundResource(R.drawable.bg_accent_bar_4);
        } else if ("assigned".equals(status)) {
            h.statusIndicator.setBackgroundResource(R.drawable.bg_accent_bar_2);
        } else if ("cancelled".equals(status)) {
            h.statusIndicator.setBackgroundResource(R.drawable.bg_accent_bar_3);
        } else {
            h.statusIndicator.setBackgroundResource(R.drawable.bg_accent_bar_5);
        }

        // ✅ Spinner: نعرض أسماء الفرق فقط (بدون تغيير Team.toString())
        List<String> teamNames = new ArrayList<>();
        for (Team t : teams) {
            teamNames.add(safe(t.getName()));
        }

        ArrayAdapter<String> ad = new ArrayAdapter<>(
                h.itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                teamNames
        );
        h.spTeams.setAdapter(ad);

        // إخفاء assign إذا already assigned أو team موجود
        boolean alreadyAssigned = "assigned".equals(status)
                || (b.getTeamId() != null && !b.getTeamId().trim().isEmpty())
                || !teamName.isEmpty();

        h.layoutAssign.setVisibility(alreadyAssigned ? View.GONE : View.VISIBLE);

        h.btnAssign.setOnClickListener(v -> {
            int idx = h.spTeams.getSelectedItemPosition();
            Team selected = (idx >= 0 && idx < teams.size()) ? teams.get(idx) : null;
            if (listener != null) listener.onAssignClicked(b, selected);
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View statusIndicator;
        TextView tvServiceName, tvCarModel, tvBookingDate, tvBookingTime, tvAddress;
        TextView tvBookingStatus, tvBookingPrice, tvTeam;
        Spinner spTeams;
        MaterialButton btnAssign;
        View layoutAssign;

        VH(@NonNull View itemView) {
            super(itemView);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);

            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvCarModel = itemView.findViewById(R.id.tvCarModel);

            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);

            tvAddress = itemView.findViewById(R.id.tvAddress);

            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvBookingPrice = itemView.findViewById(R.id.tvBookingPrice);

            tvTeam = itemView.findViewById(R.id.tvTeam);

            spTeams = itemView.findViewById(R.id.spTeams);
            btnAssign = itemView.findViewById(R.id.btnAssign);
            layoutAssign = itemView.findViewById(R.id.layoutAssign);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
