package com.example.carwash.activities.customer;

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
 * CustomerActivity.java
 * Main dashboard for customer users
 * Shows main menu and quick actions
 */
public class CustomerActivity extends BaseActivity {

    private TextView tvWelcome;
    private CardView cardServices, cardBookings, cardCars, cardWallet, cardProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

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
            getSupportActionBar().setTitle(R.string.dashboard);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardServices = findViewById(R.id.cardServices);
        cardBookings = findViewById(R.id.cardBookings);
        cardCars = findViewById(R.id.cardCars);
        cardWallet = findViewById(R.id.cardWallet);
        cardProfile = findViewById(R.id.cardProfile);
    }

    /**
     * Setup click listeners
     */
    private void setupListeners() {
        cardServices.setOnClickListener(v -> openServices());
        cardBookings.setOnClickListener(v -> openBookings());
        cardCars.setOnClickListener(v -> openCars());
        cardWallet.setOnClickListener(v -> openWallet());
        cardProfile.setOnClickListener(v -> openProfile());
    }

    /**
     * Display user information
     */
    private void displayUserInfo() {
        String userName = prefManager.getUserName();
        tvWelcome.setText("Welcome, " + userName + "!");
    }

    /**
     * Open Services activity
     */
    private void openServices() {
        Intent intent = new Intent(this, ServicesActivity.class);
        startActivity(intent);
    }

    /**
     * Open Bookings activity
     */
    private void openBookings() {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(Constants.EXTRA_MODE, Constants.MODE_VIEW);
        startActivity(intent);
    }

    /**
     * Open Cars activity
     */
    private void openCars() {
        Intent intent = new Intent(this, CarsActivity.class);
        startActivity(intent);
    }

    /**
     * Open Wallet activity
     */
    private void openWallet() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    /**
     * Open Profile activity
     */
    private void openProfile() {
        // TODO: Implement ProfileActivity
        showToast("Profile coming soon!");
    }

    /**
     * Create options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handle menu item clicks
     */
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
                "Are you sure you want to logout?",
                () -> {

                    // Clear local data
                    prefManager.logout();

                    // Redirect to auth screen
                    Intent intent = new Intent(this, AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
        );
    }
}