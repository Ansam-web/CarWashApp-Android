package com.example.carwash.activities.employee;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.BookingsAdapter;
import com.example.carwash.database.*;
import com.example.carwash.models.Booking;
import java.util.List;

/**
 * BookingsManageActivity.java
 * Shows bookings assigned to employee/team
 * Allows updating booking status
 */
public class BookingsManageActivity extends BaseActivity {

    private RecyclerView recyclerBookings;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private BookingsAdapter adapter;
    private BookingRepository bookingRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_manage);

        setupToolbar();
        initViews();
        setupRecyclerView();
        loadBookings();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.assigned_bookings);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        recyclerBookings = findViewById(R.id.recyclerBookings);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        bookingRepository = new BookingRepository(this);
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new BookingsAdapter(this);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerBookings.setAdapter(adapter);

        adapter.setOnBookingClickListener(booking -> {
            showBookingOptions(booking);
        });
    }

    /**
     * Load bookings
     */
    private void loadBookings() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerBookings.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        // TODO: Load bookings assigned to employee's team
        // For now, show empty state
        progressBar.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
        tvEmptyState.setText("No assigned bookings");
    }

    /**
     * Show booking options dialog
     */
    private void showBookingOptions(Booking booking) {
        String[] options = {
                "Update Status",
                "Navigate to Customer",
                "View Details"
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle("Booking Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showUpdateStatusDialog(booking);
                            break;
                        case 1:
                            showToast("Navigation feature coming soon!");
                            break;
                        case 2:
                            showToast("Booking ID: " + booking.getBookingId());
                            break;
                    }
                })
                .show();
    }

    /**
     * Show update status dialog
     */
    private void showUpdateStatusDialog(Booking booking) {
        String[] statuses = {
                "On the Way",
                "Arrived",
                "In Progress",
                "Completed"
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle("Update Status")
                .setItems(statuses, (dialog, which) -> {
                    String newStatus;
                    switch (which) {
                        case 0:
                            newStatus = "on_the_way";
                            break;
                        case 1:
                            newStatus = "arrived";
                            break;
                        case 2:
                            newStatus = "in_progress";
                            break;
                        case 3:
                            newStatus = "completed";
                            break;
                        default:
                            return;
                    }
                    updateBookingStatus(booking.getBookingId(), newStatus);
                })
                .show();
    }

    /**
     * Update booking status
     */
    private void updateBookingStatus(String bookingId, String newStatus) {
        showLoading("Updating status...");

        bookingRepository.updateBookingStatus(bookingId, newStatus,
                new BookingRepository.OnBookingStatusUpdatedListener() {
                    @Override
                    public void onStatusUpdated() {
                        hideLoading();
                        showToast("Status updated successfully");
                        loadBookings();
                    }

                    @Override
                    public void onError(String error) {
                        hideLoading();
                        showError(error);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}