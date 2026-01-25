package com.example.carwash.database;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Service;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.SharedPrefManager;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceRepository {

    private final Context context;

    public ServiceRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    // ✅ FIX: SharedPrefManager.getUserId() عندك String
    // نحوله لـ int هنا
    private int managerId() {
        try {
            String idStr = SharedPrefManager.getInstance(context).getUserId(); // String
            if (idStr == null) return 0;
            idStr = idStr.trim();
            if (idStr.isEmpty()) return 0;
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return 0;
        }
    }

    private String volleyBody(VolleyError error) {
        if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
            return new String(error.networkResponse.data, StandardCharsets.UTF_8);
        }
        return "";
    }

    public void getAllServices(OnServicesLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.GET,
                ApiConfig.GET_ALL_SERVICES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load services"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("services");
                        List<Service> list = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject s = arr.getJSONObject(i);
                                list.add(parseService(s));
                            }
                        }

                        listener.onServicesLoaded(list);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> {
                    String body = volleyBody(error);
                    listener.onError(body.isEmpty() ? "Network error" : body);
                }
        );

        VolleySingleton.getInstance(context).add(req);
    }

    public void addService(Service service, OnServiceAddedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.ADD_SERVICE,
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
                error -> {
                    String body = volleyBody(error);
                    listener.onError(body.isEmpty() ? "Network error" : body);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                int mid = managerId();
                Log.d("MID", "addService manager_id=" + mid);

                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(mid)); // ✅ required

                p.put("type", safe(service.getType()));
                p.put("description", safe(service.getDescription()));
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
                ApiConfig.UPDATE_SERVICE,
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
                error -> {
                    String body = volleyBody(error);
                    listener.onError(body.isEmpty() ? "Network error" : body);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                int mid = managerId();
                Log.d("MID", "updateService manager_id=" + mid);

                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(mid)); // ✅ required

                p.put("service_id", serviceId);
                p.put("type", safe(service.getType()));
                p.put("description", safe(service.getDescription()));
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
                ApiConfig.DELETE_SERVICE,
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
                error -> {
                    String body = volleyBody(error);
                    listener.onError(body.isEmpty() ? "Network error" : body);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                int mid = managerId();
                Log.d("MID", "deleteService manager_id=" + mid);

                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(mid)); // ✅ required
                p.put("service_id", serviceId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    private Service parseService(JSONObject s) {
        Service service = new Service();
        service.setServiceId(s.optString("service_id", ""));
        service.setType(s.optString("type", ""));
        service.setDescription(s.optString("description", ""));
        service.setDuration(s.optInt("duration", 0));
        service.setPrice(s.optDouble("price", 0.0));
        return service;
    }

    // Callbacks
    public interface OnServicesLoadedListener {
        void onServicesLoaded(List<Service> services);
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
