package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.carwash.R;
import com.example.carwash.utils.ApiConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ManagerStatsActivity extends AppCompatActivity {

    private TextView tvTotal, tvCompleted, tvPending, tvRevenue, tvAvgRating;
    private ProgressBar progress;

    // مؤقتاً: حطي manager_id ثابت للتجربة (بعدها بنجيبها من Session/UserManager)
    private int managerId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_stats);

        tvTotal = findViewById(R.id.tvTotalBookings);
        tvCompleted = findViewById(R.id.tvCompletedBookings);
        tvPending = findViewById(R.id.tvPendingBookings);
        tvRevenue = findViewById(R.id.tvTotalRevenue);
        tvAvgRating = findViewById(R.id.tvAvgRating);
        progress = findViewById(R.id.progress);

        loadStats();
    }

    private void loadStats() {
        progress.setVisibility(View.VISIBLE);

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.MANAGER_STATS,
                response -> {
                    progress.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success")) {
                            Toast.makeText(this, obj.optString("message","Error"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject stats = obj.getJSONObject("stats");
                        tvTotal.setText(String.valueOf(stats.optInt("total_bookings")));
                        tvCompleted.setText(String.valueOf(stats.optInt("completed_bookings")));
                        tvPending.setText(String.valueOf(stats.optInt("pending_bookings")));
                        tvRevenue.setText(String.valueOf(stats.optDouble("total_revenue")));
                        tvAvgRating.setText(String.valueOf(stats.optDouble("avg_rating")));

                    } catch (Exception e) {
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(managerId));
                return p;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }
}
