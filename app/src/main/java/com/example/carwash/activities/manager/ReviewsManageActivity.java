package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.ReviewsAdapter;
import com.example.carwash.models.Review;
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

public class ReviewsManageActivity extends BaseActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView tvEmpty;
    private View cardEmpty;

    private ReviewsAdapter adapter;
    private final List<Review> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_reviews_manage);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Customer Reviews");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recycler = findViewById(R.id.recyclerReviews);
        progress = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmptyState);
        cardEmpty = findViewById(R.id.cardEmpty);

        adapter = new ReviewsAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        loadReviews();
    }

    private void loadReviews() {
        progress.setVisibility(View.VISIBLE);
        showEmpty(false);

        final String managerId = SharedPrefManager.getInstance(this).getUserId();

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.GET_ALL_REVIEWS,
                response -> {
                    progress.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            showError(obj.optString("message", "Failed to load reviews"));
                            showEmpty(true);
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("reviews");
                        list.clear();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject r = arr.getJSONObject(i);

                                list.add(new Review(
                                        r.optInt("booking_id", 0),
                                        r.optString("customer_name", ""),
                                        r.optString("service_type", ""),
                                        r.optInt("rating", 0),
                                        r.optString("review", ""),
                                        r.optString("booking_date", "")
                                ));
                            }
                        }

                        if (list.isEmpty()) {
                            showEmpty(true);
                        } else {
                            adapter.setList(list);
                            showEmpty(false);
                        }

                    } catch (Exception e) {
                        showError("Response error");
                        showEmpty(true);
                    }
                },
                error -> {
                    progress.setVisibility(View.GONE);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("REVIEWS_API", body);
                        showError(body);
                    } else {
                        showError("Network error");
                    }
                    showEmpty(true);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("manager_id", String.valueOf(managerId));
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        VolleySingleton.getInstance(this).add(req);
    }

    private void showEmpty(boolean empty) {
        cardEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
