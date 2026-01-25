package com.example.carwash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.models.Service;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.VH> {

    public interface OnServiceActionListener {
        void onClick(Service service);
        void onEdit(Service service);
        void onDelete(Service service);
    }

    private final List<Service> list = new ArrayList<>();
    private OnServiceActionListener listener;

    // ✅ عشان الكستمر: new ServicesAdapter()
    public ServicesAdapter() {}

    // ✅ عشان أي كود قديم: new ServicesAdapter(this)
    public ServicesAdapter(Context ignored) {}

    public void setOnServiceActionListener(OnServiceActionListener listener) {
        this.listener = listener;
    }

    public void setServicesList(List<Service> services) {
        list.clear();
        if (services != null) list.addAll(services);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Service s = list.get(position);

        h.tvType.setText(s.getType());
        h.tvDesc.setText(s.getDescription() == null ? "" : s.getDescription());

        String meta = s.getDuration() + " min • " + s.getPrice();
        h.tvMeta.setText(meta);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(s);
        });

        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(s);
        });

        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(s);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvType, tvDesc, tvMeta;
        ImageButton btnEdit, btnDelete;

        VH(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
