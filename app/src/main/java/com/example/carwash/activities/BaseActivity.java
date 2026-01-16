package com.example.carwash.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carwash.utils.Helpers;
import com.example.carwash.utils.SharedPrefManager;

/**
 * BaseActivity.java
 * Base class for all activities with common functionality
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPrefManager prefManager;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize managers
        prefManager = SharedPrefManager.getInstance(this);

        // Initialize progress dialog
        initProgressDialog();
    }

    /**
     * Initialize progress dialog
     */
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    // ========================================================================
    // LOADING METHODS
    // ========================================================================

    /**
     * Show loading dialog with custom message
     */
    protected void showLoading(String message) {
        if (progressDialog != null && !isFinishing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     * Show loading dialog with default message
     */
    protected void showLoading() {
        showLoading("Please wait...");
    }

    /**
     * Hide loading dialog
     */
    protected void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // ========================================================================
    // TOAST METHODS
    // ========================================================================

    /**
     * Show short toast
     */
    protected void showToast(String message) {
        Helpers.showToast(this, message);
    }

    /**
     * Show long toast
     */
    protected void showLongToast(String message) {
        Helpers.showLongToast(this, message);
    }

    // ========================================================================
    // DIALOG METHODS
    // ========================================================================

    /**
     * Show error message
     */
    protected void showError(String message) {
        hideLoading();
        Helpers.showErrorDialog(this, message);
    }

    /**
     * Show info message
     */
    protected void showInfo(String title, String message) {
        Helpers.showInfoDialog(this, title, message);
    }

    /**
     * Show confirmation dialog
     */
    protected void showConfirmation(String title, String message,
                                    Helpers.OnConfirmListener listener) {
        Helpers.showConfirmDialog(this, title, message, listener);
    }

    // ========================================================================
    // NAVIGATION METHODS
    // ========================================================================

    /**
     * Hide keyboard
     */
    protected void hideKeyboard() {
        Helpers.hideKeyboard(this);
    }

    /**
     * Finish activity with result
     */
    protected void finishWithSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    // ========================================================================
    // LIFECYCLE METHODS
    // ========================================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}