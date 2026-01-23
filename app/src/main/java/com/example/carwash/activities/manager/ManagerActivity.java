package com.example.carwash.activities.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.activities.auth.AuthActivity;

/**
 * ManagerActivity.java
 * Main dashboard for manager users
 * Shows management options for services, teams, bookings, and statistics
 */
public class ManagerActivity extends BaseActivity {

    private TextView tvWelcome;
    private CardView cardStatistics, cardManageServices, cardManageTeams;
    private CardView cardManageBookings, cardManageEmployees, cardReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        setupToolbar();
        initViews();
        setupListeners();
        displayUserInfo();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.manager_dashboard);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardStatistics = findViewById(R.id.cardStatistics);
        cardManageServices = findViewById(R.id.cardManageServices);
        cardManageTeams = findViewById(R.id.cardManageTeams);
        cardManageBookings = findViewById(R.id.cardManageBookings);
        cardManageEmployees = findViewById(R.id.cardManageEmployees);
        cardReviews = findViewById(R.id.cardReviews);
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        cardStatistics.setOnClickListener(v -> openStatistics());
        cardManageServices.setOnClickListener(v -> openManageServices());
        cardManageTeams.setOnClickListener(v -> openManageTeams());
        cardManageBookings.setOnClickListener(v -> openManageBookings());
        cardManageEmployees.setOnClickListener(v -> openManageEmployees());
        cardReviews.setOnClickListener(v -> openReviews());
    }

    /**
     * Display user info
     */
    private void displayUserInfo() {
        String userName = prefManager.getUserName();
        tvWelcome.setText("Welcome, " + userName + "!");
    }

    /**
     * Open statistics
     */
    private void openStatistics() {
        // فتح شاشة الإحصائيات
        Intent intent = new Intent(this, ManagerStatsActivity.class);
        startActivity(intent);
    }

    /**
     * Open manage services
     */
    private void openManageServices() {
        Intent intent = new Intent(this, ServicesManageActivity.class);
        startActivity(intent);
    }

    /**
     * Open manage teams
     */
    private void openManageTeams() {
        Intent intent = new Intent(this, TeamsManageActivity.class);
        startActivity(intent);
    }

    /**
     * Open manage bookings
     */
    private void openManageBookings() {
        // Intent intent = new Intent(this, ManagerBookingsActivity.class);
        // startActivity(intent);

        showToast("Manage Bookings feature coming soon!");
    }

    /**
     * Open manage employees
     */
    private void openManageEmployees() {
        // Intent intent = new Intent(this, EmployeesManageActivity.class);
        // startActivity(intent);

        showToast("Manage Employees feature coming soon!");
    }

    /**
     * Open reviews
     */
    private void openReviews() {
        // Intent intent = new Intent(this, ReviewsActivity.class);
        // startActivity(intent);

        showToast("Customer Reviews feature coming soon!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        showConfirmation(
                "Logout",
                getString(R.string.logout_confirm),
                () -> {
                    prefManager.logout();

                    Intent intent = new Intent(this, AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
        );
    }
}
