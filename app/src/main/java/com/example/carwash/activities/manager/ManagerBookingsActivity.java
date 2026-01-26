package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.BookingManagerAdapter;
import com.example.carwash.models.Booking;
import com.example.carwash.models.Team;
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

public class ManagerBookingsActivity extends BaseActivity implements BookingManagerAdapter.OnAssignClickListener {

    private RecyclerView recycler;
    private ProgressBar progressBar;
    private View cardEmpty;

    private BookingManagerAdapter adapter;

    private final List<Booking> bookings = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();

    private String managerId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_bookings_manage);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Bookings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recycler = findViewById(R.id.recyclerBookings);
        progressBar = findViewById(R.id.progressBar);
        cardEmpty = findViewById(R.id.cardEmpty);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingManagerAdapter(bookings, teams, this);
        recycler.setAdapter(adapter);

        managerId = SharedPrefManager.getInstance(this).getUserId();

        loadTeamsThenBookings();
    }

    private void loadTeamsThenBookings() {
        fetchTeams(() -> fetchBookings());
    }

    private void fetchTeams(Runnable onDone) {
        progressBar.setVisibility(View.VISIBLE);
        showEmpty(false);

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.GET_ALL_TEAMS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        // حتى لو fail، نكمل bookings
                        if (!obj.optBoolean("success", false)) {
                            teams.clear();
                            adapter.setTeams(teams);
                            onDone.run();
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("teams");
                        teams.clear();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject t = arr.getJSONObject(i);

                                Team team = new Team();
                                team.setTeamId(String.valueOf(t.optInt("team_id", 0)));
                                team.setName(t.optString("name", ""));

                                // لو عندك car fields في DB وبدك تعرضها لاحقًا:
                                team.setCarNumber(t.optString("car_number", ""));
                                team.setCarPlate(t.optString("car_plate", ""));

                                teams.add(team);
                            }
                        }

                        adapter.setTeams(teams);
                        onDone.run();

                    } catch (Exception e) {
                        teams.clear();
                        adapter.setTeams(teams);
                        onDone.run();
                    }
                },
                error -> {
                    teams.clear();
                    adapter.setTeams(teams);
                    onDone.run();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(managerId));
                return p;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(15000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).add(req);
    }

    private void fetchBookings() {
        progressBar.setVisibility(View.VISIBLE);
        showEmpty(false);

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.GET_ALL_BOOKINGS,
                response -> {
                    progressBar.setVisibility(View.GONE);

                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success", false)) {
                            showError(obj.optString("message", "Failed to load bookings"));
                            showEmpty(true);
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("bookings");
                        bookings.clear();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject r = arr.getJSONObject(i);

                                Booking bk = new Booking();
                                bk.setBookingId(String.valueOf(r.optInt("booking_id", 0)));
                                bk.setStatus(r.optString("status", "pending"));

                                bk.setBookingDate(r.optString("booking_date", ""));
                                bk.setBookingTime(r.optString("booking_time", ""));
                                bk.setLocationAddress(r.optString("location_address", ""));
                                bk.setTotalPrice(r.optDouble("total_price", 0.0));

                                // ✅ display fields (JOIN)
                                bk.setCustomerName(r.optString("customer_name", ""));
                                bk.setServiceType(r.optString("service_type", ""));
                                bk.setTeamName(r.isNull("team_name") ? "" : r.optString("team_name", ""));

                                // team_id nullable
                                if (r.isNull("team_id")) bk.setTeamId(null);
                                else bk.setTeamId(String.valueOf(r.optInt("team_id", 0)));

                                // rating nullable
                                if (r.isNull("rating")) bk.setRating(null);
                                else bk.setRating(r.optInt("rating", 0));

                                bk.setReview(r.isNull("review") ? null : r.optString("review", null));

                                bookings.add(bk);
                            }
                        }

                        adapter.notifyDataSetChanged();
                        showEmpty(bookings.isEmpty());

                    } catch (Exception e) {
                        showError("Response error");
                        showEmpty(true);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("BOOKINGS_API", "Status=" + error.networkResponse.statusCode + " Body=" + body);
                        showError(body);
                    } else {
                        showError("Network error");
                    }
                    showEmpty(true);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(managerId));
                return p;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(15000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    @Override
    public void onAssignClicked(Booking booking, Team selectedTeam) {
        if (selectedTeam == null || selectedTeam.getTeamId() == null || selectedTeam.getTeamId().trim().isEmpty()) {
            showToast("Choose a team");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.ASSIGN_TEAM,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {

                            booking.setStatus("assigned");
                            booking.setTeamId(selectedTeam.getTeamId());
                            booking.setTeamName(selectedTeam.getName());

                            adapter.notifyDataSetChanged();
                            showToast("Assigned!");
                        } else {
                            showError(obj.optString("message", "Assign failed"));
                        }
                    } catch (Exception e) {
                        showError("Response error");
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        showError(body);
                    } else {
                        showError("Assign failed");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", String.valueOf(managerId));
                p.put("booking_id", booking.getBookingId());
                p.put("team_id", selectedTeam.getTeamId()); // String but PHP casts to int ✅
                return p;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(15000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).add(req);
    }
}
