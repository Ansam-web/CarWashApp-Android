package com.example.carwash.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Car;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepository {

    private final Context context;

    private final String GET_USER_CARS = ApiConfig.BASE_URL + "get_user_cars.php";
    private final String ADD_CAR = ApiConfig.BASE_URL + "add_car.php";
    private final String UPDATE_CAR = ApiConfig.BASE_URL + "update_car.php";
    private final String DELETE_CAR = ApiConfig.BASE_URL + "delete_car.php";

    public CarRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public void getUserCars(String userId, OnCarsLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                GET_USER_CARS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load cars"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("cars");
                        List<Car> cars = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject c = arr.getJSONObject(i);
                                cars.add(parseCar(c));
                            }
                        }

                        listener.onCarsLoaded(cars);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("user_id", userId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void addCar(Car car, OnCarAddedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ADD_CAR,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            int carId = obj.optInt("car_id", -1);
                            if (carId != -1) car.setCarId(String.valueOf(carId));
                            listener.onCarAdded(car);
                        } else {
                            listener.onError(obj.optString("message", "Add car failed"));
                        }

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("user_id", car.getUserId());
                p.put("brand", car.getBrand());
                p.put("model", car.getModel());
                p.put("year", String.valueOf(car.getYear()));
                p.put("color", car.getColor());
                p.put("plate_number", car.getPlateNumber());
                p.put("car_type", car.getCarType());
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void updateCar(String carId, Car car, OnCarUpdatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                UPDATE_CAR,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onCarUpdated();
                        } else {
                            listener.onError(obj.optString("message", "Update car failed"));
                        }

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("car_id", carId);
                p.put("brand", car.getBrand());
                p.put("model", car.getModel());
                p.put("year", String.valueOf(car.getYear()));
                p.put("color", car.getColor());
                p.put("plate_number", car.getPlateNumber());
                p.put("car_type", car.getCarType());
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void deleteCar(String carId, OnCarDeletedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                DELETE_CAR,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onCarDeleted();
                        } else {
                            listener.onError(obj.optString("message", "Delete car failed"));
                        }

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("car_id", carId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    // ===== Helper: parse Car from JSON =====
    private Car parseCar(JSONObject c) {
        Car car = new Car();

        // عدّل أسماء الحقول إذا PHP بيرجع غير هيك
        car.setCarId(c.optString("car_id", ""));
        car.setUserId(c.optString("user_id", ""));
        car.setBrand(c.optString("brand", ""));
        car.setModel(c.optString("model", ""));
        car.setYear(c.optInt("year", 0));
        car.setColor(c.optString("color", ""));
        car.setPlateNumber(c.optString("plate_number", ""));
        car.setCarType(c.optString("car_type", ""));

        return car;
    }

    // ===== Callbacks (زي كودك القديم) =====
    public interface OnCarsLoadedListener {
        void onCarsLoaded(List<Car> cars);
        void onError(String error);
    }

    public interface OnCarAddedListener {
        void onCarAdded(Car car);
        void onError(String error);
    }

    public interface OnCarUpdatedListener {
        void onCarUpdated();
        void onError(String error);
    }

    public interface OnCarDeletedListener {
        void onCarDeleted();
        void onError(String error);
    }
}
