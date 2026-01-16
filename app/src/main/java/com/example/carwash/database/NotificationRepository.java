package com.example.carwash.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Notification;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepository {

    private final Context context;

    private final String GET_USER_NOTIFICATIONS = ApiConfig.BASE_URL + "get_user_notifications.php";
    private final String CREATE_NOTIFICATION = ApiConfig.BASE_URL + "create_notification.php";
    private final String MARK_READ = ApiConfig.BASE_URL + "mark_notification_read.php";

    public NotificationRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public void getUserNotifications(String userId, OnNotificationsLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                GET_USER_NOTIFICATIONS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load notifications"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("notifications");
                        List<Notification> notifications = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject n = arr.getJSONObject(i);
                                notifications.add(parseNotification(n));
                            }
                        }

                        listener.onNotificationsLoaded(notifications);

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

    public void createNotification(Notification notification, OnNotificationCreatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                CREATE_NOTIFICATION,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            int notificationId = obj.optInt("notification_id", -1);
                            if (notificationId != -1) {
                                notification.setNotificationId(String.valueOf(notificationId));
                            }
                            listener.onNotificationCreated(notification);
                        } else {
                            listener.onError(obj.optString("message", "Create notification failed"));
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
                p.put("user_id", notification.getUserId());
                p.put("title", notification.getTitle());
                p.put("message", notification.getMessage());
                p.put("type", notification.getType());

                // related booking ممكن يكون فاضي
                String related = notification.getRelatedBookingId();
                p.put("related_booking_id", related == null ? "" : related);

                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void markAsRead(String notificationId, OnNotificationMarkedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                MARK_READ,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onNotificationMarked();
                        } else {
                            listener.onError(obj.optString("message", "Mark as read failed"));
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
                p.put("notification_id", notificationId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    // ===== Helper: parse Notification from JSON =====
    private Notification parseNotification(JSONObject n) {
        Notification notification = new Notification();

        // عدّل أسماء الحقول إذا PHP بيرجع غير هيك
        notification.setNotificationId(n.optString("notification_id", ""));
        notification.setUserId(n.optString("user_id", ""));
        notification.setTitle(n.optString("title", ""));
        notification.setMessage(n.optString("message", ""));
        notification.setType(n.optString("type", ""));
        notification.setRelatedBookingId(n.optString("related_booking_id", ""));
        notification.setRead(n.optInt("is_read", 0) == 1);

        // إذا عندك created_at بالموديل
        // notification.setCreatedAt(n.optString("created_at", ""));

        return notification;
    }

    // ===== Callbacks =====
    public interface OnNotificationsLoadedListener {
        void onNotificationsLoaded(List<Notification> notifications);
        void onError(String error);
    }

    public interface OnNotificationCreatedListener {
        void onNotificationCreated(Notification notification);
        void onError(String error);
    }

    public interface OnNotificationMarkedListener {
        void onNotificationMarked();
        void onError(String error);
    }
}
