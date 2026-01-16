package com.example.carwash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.models.Service;
import com.example.carwash.utils.Helpers;
import java.util.ArrayList;
import java.util.List;

/**
 * ServicesAdapter.java
 * RecyclerView Adapter for displaying services list
 */
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> servicesList;
    private OnServiceClickListener listener;

    public ServicesAdapter(Context context) {
        this.context = context;
        this.servicesList = new ArrayList<>();
    }

    public void setServicesList(List<Service> servicesList) {
        this.servicesList = servicesList;
        notifyDataSetChanged();
    }

    public void setOnServiceClickListener(OnServiceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = servicesList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {

        CardView cardService;
        TextView tvServiceType;
        TextView tvServiceDescription;
        TextView tvServiceDuration;
        TextView tvServicePrice;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            cardService = itemView.findViewById(R.id.cardService);
            tvServiceType = itemView.findViewById(R.id.tvServiceType);
            tvServiceDescription = itemView.findViewById(R.id.tvServiceDescription);
            tvServiceDuration = itemView.findViewById(R.id.tvServiceDuration);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
        }

        public void bind(Service service) {
            tvServiceType.setText(service.getType());
            tvServiceDescription.setText(service.getDescription());
            tvServiceDuration.setText(Helpers.formatDuration(service.getDuration()));
            tvServicePrice.setText(Helpers.formatPrice(service.getPrice()));

            // Click listener
            cardService.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceClick(service);
                }
            });
        }
    }

    // Interface for click events
    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }
}