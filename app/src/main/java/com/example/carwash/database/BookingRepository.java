package com.example.carwash.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Booking;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRepository {

    private final Context context;

    // Endpoints
    private final String CREATE_BOOKING = ApiConfig.BASE_URL + "create_booking.php";
    private final String GET_USER_BOOKINGS = ApiConfig.BASE_URL + "get_user_bookings.php";
    private final String GET_BOOKING_BY_ID = ApiConfig.BASE_URL + "get_booking_by_id.php";
    private final String UPDATE_STATUS = ApiConfig.BASE_URL + "update_booking_status.php";
    private final String ASSIGN_TEAM = ApiConfig.BASE_URL + "assign_team.php";
    private final String RATE_BOOKING = ApiConfig.BASE_URL + "rate_booking.php";
    private final String CANCEL_BOOKING = ApiConfig.BASE_URL + "cancel_booking.php";

    public BookingRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public void createBooking(Booking booking, OnBookingCreatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                CREATE_BOOKING,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            int bookingId = obj.getInt("booking_id"); // لازم يرجعها الـ PHP
                            booking.setBookingId(String.valueOf(bookingId));
                            listener.onBookingCreated(booking);
                        } else {
                            listener.onError(obj.optString("message", "Create booking failed"));
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
                p.put("user_id", booking.getUserId());
                p.put("car_id", booking.getCarId());
                p.put("service_id", booking.getServiceId());
                p.put("booking_date", booking.getBookingDate());
                p.put("booking_time", booking.getBookingTime());
                p.put("location_address", booking.getLocationAddress());
                p.put("total_price", String.valueOf(booking.getTotalPrice()));
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void getUserBookings(String userId, OnBookingsLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                GET_USER_BOOKINGS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load bookings"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("bookings");
                        List<Booking> bookings = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject b = arr.getJSONObject(i);
                                bookings.add(parseBooking(b));
                            }
                        }

                        listener.onBookingsLoaded(bookings);

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

    public void getBookingById(String bookingId, OnBookingLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                GET_BOOKING_BY_ID,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Booking not found"));
                            return;
                        }

                        JSONObject b = obj.getJSONObject("booking");
                        Booking booking = parseBooking(b);
                        listener.onBookingLoaded(booking);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("booking_id", bookingId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void updateBookingStatus(String bookingId, String newStatus, OnBookingStatusUpdatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                UPDATE_STATUS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onStatusUpdated();
                        } else {
                            listener.onError(obj.optString("message", "Update failed"));
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
                p.put("booking_id", bookingId);
                p.put("status", newStatus);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void assignTeam(String bookingId, String teamId, OnTeamAssignedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ASSIGN_TEAM,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onTeamAssigned();
                        } else {
                            listener.onError(obj.optString("message", "Assign team failed"));
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
                p.put("booking_id", bookingId);
                p.put("team_id", teamId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void rateBooking(String bookingId, int rating, String review, OnBookingRatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                RATE_BOOKING,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onBookingRated();
                        } else {
                            listener.onError(obj.optString("message", "Rate booking failed"));
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
                p.put("booking_id", bookingId);
                p.put("rating", String.valueOf(rating));
                p.put("review", review == null ? "" : review);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void cancelBooking(String bookingId, OnBookingCancelledListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                CANCEL_BOOKING,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onBookingCancelled();
                        } else {
                            listener.onError(obj.optString("message", "Cancel failed"));
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
                p.put("booking_id", bookingId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    // ===== Helper: parse Booking from JSON =====
    private Booking parseBooking(JSONObject b) {
        Booking booking = new Booking();

        // NOTE: عدّل أسماء الحقول هنا إذا PHP بيرجع أسماء مختلفة
        booking.setBookingId(b.optString("booking_id", ""));
        booking.setUserId(b.optString("user_id", ""));
        booking.setCarId(b.optString("car_id", ""));
        booking.setServiceId(b.optString("service_id", ""));
        booking.setTeamId(b.optString("team_id", ""));
        booking.setStatus(b.optString("status", "pending"));
        booking.setBookingDate(b.optString("booking_date", ""));
        booking.setBookingTime(b.optString("booking_time", ""));
        booking.setLocationAddress(b.optString("location_address", ""));
        booking.setTotalPrice(b.optDouble("total_price", 0.0));
        booking.setRating(b.optInt("rating", 0));
        booking.setReview(b.optString("review", ""));

        return booking;
    }

    // ===== Callbacks (نفس فكرتك القديمة) =====
    public interface OnBookingCreatedListener {
        void onBookingCreated(Booking booking);
        void onError(String error);
    }

    public interface OnBookingsLoadedListener {
        void onBookingsLoaded(List<Booking> bookings);
        void onError(String error);
    }

    public interface OnBookingLoadedListener {
        void onBookingLoaded(Booking booking);
        void onError(String error);
    }

    public interface OnBookingStatusUpdatedListener {
        void onStatusUpdated();
        void onError(String error);
    }

    public interface OnTeamAssignedListener {
        void onTeamAssigned();
        void onError(String error);
    }

    public interface OnBookingRatedListener {
        void onBookingRated();
        void onError(String error);
    }

    public interface OnBookingCancelledListener {
        void onBookingCancelled();
        void onError(String error);
    }
}
