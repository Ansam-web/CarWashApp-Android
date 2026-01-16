package com.example.carwash.activities.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.BookingsAdapter;
import com.example.carwash.database.*;
import com.example.carwash.models.Booking;
import com.example.carwash.models.Service;
import com.example.carwash.models.Car;
import com.example.carwash.utils.Constants;
import com.example.carwash.utils.Helpers;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingActivity.java
 * Handles both:
 * 1. Creating new bookings (MODE_ADD)
 * 2. Viewing booking list (MODE_VIEW)
 * 3. Viewing booking details (MODE_EDIT)
 */
public class BookingActivity extends BaseActivity {

    // UI Components
    private View layoutBookingForm, layoutBookingsList;
    private RecyclerView recyclerBookings;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    // Form fields
    private Spinner spinnerCars, spinnerServices;
    private EditText etBookingDate, etBookingTime, etLocation;
    private TextView tvTotalPrice;
    private Button btnConfirmBooking;

    // Data
    private BookingsAdapter adapter;
    private BookingRepository bookingRepository;
    private CarRepository carRepository;
    private ServiceRepository serviceRepository;

    private String currentMode;
    private String selectedServiceId;
    private Service selectedService;
    private List<Car> userCars;
    private List<Service> availableServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Get mode from intent
        currentMode = getIntent().getStringExtra(Constants.EXTRA_MODE);
        if (currentMode == null) {
            currentMode = Constants.MODE_VIEW;
        }

        selectedServiceId = getIntent().getStringExtra(Constants.EXTRA_SERVICE_ID);

        setupToolbar();
        initViews();
        initRepositories();

