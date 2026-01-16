package com.example.carwash.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthActivity
 * Login + Register using PHP + MySQL + Volley
 */
public class AuthActivity extends BaseActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private EditText etName, etPhone;
    private Button btnSubmit;
    private TextView tvToggleMode, tvForgotPassword;
    private View layoutRegisterFields;

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
        btnSubmit = findViewById(R.id.btnSubmit);
        tvToggleMode = findViewById(R.id.tvToggleMode);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        layoutRegisterFields = findViewById(R.id.layoutRegisterFields);
    }

    private void setupMode(String mode) {
        currentMode = mode;

        if (mode.equals(Constants.MODE_LOGIN)) {
            layoutRegisterFields.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
            etPassword.setVisibility(View.VISIBLE);
            btnSubmit.setText(R.string.login);
            tvToggleMode.setText(R.string.dont_have_account);
            tvForgotPassword.setVisibility(View.VISIBLE);

        } else if (mode.equals(Constants.MODE_REGISTER)) {
            layoutRegisterFields.setVisibility(View.VISIBLE);
            etConfirmPassword.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            btnSubmit.setText(R.string.register);
            tvToggleMode.setText(R.string.already_have_account);
            tvForgotPassword.setVisibility(View.GONE);

        } else if (mode.equals(Constants.MODE_FORGOT)) {
            layoutRegisterFields.setVisibility(View.GONE);
            etConfirmPassword.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
            btnSubmit.setText(R.string.send_reset_link);
            tvToggleMode.setText(R.string.already_have_account);
            tvForgotPassword.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnSubmit.setOnClickListener(v -> handleSubmit());

        tvToggleMode.setOnClickListener(v -> {
            if (currentMode.equals(Constants.MODE_LOGIN)) {
                setupMode(Constants.MODE_REGISTER);
            } else {
                setupMode(Constants.MODE_LOGIN);
            }
        });

        tvForgotPassword.setOnClickListener(v -> setupMode(Constants.MODE_FORGOT));
    }

    private void handleSubmit() {
        if (currentMode.equals(Constants.MODE_LOGIN)) {
            handleLogin();
        } else if (currentMode.equals(Constants.MODE_REGISTER)) {
            handleRegister();
        } else {
            showInfo("Not supported", "Forgot password is not implemented yet.");
            setupMode(Constants.MODE_LOGIN);
        }
    }

    /* ================= LOGIN ================= */

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

                        if (obj.getBoolean("success")) {
                            JSONObject user = obj.getJSONObject("user");

                            SharedPrefManager.getInstance(this).saveUser(
                                    user.getInt("id"),
                                    user.getString("name"),
                                    user.getString("email"),
                                    user.getString("role")
                            );

                            showToast("Welcome back " + user.getString("name"));
                            redirectToRoleDashboard(user.getString("role"));
                        } else {
                            showError(obj.getString("message"));
                        }

                    } catch (Exception e) {
                        showError("Response error");
                    }
                },
                error -> {
                    hideLoading();
                    showError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).add(request);
    }

    /* ================= REGISTER ================= */

    private void handleRegister() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

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

                        if (obj.getBoolean("success")) {
                            showToast("Account created successfully");
                            setupMode(Constants.MODE_LOGIN);
                        } else {
                            showError(obj.getString("message"));
                        }

                    } catch (Exception e) {
                        showError("Response error");
                    }
                },
                error -> {
                    hideLoading();
                    showError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                return params;
            }
        };

        VolleySingleton.getInstance(this).add(request);
    }

    /* ================= VALIDATION ================= */

    private boolean validateLogin(String email, String password) {
        if (Helpers.isEmpty(email)) {
            etEmail.setError(Constants.ERROR_EMPTY_FIELD);
            return false;
        }
        if (!Helpers.isValidEmail(email)) {
            etEmail.setError(Constants.ERROR_INVALID_EMAIL);
            return false;
        }
        if (Helpers.isEmpty(password)) {
            etPassword.setError(Constants.ERROR_EMPTY_FIELD);
            return false;
        }
        return true;
    }

    private boolean validateRegister(String email, String password, String confirmPassword,
                                     String name, String phone) {
        if (Helpers.isEmpty(name)) {
            etName.setError(Constants.ERROR_EMPTY_FIELD);
            return false;
        }
        if (!Helpers.isValidEmail(email)) {
            etEmail.setError(Constants.ERROR_INVALID_EMAIL);
            return false;
        }
        if (!Helpers.isValidPhone(phone)) {
            etPhone.setError(Constants.ERROR_INVALID_PHONE);
            return false;
        }
        if (!Helpers.isValidPassword(password)) {
            etPassword.setError(Constants.ERROR_PASSWORD_SHORT);
            return false;
        }
        if (!Helpers.passwordsMatch(password, confirmPassword)) {
            etConfirmPassword.setError(Constants.ERROR_PASSWORDS_DONT_MATCH);
            return false;
        }
        return true;
    }

    /* ================= REDIRECT ================= */

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
                showError("Unknown role");
                return;
        }

        startActivity(intent);
        finish();
    }
}
