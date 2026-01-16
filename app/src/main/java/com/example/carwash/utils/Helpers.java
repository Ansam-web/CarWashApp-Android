package com.example.carwash.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Helpers.java
 * Contains all helper functions in one place
 * Consolidates: ValidationHelper, DateTimeHelper, DialogHelper, UIHelper
 */
public class Helpers {

    // ========================================================================
    // VALIDATION HELPERS
    // ========================================================================

    /**
     * Check if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if phone number is valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces and special characters
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        return cleanPhone.matches("^[0-9]{10,}$");
    }

    /**
     * Check if string is empty
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Check if password is valid (min 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }

    /**
     * Check if two passwords match
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }

    // ========================================================================
    // DATE & TIME HELPERS
    // ========================================================================

    /**
     * Format timestamp to date string
     */
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * Format timestamp to time string
     */
    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * Format timestamp to datetime string
     */
    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    /**
     * Get current date string
     */
    public static String getCurrentDate() {
        return formatDate(System.currentTimeMillis());
    }

    /**
     * Get current time string
     */
    public static String getCurrentTime() {
        return formatTime(System.currentTimeMillis());
    }

    /**
     * Show date picker dialog
     */
    public static void showDatePicker(Context context, OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%d",
                            dayOfMonth, month + 1, year);
                    listener.onDateSelected(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * Show time picker dialog
     */
    public static void showTimePicker(Context context, OnTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d",
                            hourOfDay, minute);
                    listener.onTimeSelected(time);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    // ========================================================================
    // DIALOG HELPERS
    // ========================================================================

    /**
     * Show short toast message
     */
    public static void showToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show long toast message
     */
    public static void showLongToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show confirmation dialog
     */
    public static void showConfirmDialog(Context context, String title, String message,
                                         OnConfirmListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, which) -> listener.onConfirm())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    /**
     * Show info dialog
     */
    public static void showInfoDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    /**
     * Show error dialog
     */
    public static void showErrorDialog(Context context, String message) {
        showInfoDialog(context, "Error", message);
    }

    // ========================================================================
    // UI HELPERS
    // ========================================================================

    /**
     * Hide keyboard
     */
    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Get status color based on booking status
     */
    public static String getStatusColor(String status) {
        if (status == null) return "#9E9E9E";

        switch (status) {
            case Constants.STATUS_PENDING:
                return "#FFC107"; // Amber
            case Constants.STATUS_ASSIGNED:
                return "#2196F3"; // Blue
            case Constants.STATUS_ON_THE_WAY:
                return "#9C27B0"; // Purple
            case Constants.STATUS_ARRIVED:
                return "#00BCD4"; // Cyan
            case Constants.STATUS_IN_PROGRESS:
                return "#FF9800"; // Orange
            case Constants.STATUS_COMPLETED:
                return "#4CAF50"; // Green
            case Constants.STATUS_CANCELLED_CUSTOMER:
            case Constants.STATUS_CANCELLED_COMPANY:
                return "#F44336"; // Red
            default:
                return "#9E9E9E"; // Grey
        }
    }

    /**
     * Get status text (formatted)
     */
    public static String getStatusText(String status) {
        if (status == null) return "Unknown";

        switch (status) {
            case Constants.STATUS_PENDING:
                return "Pending";
            case Constants.STATUS_ASSIGNED:
                return "Assigned";
            case Constants.STATUS_ON_THE_WAY:
                return "On the Way";
            case Constants.STATUS_ARRIVED:
                return "Arrived";
            case Constants.STATUS_IN_PROGRESS:
                return "In Progress";
            case Constants.STATUS_COMPLETED:
                return "Completed";
            case Constants.STATUS_CANCELLED_CUSTOMER:
                return "Cancelled by Customer";
            case Constants.STATUS_CANCELLED_COMPANY:
                return "Cancelled by Company";
            default:
                return status.replace("_", " ");
        }
    }

    /**
     * Format price
     */
    public static String formatPrice(double price) {
        return String.format(Locale.getDefault(), "$%.2f", price);
    }

    /**
     * Format duration
     */
    public static String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int mins = minutes % 60;
            if (mins == 0) {
                return hours + " hr";
            } else {
                return hours + " hr " + mins + " min";
            }
        }
    }

    // ========================================================================
    // INTERFACES
    // ========================================================================

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}