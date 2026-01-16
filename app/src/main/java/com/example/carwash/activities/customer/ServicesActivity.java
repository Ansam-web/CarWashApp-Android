package com.example.carwash.activities.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.carwash.utils.Constants;
import java.util.List;

/**
 * ServicesActivity.java
 * Displays list of available car wash services
 * Allows customer to select a service to book
 */
public class ServicesActivity extends BaseActivity {

    private RecyclerView recyclerServices;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private ServicesAdapter adapter;
    private ServiceRepository serviceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

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
            getSupportActionBar().setTitle(R.string.services);
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

        serviceRepository = new ServiceRepository(this);
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new ServicesAdapter(this);
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setAdapter(adapter);

        // Handle service click
        adapter.setOnServiceClickListener(service -> {
            openServiceDetails(service);
        });
    }

    /**
     * Load services from Firestore
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
                tvEmptyState.setText("Error: " + error);
                showError(error);
            }
        });
    }

    /**
     * Open service details / booking screen
     */
    private void openServiceDetails(Service service) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra(Constants.EXTRA_MODE, Constants.MODE_ADD);
        intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getServiceId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}