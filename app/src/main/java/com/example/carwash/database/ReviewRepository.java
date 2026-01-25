package com.example.carwash.database;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.carwash.models.Review;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.*;
import java.util.*;

public class ReviewRepository {

    private final Context context;

    public ReviewRepository(Context context) {
        this.context = context;
    }

    public interface OnReviewsLoadedListener {
        void onLoaded(List<Review> list);
        void onError(String error);
    }

    public void getAllReviews(OnReviewsLoadedListener listener) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                ApiConfig.GET_ALL_REVIEWS,
                null,
                response -> {
                    try {
                        List<Review> list = new ArrayList<>();
                        JSONArray arr = response.getJSONArray("bookings");

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);

                            if (o.isNull("rating")) continue;

                            list.add(new Review(
                                    o.getInt("booking_id"),
                                    o.getString("customer_name"),
                                    o.getString("service_type"),
                                    o.getInt("rating"),
                                    o.optString("review", ""),
                                    o.optString("completed_at", "")
                            ));
                        }
                        listener.onLoaded(list);
                    } catch (Exception e) {
                        listener.onError(e.getMessage());
                    }
                },
                error -> listener.onError(error.toString())
        );

        VolleySingleton.getInstance(context).add(req);
    }
}
