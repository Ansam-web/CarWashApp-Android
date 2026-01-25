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
import com.example.carwash.adapters.CustomerServicesAdapter;
import com.example.carwash.database.ServiceRepository;
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

    private CustomerServicesAdapter adapter;
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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.services);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerServices = findViewById(R.id.recyclerServices);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        serviceRepository = new ServiceRepository(this);
    }

    private void setupRecyclerView() {
        adapter = new CustomerServicesAdapter();
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerServices.setAdapter(adapter);

        // Handle service click
        adapter.setListener(service -> openServiceDetails(service));
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
                    tvEmptyState.setText("No services yet");
                } else {
                    recyclerServices.setVisibility(View.VISIBLE);
                    adapter.setData(services);
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
