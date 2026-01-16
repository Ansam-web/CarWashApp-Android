package com.example.carwash.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPrefManager.java
 * Manages SharedPreferences for user session and app settings
 */
public class SharedPrefManager {

    private static SharedPrefManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    // مفاتيح داخلية ثابتة (بدون اعتماد على Constants)
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_FIRST_TIME = "first_time";

    private SharedPrefManager(Context context) {
        Context appCtx = context.getApplicationContext();
        this.sharedPreferences = appCtx.getSharedPreferences(
                Constants.PREFS_NAME,
                Context.MODE_PRIVATE
        );
        this.editor = sharedPreferences.edit();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    // ========================================================================
    // USER SESSION METHODS
    // ========================================================================

    public void saveUser(int id, String name, String email, String role) {
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public void saveUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, "");
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }

    public boolean isCustomer() {
        return Constants.ROLE_CUSTOMER.equals(getUserRole());
    }

    public boolean isEmployee() {
        return Constants.ROLE_EMPLOYEE.equals(getUserRole());
    }

    public boolean isManager() {
        return Constants.ROLE_MANAGER.equals(getUserRole());
    }

    public void updateUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public void updateUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    // ========================================================================
    // APP SETTINGS METHODS
    // ========================================================================

    public boolean isFirstTime() {
        return sharedPreferences.getBoolean(KEY_FIRST_TIME, true);
    }

    public void setNotFirstTime() {
        editor.putBoolean(KEY_FIRST_TIME, false);
        editor.apply();
    }

    public void saveSetting(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getSetting(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveBooleanSetting(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanSetting(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}
