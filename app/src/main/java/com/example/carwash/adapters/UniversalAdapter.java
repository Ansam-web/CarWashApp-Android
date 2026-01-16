package com.example.carwash.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;

/**
 * UniversalAdapter.java
 * Generic RecyclerView Adapter for simple lists (Teams, Employees, Notifications, etc.)
 * Uses ListAdapter and DiffUtil for better performance.
 */
public class UniversalAdapter<T> extends ListAdapter<T, UniversalAdapter.UniversalViewHolder> {

    private OnItemClickListener<T> listener;
    private final ItemBinder<T> binder;

    public UniversalAdapter(@NonNull ItemBinder<T> binder, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
        this.binder = binder;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UniversalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_universal, parent, false);
        return new UniversalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversalViewHolder holder, int position) {
        T item = getItem(position);

        // Use the binder to display item data
        if (binder != null) {
            binder.bindData(item, holder.tvTitle, holder.tvSubtitle, holder.tvExtra);
        }

        // Click listener
        holder.cardItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    static class UniversalViewHolder extends RecyclerView.ViewHolder {

        CardView cardItem;
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvExtra;

        public UniversalViewHolder(@NonNull View itemView) {
            super(itemView);

            cardItem = itemView.findViewById(R.id.cardItem);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubtitle = itemView.findViewById(R.id.tvSubtitle);
            tvExtra = itemView.findViewById(R.id.tvExtra);
        }
    }

    // Interface for custom binding
    public interface ItemBinder<T> {
        void bindData(T item, TextView title, TextView subtitle, TextView extra);
    }

    // Interface for click events
    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }
}

// ============================================================================
// USAGE EXAMPLES
// ============================================================================

/*

// Example 1: Teams Adapter

// 1. Create a DiffUtil.ItemCallback for the Team class
DiffUtil.ItemCallback<Team> teamDiffCallback = new DiffUtil.ItemCallback<Team>() {
    @Override
    public boolean areItemsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
        // Return true if items have the same ID
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
        // Return true if contents are the same.
        // This relies on a correct equals() implementation in your model class.
        return oldItem.equals(newItem);
    }
};

// 2. Create the binder
UniversalAdapter.ItemBinder<Team> teamBinder = (team, title, subtitle, extra) -> {
    title.setText(team.getName());
    subtitle.setText(team.getCarNumber());
    extra.setText(team.isAvailable() ? "Available" : "Busy");
};

// 3. Create the adapter instance
UniversalAdapter<Team> teamsAdapter = new UniversalAdapter<>(teamBinder, teamDiffCallback);


// Example 2: Employees Adapter (with data class that has good equals())
DiffUtil.ItemCallback<User> employeeDiffCallback = new DiffUtil.ItemCallback<User>() {
    @Override
    public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
        return oldItem.getUid().equals(newItem.getUid());
    }

    @Override
    public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
        return oldItem.equals(newItem);
    }
};

UniversalAdapter<User> employeesAdapter = new UniversalAdapter<>(
    (employee, title, subtitle, extra) -> {
        title.setText(employee.getName());
        subtitle.setText(employee.getEmail());
        extra.setText(employee.getPhone());
    },
    employeeDiffCallback
);

// --- Using the adapter ---

// Set click listener (optional)
teamsAdapter.setOnItemClickListener(team -> {
    // Handle team click
});

// Set data by submitting the list (it will be diffed and updated automatically)
teamsAdapter.submitList(teamsList);

*/
