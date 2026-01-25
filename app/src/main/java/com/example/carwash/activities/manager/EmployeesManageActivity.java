package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.UniversalAdapter;
import com.example.carwash.database.EmployeeRepository;
import com.example.carwash.models.Employee;
import com.example.carwash.models.EmployeeTeam;
import com.example.carwash.utils.Helpers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmployeesManageActivity extends BaseActivity {

    private RecyclerView recyclerEmployees;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddEmployee;
    private View layoutEmployeeForm;

    private EditText etName, etEmail, etPhone, etPassword;
    private View layoutPassword;
    private Button btnSave, btnCancel;

    private UniversalAdapter<Employee> adapter;
    private EmployeeRepository employeeRepository;

    private Employee currentEmployee;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_manage);

        setupToolbar();
        initViews();
        setupRecyclerView();
        loadEmployees();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Employees");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerEmployees = findViewById(R.id.recyclerEmployees);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddEmployee = findViewById(R.id.fabAddEmployee);
        layoutEmployeeForm = findViewById(R.id.layoutEmployeeForm);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        layoutPassword = findViewById(R.id.layoutPassword);
        etPassword = findViewById(R.id.etPassword);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        employeeRepository = new EmployeeRepository(this);

        fabAddEmployee.setOnClickListener(v -> showAddForm());
        btnCancel.setOnClickListener(v -> hideForm());
        btnSave.setOnClickListener(v -> saveEmployee());
    }

    private void setupRecyclerView() {
        DiffUtil.ItemCallback<Employee> diff = new DiffUtil.ItemCallback<Employee>() {
            @Override
            public boolean areItemsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {
                return oldItem.getId() != null && oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {
                return oldItem.getName().equals(newItem.getName())
                        && oldItem.getEmail().equals(newItem.getEmail())
                        && oldItem.getPhone().equals(newItem.getPhone())
                        && oldItem.getTeams().size() == newItem.getTeams().size();
            }
        };

        UniversalAdapter.ItemBinder<Employee> binder = (emp, title, subtitle, extra) -> {
            title.setText(emp.getName());
            subtitle.setText(emp.getEmail());

            StringBuilder sb = new StringBuilder();
            List<EmployeeTeam> teams = emp.getTeams();
            if (teams == null || teams.isEmpty()) {
                sb.append("Teams: none");
            } else {
                sb.append("Teams: ");
                for (int i = 0; i < teams.size(); i++) {
                    EmployeeTeam t = teams.get(i);
                    sb.append(t.getTeamName())
                            .append(" (")
                            .append(t.getRoleInTeam())
                            .append(")");
                    if (i < teams.size() - 1) sb.append(", ");
                }
            }
            extra.setText(sb.toString());
        };

        adapter = new UniversalAdapter<>(binder, diff);

        recyclerEmployees.setLayoutManager(new LinearLayoutManager(this));

        // ✅ FIX: prevent "floating" items
        recyclerEmployees.setItemAnimator(null);

        while (recyclerEmployees.getItemDecorationCount() > 0) {
            recyclerEmployees.removeItemDecorationAt(0);
        }

        int space = (int) (10 * getResources().getDisplayMetrics().density);
        recyclerEmployees.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull android.graphics.Rect outRect,
                                       @NonNull android.view.View view,
                                       @NonNull RecyclerView parent,
                                       @NonNull RecyclerView.State state) {
                outRect.top = space;
                outRect.bottom = space;
            }
        });

        recyclerEmployees.setAdapter(adapter);

        adapter.setOnItemClickListener(this::showEditForm);
    }

    private void loadEmployees() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerEmployees.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        employeeRepository.getAllEmployees(new EmployeeRepository.OnEmployeesLoadedListener() {
            @Override
            public void onEmployeesLoaded(List<Employee> employees) {
                progressBar.setVisibility(View.GONE);
                if (employees == null || employees.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    recyclerEmployees.setVisibility(View.VISIBLE);
                    adapter.submitList(employees);
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                showError(error);
            }
        });
    }

    private void showAddForm() {
        isEditMode = false;
        currentEmployee = null;
        clearForm();
        layoutEmployeeForm.setVisibility(View.VISIBLE);
        fabAddEmployee.hide();
        layoutPassword.setVisibility(View.VISIBLE); // password required
        btnSave.setText("Add");
    }

    private void showEditForm(Employee emp) {
        isEditMode = true;
        currentEmployee = emp;
        fillForm(emp);
        layoutEmployeeForm.setVisibility(View.VISIBLE);
        fabAddEmployee.hide();
        layoutPassword.setVisibility(View.GONE); // no password in edit
        btnSave.setText("Update");
    }

    private void hideForm() {
        layoutEmployeeForm.setVisibility(View.GONE);
        fabAddEmployee.show();
        clearForm();
    }

    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
    }

    private void fillForm(Employee e) {
        etName.setText(e.getName());
        etEmail.setText(e.getEmail());
        etPhone.setText(e.getPhone());
        etPassword.setText("");
    }

    private boolean validate(boolean needPassword) {
        if (Helpers.isEmpty(etName.getText().toString())) {
            etName.setError("Required"); return false;
        }
        if (Helpers.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required"); return false;
        }
        if (Helpers.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Required"); return false;
        }
        if (needPassword && Helpers.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required"); return false;
        }
        return true;
    }

    private void saveEmployee() {
        if (isEditMode) {
            if (!validate(false)) return;

            showLoading("Updating employee...");

            employeeRepository.updateEmployee(
                    currentEmployee.getId(),
                    etName.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etPhone.getText().toString().trim(),
                    new EmployeeRepository.OnEmployeeUpdatedListener() {
                        @Override
                        public void onEmployeeUpdated() {
                            hideLoading();
                            showToast("Employee updated");
                            hideForm();
                            loadEmployees();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError(error);
                        }
                    }
            );

        } else {
            if (!validate(true)) return;

            showLoading("Adding employee...");

            employeeRepository.addEmployee(
                    etName.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etPhone.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    new EmployeeRepository.OnEmployeeAddedListener() {
                        @Override
                        public void onEmployeeAdded() {
                            hideLoading();
                            showToast("Employee added");
                            hideForm();
                            loadEmployees();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError(error);
                        }
                    }
            );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