        if (currentMode.equals(Constants.MODE_ADD)) {
            showBookingForm();
            loadFormData();
        } else {
            showBookingsList();
            loadBookings();
        }
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            if (currentMode.equals(Constants.MODE_ADD)) {
                getSupportActionBar().setTitle(R.string.book_service);
            } else {
                getSupportActionBar().setTitle(R.string.my_bookings);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        layoutBookingForm = findViewById(R.id.layoutBookingForm);
        layoutBookingsList = findViewById(R.id.layoutBookingsList);
        recyclerBookings = findViewById(R.id.recyclerBookings);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        // Form fields
        spinnerCars = findViewById(R.id.spinnerCars);
        spinnerServices = findViewById(R.id.spinnerServices);
        etBookingDate = findViewById(R.id.etBookingDate);
        etBookingTime = findViewById(R.id.etBookingTime);
        etLocation = findViewById(R.id.etLocation);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        // Date and Time pickers
        etBookingDate.setOnClickListener(v -> showDatePicker());
        etBookingTime.setOnClickListener(v -> showTimePicker());

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    /**
     * Initialize repositories
     */
    private void initRepositories() {
        bookingRepository = new BookingRepository(this);
        carRepository = new CarRepository(this);
        serviceRepository = new ServiceRepository(this);
    }

    /**
     * Show booking form
     */
    private void showBookingForm() {
        layoutBookingForm.setVisibility(View.VISIBLE);
        layoutBookingsList.setVisibility(View.GONE);
    }

    /**
     * Show bookings list
     */
    private void showBookingsList() {
        layoutBookingForm.setVisibility(View.GONE);
        layoutBookingsList.setVisibility(View.VISIBLE);
        setupRecyclerView();
    }

    /**
     * Setup RecyclerView for bookings list
     */
    private void setupRecyclerView() {
        adapter = new BookingsAdapter(this);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerBookings.setAdapter(adapter);

        adapter.setOnBookingClickListener(booking -> {
            // TODO: Show booking details
            showToast("Booking ID: " + booking.getBookingId());
        });
    }

    /**
     * Load form data (cars and services)
     */
    private void loadFormData() {
        showLoading("Loading...");

        // Load user's cars
        carRepository.getUserCars(prefManager.getUserId(),
                new CarRepository.OnCarsLoadedListener() {
                    @Override
                    public void onCarsLoaded(List<Car> cars) {
                        userCars = cars;
                        populateCarsSpinner();

                        // Load services
                        loadServices();
                    }

                    @Override
                    public void onError(String error) {
                        hideLoading();
                        showError(error);
                    }
                });
    }

    /**
     * Load services
     */
    private void loadServices() {
        serviceRepository.getAllServices(new ServiceRepository.OnServicesLoadedListener() {
            @Override
            public void onServicesLoaded(List<Service> services) {
                hideLoading();
                availableServices = services;
                populateServicesSpinner();

                // Pre-select service if provided
                if (selectedServiceId != null) {
                    preselectService();
                }
            }

            @Override
            public void onError(String error) {
                hideLoading();
                showError(error);
            }
        });
    }

    /**
     * Populate cars spinner
     */
    private void populateCarsSpinner() {
        if (userCars == null || userCars.isEmpty()) {
            showError("Please add a car first");
            finish();
            return;
        }

        List<String> carNames = new ArrayList<>();
        for (Car car : userCars) {
            carNames.add(car.getDisplayName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, carNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCars.setAdapter(adapter);
    }

    /**
     * Populate services spinner
     */
    private void populateServicesSpinner() {
        List<String> serviceNames = new ArrayList<>();
        for (Service service : availableServices) {
            serviceNames.add(service.getType() + " - " +
                    Helpers.formatPrice(service.getPrice()));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setAdapter(adapter);

        // Update price when service selected
        spinnerServices.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view,
                                       int position, long id) {
                updateTotalPrice(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    /**
     * Pre-select service if provided
     */
    private void preselectService() {
        for (int i = 0; i < availableServices.size(); i++) {
            if (availableServices.get(i).getServiceId().equals(selectedServiceId)) {
                spinnerServices.setSelection(i);
                break;
            }
        }
    }

    /**
     * Update total price
     */
    private void updateTotalPrice(int position) {
        if (availableServices != null && position < availableServices.size()) {
            selectedService = availableServices.get(position);
            tvTotalPrice.setText("Total: " +
                    Helpers.formatPrice(selectedService.getPrice()));
        }
    }

    /**
     * Show date picker
     */
    private void showDatePicker() {
        Helpers.showDatePicker(this, date -> {
            etBookingDate.setText(date);
        });
    }

    /**
     * Show time picker
     */
    private void showTimePicker() {
        Helpers.showTimePicker(this, time -> {
            etBookingTime.setText(time);
        });
    }

    /**
     * Confirm booking
     */
    private void confirmBooking() {
        // Validation
        if (!validateBooking()) {
            return;
        }

        showLoading("Creating booking...");

        // Get selected car
        int carPosition = spinnerCars.getSelectedItemPosition();
        Car selectedCar = userCars.get(carPosition);

        // Get selected service
        int servicePosition = spinnerServices.getSelectedItemPosition();
        selectedService = availableServices.get(servicePosition);

        // Create booking
        String date = etBookingDate.getText().toString();
        String time = etBookingTime.getText().toString();
        String location = etLocation.getText().toString();

        Booking booking = new Booking(
                prefManager.getUserId(),
                selectedCar.getCarId(),
                selectedService.getServiceId(),
                date,
                time,
                location,
                selectedService.getPrice()
        );

        bookingRepository.createBooking(booking,
                new BookingRepository.OnBookingCreatedListener() {
                    @Override
                    public void onBookingCreated(Booking createdBooking) {
                        hideLoading();
                        showToast(getString(R.string.booking_created));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        hideLoading();
                        showError(error);
                    }
                });
    }

    /**
     * Validate booking inputs
     */
    private boolean validateBooking() {
        if (Helpers.isEmpty(etBookingDate.getText().toString())) {
            showToast("Please select date");
            return false;
        }

        if (Helpers.isEmpty(etBookingTime.getText().toString())) {
            showToast("Please select time");
            return false;
        }

        if (Helpers.isEmpty(etLocation.getText().toString())) {
            etLocation.setError(Constants.ERROR_EMPTY_FIELD);
            etLocation.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Load user bookings
     */
    private void loadBookings() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerBookings.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        bookingRepository.getUserBookings(prefManager.getUserId(),
                new BookingRepository.OnBookingsLoadedListener() {
                    @Override
                    public void onBookingsLoaded(List<Booking> bookings) {
                        progressBar.setVisibility(View.GONE);

                        if (bookings.isEmpty()) {
                            tvEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            recyclerBookings.setVisibility(View.VISIBLE);
                            adapter.setBookingsList(bookings);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}