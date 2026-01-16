package com.example.carwash.activities.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.activities.auth.AuthActivity;
import com.example.carwash.utils.Constants;

/**
 * EmployeeActivity.java
 * Main dashboard for employee users
 * Shows assigned bookings and team info
 */
public class EmployeeActivity extends BaseActivity {

    private TextView tvWelcome;
    private CardView cardAssignedBookings, cardMyTeam, cardProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

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
            getSupportActionBar().setTitle(R.string.employee_dashboard);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardAssignedBookings = findViewById(R.id.cardAssignedBookings);
        cardMyTeam = findViewById(R.id.cardMyTeam);
        cardProfile = findViewById(R.id.cardProfile);
    }

    /**
     * Setup listeners
     */
    private void setupListeners() {
        cardAssignedBookings.setOnClickListener(v -> openAssignedBookings());
        cardMyTeam.setOnClickListener(v -> openMyTeam());
        cardProfile.setOnClickListener(v -> openProfile());
    }

    /**
     * Display user info
     */
    private void displayUserInfo() {
        String userName = prefManager.getUserName();
        tvWelcome.setText("Welcome, " + userName + "!");
    }

    /**
     * Open assigned bookings
     */
    private void openAssignedBookings() {
        Intent intent = new Intent(this, BookingsManageActivity.class);
        intent.putExtra(Constants.EXTRA_MODE, "assigned");
        startActivity(intent);
    }

    /**
     * Open my team
     */
    private void openMyTeam() {
        showToast("My Team feature coming soon!");
    }

    /**
     * Open profile
     */
    private void openProfile() {
        showToast("Profile feature coming soon!");
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