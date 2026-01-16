package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.ServicesAdapter;
import com.example.carwash.database.*;
import com.example.carwash.models.Service;
import com.example.carwash.utils.Helpers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

/**
 * ServicesManageActivity.java
 * Manages services - add, edit, delete
 * Manager only
 */
public class ServicesManageActivity extends BaseActivity {

    private RecyclerView recyclerServices;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddService;
    private View layoutServiceForm;

    // Form fields
    private EditText etServiceType, etServiceDescription, etServiceDuration, etServicePrice;
    private Button btnSaveService, btnCancelService;

    private ServicesAdapter adapter;
    private ServiceRepository serviceRepository;
    private Service currentService;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_manage);

        setupToolbar();
        initViews();
        setupRecyclerView();
        loadServices();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.manage_services);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        recyclerServices = findViewById(R.id.recyclerServices);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddService = findViewById(R.id.fabAddService);
        layoutServiceForm = findViewById(R.id.layoutServiceForm);

        // Form fields
        etServiceType = findViewById(R.id.etServiceType);
        etServiceDescription = findViewById(R.id.etServiceDescription);
        etServiceDuration = findViewById(R.id.etServiceDuration);
        etServicePrice = findViewById(R.id.etServicePrice);
        btnSaveService = findViewById(R.id.btnSaveService);
        btnCancelService = findViewById(R.id.btnCancelService);

        serviceRepository = new ServiceRepository(this);

        // Listeners
        fabAddService.setOnClickListener(v -> showAddServiceForm());
        btnSaveService.setOnClickListener(v -> saveService());
        btnCancelService.setOnClickListener(v -> hideServiceForm());
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new ServicesAdapter(this);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setAdapter(adapter);

        adapter.setOnServiceClickListener(service -> {
            showEditServiceForm(service);
        });
    }

    /**
     * Load services
     */
    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerServices.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        serviceRepository.getAllServices(new ServiceRepository.OnServicesLoadedListener() {
            @Override
            public void onServicesLoaded(List<Service> services) {
                progressBar.setVisibility(View.GONE);

                if (services.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    recyclerServices.setVisibility(View.VISIBLE);
                    adapter.setServicesList(services);
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

    /**
     * Show add service form
     */
    private void showAddServiceForm() {
        isEditMode = false;
        currentService = null;
        clearForm();
        layoutServiceForm.setVisibility(View.VISIBLE);
        fabAddService.hide();
        btnSaveService.setText(R.string.add);
    }

    /**
     * Show edit service form
     */
    private void showEditServiceForm(Service service) {
        isEditMode = true;
        currentService = service;
        fillForm(service);
        layoutServiceForm.setVisibility(View.VISIBLE);
        fabAddService.hide();
        btnSaveService.setText(R.string.update);
    }

    /**
     * Hide service form
     */
    private void hideServiceForm() {
        layoutServiceForm.setVisibility(View.GONE);
        fabAddService.show();
        clearForm();
    }

    /**
     * Clear form
     */
    private void clearForm() {
        etServiceType.setText("");
        etServiceDescription.setText("");
        etServiceDuration.setText("");
        etServicePrice.setText("");
    }

    /**
     * Fill form with service data
     */
    private void fillForm(Service service) {
        etServiceType.setText(service.getType());
        etServiceDescription.setText(service.getDescription());
        etServiceDuration.setText(String.valueOf(service.getDuration()));
        etServicePrice.setText(String.valueOf(service.getPrice()));
    }

    /**
     * Save service
     */
    private void saveService() {
        if (!validateService()) {
            return;
        }

        showLoading(isEditMode ? "Updating service..." : "Adding service...");

        String type = etServiceType.getText().toString().trim();
        String description = etServiceDescription.getText().toString().trim();
        int duration = Integer.parseInt(etServiceDuration.getText().toString().trim());
        double price = Double.parseDouble(etServicePrice.getText().toString().trim());

        if (isEditMode) {
            currentService.setType(type);
            currentService.setDescription(description);
            currentService.setDuration(duration);
            currentService.setPrice(price);

            serviceRepository.updateService(currentService.getServiceId(), currentService,
                    new ServiceRepository.OnServiceUpdatedListener() {
                        @Override
                        public void onServiceUpdated() {
                            hideLoading();
                            showToast("Service updated successfully");
                            hideServiceForm();
                            loadServices();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError(error);
                        }
                    });
        } else {
            Service newService = new Service(null, type, description, duration, price);

            serviceRepository.addService(newService, new ServiceRepository.OnServiceAddedListener() {
                @Override
                public void onServiceAdded(Service service) {
                    hideLoading();
                    showToast("Service added successfully");
                    hideServiceForm();
                    loadServices();
                }

                @Override
                public void onError(String error) {
                    hideLoading();
                    showError(error);
                }
            });
        }
    }

    /**
     * Validate service inputs
     */
    private boolean validateService() {
        if (Helpers.isEmpty(etServiceType.getText().toString())) {
            etServiceType.setError("Required");
            return false;
        }

        if (Helpers.isEmpty(etServiceDuration.getText().toString())) {
            etServiceDuration.setError("Required");
            return false;
        }

        if (Helpers.isEmpty(etServicePrice.getText().toString())) {
            etServicePrice.setError("Required");
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}