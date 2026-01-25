package com.example.carwash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.models.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomerServicesAdapter
 * SIMPLE adapter just to show services (NO edit / delete)
 */
public class CustomerServicesAdapter extends RecyclerView.Adapter<CustomerServicesAdapter.VH> {

    public interface Listener {
        void onClick(Service service);
    }

    private final List<Service> list = new ArrayList<>();
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<Service> services) {
        list.clear();
        if (services != null) list.addAll(services);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_customer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Service s = list.get(position);

        h.tvType.setText(s.getType());
        h.tvPrice.setText(String.valueOf(s.getPrice()));

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvType, tvPrice;

        VH(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
