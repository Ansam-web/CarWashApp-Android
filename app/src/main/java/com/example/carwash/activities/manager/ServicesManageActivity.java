package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.ServicesAdapter;
import com.example.carwash.database.ServiceRepository;
import com.example.carwash.models.Service;
import com.example.carwash.utils.Helpers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ServicesManageActivity extends BaseActivity {

    private RecyclerView recyclerServices;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    private FloatingActionButton fabAddService;
    private View layoutServiceForm;

    private TextView tvFormTitle;
    private EditText etServiceType, etServiceDescription, etServiceDuration, etServicePrice;
    private Button btnSaveService, btnCancelService;

    private ServiceRepository serviceRepository;
    private ServicesAdapter adapter;

    private Service currentService;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_manage);

        setupToolbar();
        initViews();
        setupRecycler();
        loadServices();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.manage_services);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerServices = findViewById(R.id.recyclerServices);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        fabAddService = findViewById(R.id.fabAddService);
        layoutServiceForm = findViewById(R.id.layoutServiceForm);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etServiceType = findViewById(R.id.etServiceType);
        etServiceDescription = findViewById(R.id.etServiceDescription);
        etServiceDuration = findViewById(R.id.etServiceDuration);
        etServicePrice = findViewById(R.id.etServicePrice);

        btnSaveService = findViewById(R.id.btnSaveService);
        btnCancelService = findViewById(R.id.btnCancelService);

        serviceRepository = new ServiceRepository(this);

        fabAddService.setOnClickListener(v -> showAddForm());
        btnCancelService.setOnClickListener(v -> hideForm());
        btnSaveService.setOnClickListener(v -> saveService());
    }

    private void setupRecycler() {
        adapter = new ServicesAdapter(); // no-arg
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setAdapter(adapter);

        adapter.setOnServiceActionListener(new ServicesAdapter.OnServiceActionListener() {
            @Override
            public void onEdit(Service service) {
                showEditForm(service);
            }

            @Override
            public void onDelete(Service service) {
                confirmDelete(service);
            }

            @Override
            public void onClick(Service service) {
                // optional: click = edit
                showEditForm(service);
            }
        });
    }

    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerServices.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        serviceRepository.getAllServices(new ServiceRepository.OnServicesLoadedListener() {
            @Override
            public void onServicesLoaded(List<Service> services) {
                progressBar.setVisibility(View.GONE);

                if (services == null || services.isEmpty()) {
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
                tvEmptyState.setText("Error: " + error);
                showError(error);
            }
        });
    }

    private void showAddForm() {
        isEditMode = false;
        currentService = null;
        clearForm();
        tvFormTitle.setText("Add Service");
        layoutServiceForm.setVisibility(View.VISIBLE);
        fabAddService.hide();
        btnSaveService.setText("Add");
    }

    private void showEditForm(Service s) {
        isEditMode = true;
        currentService = s;

        tvFormTitle.setText("Edit Service");
        etServiceType.setText(s.getType());
        etServiceDescription.setText(s.getDescription());
        etServiceDuration.setText(String.valueOf(s.getDuration()));
        etServicePrice.setText(String.valueOf(s.getPrice()));

        layoutServiceForm.setVisibility(View.VISIBLE);
        fabAddService.hide();
        btnSaveService.setText("Update");
    }

    private void hideForm() {
        layoutServiceForm.setVisibility(View.GONE);
        fabAddService.show();
        clearForm();
    }

    private void clearForm() {
        etServiceType.setText("");
        etServiceDescription.setText("");
        etServiceDuration.setText("");
        etServicePrice.setText("");
    }

    private void saveService() {
        String type = etServiceType.getText().toString().trim();
        String desc = etServiceDescription.getText().toString().trim();
        String durStr = etServiceDuration.getText().toString().trim();
        String priceStr = etServicePrice.getText().toString().trim();

        // ✅ هاي الثلاث لازم مش فاضيين (عشان PHP ما يرجع 422)
        if (Helpers.isEmpty(type)) {
            etServiceType.setError("Required");
            etServiceType.requestFocus();
            return;
        }
        if (Helpers.isEmpty(durStr)) {
            etServiceDuration.setError("Required");
            etServiceDuration.requestFocus();
            return;
        }
        if (Helpers.isEmpty(priceStr)) {
            etServicePrice.setError("Required");
            etServicePrice.requestFocus();
            return;
        }

        int duration;
        double price;

        try {
            duration = Integer.parseInt(durStr);
            if (duration <= 0) throw new NumberFormatException();
        } catch (Exception e) {
            etServiceDuration.setError("Invalid duration");
            etServiceDuration.requestFocus();
            return;
        }

        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException();
        } catch (Exception e) {
            etServicePrice.setError("Invalid price");
            etServicePrice.requestFocus();
            return;
        }

        showLoading(isEditMode ? "Updating..." : "Adding...");

        if (isEditMode && currentService != null) {
            currentService.setType(type);
            currentService.setDescription(desc);
            currentService.setDuration(duration);
            currentService.setPrice(price);

            serviceRepository.updateService(currentService.getServiceId(), currentService,
                    new ServiceRepository.OnServiceUpdatedListener() {
                        @Override
                        public void onServiceUpdated() {
                            if (isFinishing() || isDestroyed()) return;
                            hideLoading();
                            showToast("Updated");
                            hideForm();
                            loadServices();
                        }

                        @Override
                        public void onError(String error) {
                            if (isFinishing() || isDestroyed()) return;
                            hideLoading();
                            showError(error);
                        }
                    });

        } else {
            // ✅ مهم: اعمل set للحقول بدل ما تعتمد على constructor مش متأكد منه
            Service s = new Service();
            s.setType(type);
            s.setDescription(desc);
            s.setDuration(duration);
            s.setPrice(price);

            serviceRepository.addService(s, new ServiceRepository.OnServiceAddedListener() {
                @Override
                public void onServiceAdded(Service service) {
                    if (isFinishing() || isDestroyed()) return;
                    hideLoading();
                    showToast("Added");
                    hideForm();
                    loadServices();
                }

                @Override
                public void onError(String error) {
                    if (isFinishing() || isDestroyed()) return;
                    hideLoading();
                    showError(error);
                }
            });
        }
    }

    private void confirmDelete(Service s) {
        showConfirmation(
                "Delete Service",
                "Delete: " + s.getType() + " ?",
                () -> doDelete(s)
        );
    }

    private void doDelete(Service s) {
        showLoading("Deleting...");
        serviceRepository.deleteService(s.getServiceId(), new ServiceRepository.OnServiceDeletedListener() {
            @Override
            public void onServiceDeleted() {
                if (isFinishing() || isDestroyed()) return;
                hideLoading();
                showToast("Deleted");
                loadServices();
            }

            @Override
            public void onError(String error) {
                if (isFinishing() || isDestroyed()) return;
                hideLoading();
                showError(error);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
