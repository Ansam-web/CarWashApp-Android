package com.example.carwash.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.models.Review;
import java.util.*;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.VH> {

    private final List<Review> list = new ArrayList<>();

    public void setList(List<Review> reviews) {
        list.clear();
        list.addAll(reviews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int p) {
        Review r = list.get(p);

        h.tvCustomer.setText(r.getCustomerName());
        h.tvService.setText(r.getServiceType());
       h.tvRating.setText("⭐ " + r.getRating() + "/5");
        h.tvReview.setText(r.getComment());
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvService, tvRating, tvReview, tvDate;
        VH(View v) {
            super(v);
            tvCustomer = v.findViewById(R.id.tvCustomer);
            tvService = v.findViewById(R.id.tvService);
            tvRating = v.findViewById(R.id.tvRating);
            tvReview = v.findViewById(R.id.tvReview);
            tvDate = v.findViewById(R.id.tvDate);
        }
    }
}
