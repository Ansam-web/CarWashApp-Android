package com.example.carwash.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.activities.customer.CustomerActivity;
import com.example.carwash.activities.employee.EmployeeActivity;
import com.example.carwash.activities.manager.ManagerActivity;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.Constants;
import com.example.carwash.utils.Helpers;
import com.example.carwash.utils.SharedPrefManager;
import com.example.carwash.utils.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends BaseActivity {

    // Inputs
    private TextInputEditText etEmail, etPassword, etConfirmPassword, etName, etPhone;

    // Layout wrappers (مهم!)
    private TextInputLayout layoutPassword, layoutConfirmPassword;
    private View layoutRegisterFields;

    private Button btnSubmit;
    private TextView tvToggleMode, tvForgotPassword;

    private String currentMode = Constants.MODE_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initViews();

        currentMode = getIntent().getStringExtra(Constants.EXTRA_MODE);
        if (currentMode == null) currentMode = Constants.MODE_LOGIN;

        setupMode(currentMode);
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);

        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);

        layoutRegisterFields = findViewById(R.id.layoutRegisterFields);

        btnSubmit = findViewById(R.id.btnSubmit);
        tvToggleMode = findViewById(R.id.tvToggleMode);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> handleSubmit());

        tvToggleMode.setOnClickListener(v -> {
            if (Constants.MODE_LOGIN.equals(currentMode)) {
                setupMode(Constants.MODE_REGISTER);
            } else {
                setupMode(Constants.MODE_LOGIN);
            }
        });

        tvForgotPassword.setOnClickListener(v -> {
            // إذا مش بدك forgot حالياً، خليه يرجع login مع رسالة
            showInfo("Not supported", "Forgot password is not implemented yet.");
        });
    }

    private void setupMode(String mode) {
        currentMode = mode;

        clearErrors();

        if (Constants.MODE_LOGIN.equals(mode)) {
            layoutRegisterFields.setVisibility(View.GONE);
            layoutConfirmPassword.setVisibility(View.GONE);

            layoutPassword.setVisibility(View.VISIBLE);
            tvForgotPassword.setVisibility(View.VISIBLE);

            btnSubmit.setText(R.string.login);
            tvToggleMode.setText(R.string.dont_have_account);

        } else if (Constants.MODE_REGISTER.equals(mode)) {
            layoutRegisterFields.setVisibility(View.VISIBLE);
            layoutConfirmPassword.setVisibility(View.VISIBLE);

            layoutPassword.setVisibility(View.VISIBLE);
            tvForgotPassword.setVisibility(View.GONE);

            btnSubmit.setText(R.string.register);
            tvToggleMode.setText(R.string.already_have_account);
        }
    }

    private void handleSubmit() {
        if (Constants.MODE_LOGIN.equals(currentMode)) {
            handleLogin();
        } else {
            handleRegister();
        }
    }

    private void handleLogin() {
        String email = safeText(etEmail);
        String password = safeText(etPassword);

        if (!validateLogin(email, password)) return;

        showLoading("Logging in...");
        hideKeyboard();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                ApiConfig.LOGIN,
                response -> {
                    hideLoading();
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            JSONObject user = obj.getJSONObject("user");

                            SharedPrefManager.getInstance(this).saveUser(
                                    user.getInt("id"),
                                    user.getString("name"),
                                    user.getString("email"),
                                    user.getString("role")
                            );

                            // لو API بيرجع phone:
                            if (user.has("phone")) {
                                SharedPrefManager.getInstance(this).saveUserPhone(user.getString("phone"));
                            }

                            showToast("Welcome back " + user.getString("name"));
                            redirectToRoleDashboard(user.getString("role"));
                        } else {
                            showError(obj.optString("message", "Login failed"));
                        }

                    } catch (Exception e) {
                        showError("Response error (JSON). Server returned: " + shortText(response));
                    }
                },
                error -> {
                    hideLoading();
                    showVolleyError(error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(this).add(request);
    }

    private void handleRegister() {
        String name = safeText(etName);
        String phone = safeText(etPhone);
        String email = safeText(etEmail);
        String password = safeText(etPassword);
        String confirmPassword = safeText(etConfirmPassword);

        if (!validateRegister(email, password, confirmPassword, name, phone)) return;

        showLoading("Creating account...");
        hideKeyboard();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                ApiConfig.REGISTER,
                response -> {
                    hideLoading();
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            showToast("Account created successfully");
                            setupMode(Constants.MODE_LOGIN);
                        } else {
                            showError(obj.optString("message", "Register failed"));
                        }

                    } catch (Exception e) {
                        showError("Response error (JSON). Server returned: " + shortText(response));
                    }
                },
                error -> {
                    hideLoading();
                    showVolleyError(error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(this).add(request);
    }

    private boolean validateLogin(String email, String password) {
        clearErrors();

        if (Helpers.isEmpty(email)) {
            etEmail.setError(Constants.ERROR_EMPTY_FIELD);
            etEmail.requestFocus();
            return false;
        }
        if (!Helpers.isValidEmail(email)) {
            etEmail.setError(Constants.ERROR_INVALID_EMAIL);
            etEmail.requestFocus();
            return false;
        }
        if (Helpers.isEmpty(password)) {
            etPassword.setError(Constants.ERROR_EMPTY_FIELD);
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateRegister(String email, String password, String confirmPassword,
                                     String name, String phone) {
        clearErrors();

        if (Helpers.isEmpty(name)) {
            etName.setError(Constants.ERROR_EMPTY_FIELD);
            etName.requestFocus();
            return false;
        }
        if (Helpers.isEmpty(phone)) {
            etPhone.setError(Constants.ERROR_EMPTY_FIELD);
            etPhone.requestFocus();
            return false;
        }
        if (!Helpers.isValidPhone(phone)) {
            etPhone.setError(Constants.ERROR_INVALID_PHONE);
            etPhone.requestFocus();
            return false;
        }
        if (Helpers.isEmpty(email)) {
            etEmail.setError(Constants.ERROR_EMPTY_FIELD);
            etEmail.requestFocus();
            return false;
        }
        if (!Helpers.isValidEmail(email)) {
            etEmail.setError(Constants.ERROR_INVALID_EMAIL);
            etEmail.requestFocus();
            return false;
        }
        if (Helpers.isEmpty(password)) {
            etPassword.setError(Constants.ERROR_EMPTY_FIELD);
            etPassword.requestFocus();
            return false;
        }
        if (!Helpers.isValidPassword(password)) {
            etPassword.setError(Constants.ERROR_PASSWORD_SHORT);
            etPassword.requestFocus();
            return false;
        }
        if (!Helpers.passwordsMatch(password, confirmPassword)) {
            etConfirmPassword.setError(Constants.ERROR_PASSWORDS_DONT_MATCH);
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void redirectToRoleDashboard(String role) {
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
                showError("Unknown role: " + role);
                return;
        }
        startActivity(intent);
        finish();
    }

    // ===== Helpers =====

    private String safeText(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void clearErrors() {
        if (etEmail != null) etEmail.setError(null);
        if (etPassword != null) etPassword.setError(null);
        if (etConfirmPassword != null) etConfirmPassword.setError(null);
        if (etName != null) etName.setError(null);
        if (etPhone != null) etPhone.setError(null);
    }

    private void showVolleyError(com.android.volley.VolleyError error) {
        // هذا بيعطيك سبب "Network error" الحقيقي
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            showError("Server error (PHP)\n" + shortText(body));
        } else {
            showError("Network error\n" + (error.getMessage() != null ? error.getMessage() : "Check URL / Internet / Cleartext"));
        }
    }

    private String shortText(String s) {
        if (s == null) return "";
        s = s.replace("\n", " ").replace("\r", " ");
        return s.length() > 180 ? s.substring(0, 180) + "..." : s;
    }
}
