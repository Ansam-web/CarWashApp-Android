package com.example.carwash.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Service;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRepository {

    private final Context context;

    private final String GET_ALL_SERVICES = ApiConfig.BASE_URL + "get_all_services.php";
    private final String GET_SERVICE_BY_ID = ApiConfig.BASE_URL + "get_service_by_id.php";
    private final String ADD_SERVICE = ApiConfig.BASE_URL + "add_service.php";
    private final String UPDATE_SERVICE = ApiConfig.BASE_URL + "update_service.php";
    private final String DELETE_SERVICE = ApiConfig.BASE_URL + "delete_service.php";

    public ServiceRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public void getAllServices(OnServicesLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.GET,
                GET_ALL_SERVICES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load services"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("services");
                        List<Service> services = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject s = arr.getJSONObject(i);
                                services.add(parseService(s));
                            }
                        }

                        listener.onServicesLoaded(services);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        );

        VolleySingleton.getInstance(context).add(req);
    }

    public void getServiceById(String serviceId, OnServiceLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                GET_SERVICE_BY_ID,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Service not found"));
                            return;
                        }

                        JSONObject s = obj.getJSONObject("service");
                        Service service = parseService(s);
                        listener.onServiceLoaded(service);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("service_id", serviceId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void addService(Service service, OnServiceAddedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ADD_SERVICE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            int id = obj.optInt("service_id", -1);
                            if (id != -1) service.setServiceId(String.valueOf(id));
                            listener.onServiceAdded(service);
                        } else {
                            listener.onError(obj.optString("message", "Add service failed"));
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
                p.put("type", service.getType());
                p.put("description", service.getDescription());
                p.put("duration", String.valueOf(service.getDuration()));
                p.put("price", String.valueOf(service.getPrice()));
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void updateService(String serviceId, Service service, OnServiceUpdatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                UPDATE_SERVICE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onServiceUpdated();
                        } else {
                            listener.onError(obj.optString("message", "Update service failed"));
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
                p.put("service_id", serviceId);
                p.put("type", service.getType());
                p.put("description", service.getDescription());
                p.put("duration", String.valueOf(service.getDuration()));
                p.put("price", String.valueOf(service.getPrice()));
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void deleteService(String serviceId, OnServiceDeletedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                DELETE_SERVICE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onServiceDeleted();
                        } else {
                            listener.onError(obj.optString("message", "Delete service failed"));
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
                p.put("service_id", serviceId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    // ===== Helper: parse Service from JSON =====
    private Service parseService(JSONObject s) {
        Service service = new Service();

        // عدّل أسماء الحقول إذا PHP بيرجع غير هيك
        service.setServiceId(s.optString("service_id", ""));
        service.setType(s.optString("type", ""));
        service.setDescription(s.optString("description", ""));
        service.setDuration(s.optInt("duration", 0));
        service.setPrice(s.optDouble("price", 0.0));

        // لو عندك isActive بالموديل
        // service.setActive(s.optInt("is_active", 1) == 1);

        return service;
    }

    // ===== Callbacks =====
    public interface OnServicesLoadedListener {
        void onServicesLoaded(List<Service> services);
        void onError(String error);
    }

    public interface OnServiceLoadedListener {
        void onServiceLoaded(Service service);
        void onError(String error);
    }

    public interface OnServiceAddedListener {
        void onServiceAdded(Service service);
        void onError(String error);
    }

    public interface OnServiceUpdatedListener {
        void onServiceUpdated();
        void onError(String error);
    }

    public interface OnServiceDeletedListener {
        void onServiceDeleted();
        void onError(String error);
    }
}
