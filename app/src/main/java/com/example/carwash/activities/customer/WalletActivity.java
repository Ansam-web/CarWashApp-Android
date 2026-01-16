package com.example.carwash.activities.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.utils.Helpers;

/**
 * WalletActivity.java
 * Displays user's wallet balance and transaction history
 * Allows recharging wallet
 */
public class WalletActivity extends BaseActivity {

    private TextView tvBalance;
    private Button btnRecharge;

    private double currentBalance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        setupToolbar();
        initViews();
        loadWalletData();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.wallet);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        tvBalance = findViewById(R.id.tvBalance);
        btnRecharge = findViewById(R.id.btnRecharge);

        btnRecharge.setOnClickListener(v -> showRechargeDialog());
    }

    /**
     * Load wallet data
     */
    private void loadWalletData() {
        showLoading("Loading wallet...");

        // TODO: Implement wallet repository
        // For now, just show demo balance
        currentBalance = 100.0;
        displayBalance();
        hideLoading();
    }

    /**
     * Display balance
     */
    private void displayBalance() {
        tvBalance.setText(Helpers.formatPrice(currentBalance));
    }

    /**
     * Show recharge dialog
     */
    private void showRechargeDialog() {
        // TODO: Implement recharge dialog
        showToast("Recharge feature coming soon!");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}