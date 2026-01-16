package com.example.carwash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carwash.R;
import com.example.carwash.activities.auth.AuthActivity;
import com.example.carwash.activities.customer.CustomerActivity;
import com.example.carwash.activities.employee.EmployeeActivity;
import com.example.carwash.activities.manager.ManagerActivity;
import com.example.carwash.utils.Constants;
import com.example.carwash.utils.SharedPrefManager;

/**
 * SplashActivity.java
 * Initial activity shown when app launches
 * Checks if user is logged in and redirects accordingly
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefManager = SharedPrefManager.getInstance(this);

        // Delay for splash screen (safe for newer Android versions)
        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus,
                Constants.SPLASH_DURATION);
    }

    /**
     * Check if user is logged in and redirect accordingly
     */
    private void checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            redirectToRoleDashboard();
        } else {
            redirectToAuth();
        }
    }

    /**
     * Redirect to appropriate dashboard based on user role
     */
    private void redirectToRoleDashboard() {
        String role = prefManager.getUserRole();
        Intent intent;

        switch (role) {
            case Constants.ROLE_CUSTOMER:
                intent = new Intent(this, CustomerActivity.class);
                break;
            case Constants.ROLE_EMPLOYEE:
                intent = new Intent(this, EmployeeActivity.class);
                break;
            case Constants.ROLE_MANAGER:
                intent = new Intent(this, ManagerActivity.class);
                break;
            default:
                // Unknown role, logout and go to auth
                prefManager.logout();
                intent = new Intent(this, AuthActivity.class);
                intent.putExtra(Constants.EXTRA_MODE, Constants.MODE_LOGIN);
                break;
        }

        startActivity(intent);
        finish();
    }

    /**
     * Redirect to authentication screen
     */
    private void redirectToAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra(Constants.EXTRA_MODE, Constants.MODE_LOGIN);
        startActivity(intent);
        finish();
    }
}
