package com.example.carwash.activities.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.VH> {

    private List<Review> list = new ArrayList<>();

    public void setList(List<Review> list) {
        this.list = list;
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
    public void onBindViewHolder(@NonNull VH h, int position) {
        Review r = list.get(position);

        h.tvCustomer.setText(r.getCustomerName());
        h.tvService.setText(r.getServiceType());
        h.tvRating.setText("⭐ " + r.getRating() + "/5");

        h.tvReview.setText(
                r.getComment() == null || r.getComment().trim().isEmpty()
                        ? "No comment"
                        : r.getComment()
        );

        h.tvDate.setText(r.getReviewDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvCustomer, tvService, tvRating, tvReview, tvDate;

        VH(@NonNull View itemView) {
            super(itemView);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvService = itemView.findViewById(R.id.tvService);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReview = itemView.findViewById(R.id.tvReview);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
