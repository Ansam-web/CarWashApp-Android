package com.example.carwash.activities.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.CarsAdapter;
import com.example.carwash.database.*;
import com.example.carwash.models.Car;
import com.example.carwash.utils.Constants;
import com.example.carwash.utils.Helpers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.List;

/**
 * CarsActivity.java
 * Manages user's cars - view list, add, edit, delete
 */
public class CarsActivity extends BaseActivity {

    // UI Components
    private RecyclerView recyclerCars;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddCar;
    private View layoutCarForm;

    // Form fields
    private EditText etBrand, etModel, etYear, etColor, etPlateNumber;
    private Spinner spinnerCarType;
    private Button btnSaveCar, btnCancelCar;

    // Data
    private CarsAdapter adapter;
    private CarRepository carRepository;
    private Car currentCar; // For editing
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        setupToolbar();
        initViews();
        setupRecyclerView();
        loadCars();
    }

    /**
     * Setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_cars);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        recyclerCars = findViewById(R.id.recyclerCars);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddCar = findViewById(R.id.fabAddCar);
        layoutCarForm = findViewById(R.id.layoutCarForm);

        // Form fields
        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        etYear = findViewById(R.id.etYear);
        etColor = findViewById(R.id.etColor);
        etPlateNumber = findViewById(R.id.etPlateNumber);
        spinnerCarType = findViewById(R.id.spinnerCarType);
        btnSaveCar = findViewById(R.id.btnSaveCar);
        btnCancelCar = findViewById(R.id.btnCancelCar);

        carRepository = new CarRepository(this);

        // Setup car type spinner
        setupCarTypeSpinner();

        // Listeners
        fabAddCar.setOnClickListener(v -> showAddCarForm());
        btnSaveCar.setOnClickListener(v -> saveCar());
        btnCancelCar.setOnClickListener(v -> hideCarForm());
    }

    /**
     * Setup car type spinner
     */
    private void setupCarTypeSpinner() {
        String[] carTypes = {"Sedan", "SUV", "Truck", "Hatchback"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, carTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarType.setAdapter(adapter);
    }

    /**
     * Setup RecyclerView
     */
    private void setupRecyclerView() {
        adapter = new CarsAdapter(this);
        recyclerCars.setLayoutManager(new LinearLayoutManager(this));
        recyclerCars.setAdapter(adapter);

        adapter.setOnCarClickListener(new CarsAdapter.OnCarClickListener() {
            @Override
            public void onCarClick(Car car) {
                // View car details
                showToast(car.getDisplayName());
            }

            @Override
            public void onCarEdit(Car car) {
                showEditCarForm(car);
            }

            @Override
            public void onCarDelete(Car car) {
                confirmDeleteCar(car);
            }
        });
    }

    /**
     * Load user's cars
     */
    private void loadCars() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerCars.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        carRepository.getUserCars(prefManager.getUserId(),
                new CarRepository.OnCarsLoadedListener() {
                    @Override
                    public void onCarsLoaded(List<Car> cars) {
                        progressBar.setVisibility(View.GONE);

                        if (cars.isEmpty()) {
                            tvEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            recyclerCars.setVisibility(View.VISIBLE);
                            adapter.setCarsList(cars);
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
     * Show add car form
     */
    private void showAddCarForm() {
        isEditMode = false;
        currentCar = null;
        clearForm();
        layoutCarForm.setVisibility(View.VISIBLE);
        fabAddCar.hide();
        btnSaveCar.setText(R.string.add);
    }

    /**
     * Show edit car form
     */
    private void showEditCarForm(Car car) {
        isEditMode = true;
        currentCar = car;
        fillForm(car);
        layoutCarForm.setVisibility(View.VISIBLE);
        fabAddCar.hide();
        btnSaveCar.setText(R.string.update);
    }

    /**
     * Hide car form
     */
    private void hideCarForm() {
        layoutCarForm.setVisibility(View.GONE);
        fabAddCar.show();
        clearForm();
    }

    /**
     * Clear form fields
     */
    private void clearForm() {
        etBrand.setText("");
        etModel.setText("");
        etYear.setText("");
        etColor.setText("");
        etPlateNumber.setText("");
        spinnerCarType.setSelection(0);
    }

    /**
     * Fill form with car data
     */
    private void fillForm(Car car) {
        etBrand.setText(car.getBrand());
        etModel.setText(car.getModel());
        etYear.setText(String.valueOf(car.getYear()));
        etColor.setText(car.getColor());
        etPlateNumber.setText(car.getPlateNumber());

        // Set spinner selection
        String carType = car.getCarType();
        String[] types = {"sedan", "suv", "truck", "hatchback"};
        for (int i = 0; i < types.length; i++) {
            if (types[i].equalsIgnoreCase(carType)) {
                spinnerCarType.setSelection(i);
                break;
            }
        }
    }

    /**
     * Save car (add or update)
     */
    private void saveCar() {
        // Validation
        if (!validateCar()) {
            return;
        }

        showLoading(isEditMode ? "Updating car..." : "Adding car...");

        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        int year = Integer.parseInt(etYear.getText().toString().trim());
        String color = etColor.getText().toString().trim();
        String plateNumber = etPlateNumber.getText().toString().trim();
        String carType = spinnerCarType.getSelectedItem().toString().toLowerCase();

        if (isEditMode) {
            // Update existing car
            currentCar.setBrand(brand);
            currentCar.setModel(model);
            currentCar.setYear(year);
            currentCar.setColor(color);
            currentCar.setPlateNumber(plateNumber);
            currentCar.setCarType(carType);

            carRepository.updateCar(currentCar.getCarId(), currentCar,
                    new CarRepository.OnCarUpdatedListener() {
                        @Override
                        public void onCarUpdated() {
                            hideLoading();
                            showToast("Car updated successfully");
                            hideCarForm();
                            loadCars();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError(error);
                        }
                    });
        } else {
            // Add new car
            Car newCar = new Car(
                    prefManager.getUserId(),
                    brand, model, year, color, plateNumber, carType
            );

            carRepository.addCar(newCar, new CarRepository.OnCarAddedListener() {
                @Override
                public void onCarAdded(Car car) {
                    hideLoading();
                    showToast(getString(R.string.car_added));
                    hideCarForm();
                    loadCars();
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
     * Validate car inputs
     */
    private boolean validateCar() {
        if (Helpers.isEmpty(etBrand.getText().toString())) {
            etBrand.setError(Constants.ERROR_EMPTY_FIELD);
            etBrand.requestFocus();
            return false;
        }

        if (Helpers.isEmpty(etModel.getText().toString())) {
            etModel.setError(Constants.ERROR_EMPTY_FIELD);
            etModel.requestFocus();
            return false;
        }

        String yearStr = etYear.getText().toString().trim();
        if (Helpers.isEmpty(yearStr)) {
            etYear.setError(Constants.ERROR_EMPTY_FIELD);
            etYear.requestFocus();
            return false;
        }

        try {
            int year = Integer.parseInt(yearStr);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) {
                etYear.setError("Invalid year");
                etYear.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etYear.setError("Invalid year");
            etYear.requestFocus();
            return false;
        }

        if (Helpers.isEmpty(etColor.getText().toString())) {
            etColor.setError(Constants.ERROR_EMPTY_FIELD);
            etColor.requestFocus();
            return false;
        }

        if (Helpers.isEmpty(etPlateNumber.getText().toString())) {
            etPlateNumber.setError(Constants.ERROR_EMPTY_FIELD);
            etPlateNumber.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Confirm delete car
     */
    private void confirmDeleteCar(Car car) {
        showConfirmation(
                "Delete Car",
                "Are you sure you want to delete " + car.getDisplayName() + "?",
                () -> deleteCar(car)
        );
    }

    /**
     * Delete car
     */
    private void deleteCar(Car car) {
        showLoading("Deleting car...");

        carRepository.deleteCar(car.getCarId(), new CarRepository.OnCarDeletedListener() {
            @Override
            public void onCarDeleted() {
                hideLoading();
                showToast(getString(R.string.car_deleted));
                loadCars();
            }

            @Override
            public void onError(String error) {
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