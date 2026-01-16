package com.example.carwash.database;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Team;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamRepository {

    private final Context context;

    private final String GET_ALL_TEAMS = ApiConfig.BASE_URL + "get_all_teams.php";
    private final String GET_AVAILABLE_TEAMS = ApiConfig.BASE_URL + "get_available_teams.php";
    private final String ADD_TEAM = ApiConfig.BASE_URL + "add_team.php";
    private final String UPDATE_TEAM = ApiConfig.BASE_URL + "update_team.php";
    private final String DELETE_TEAM = ApiConfig.BASE_URL + "delete_team.php";

    public TeamRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    public void getAllTeams(OnTeamsLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.GET,
                GET_ALL_TEAMS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load teams"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("teams");
                        List<Team> teams = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject t = arr.getJSONObject(i);
                                teams.add(parseTeam(t));
                            }
                        }

                        listener.onTeamsLoaded(teams);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        );

        VolleySingleton.getInstance(context).add(req);
    }

    public void getAvailableTeams(OnTeamsLoadedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.GET,
                GET_AVAILABLE_TEAMS,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load teams"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("teams");
                        List<Team> teams = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject t = arr.getJSONObject(i);
                                teams.add(parseTeam(t));
                            }
                        }

                        listener.onTeamsLoaded(teams);

                    } catch (Exception e) {
                        listener.onError("Parse error");
                    }
                },
                error -> listener.onError("Network error")
        );

        VolleySingleton.getInstance(context).add(req);
    }

    public void addTeam(Team team, OnTeamAddedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                ADD_TEAM,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            int id = obj.optInt("team_id", -1);
                            if (id != -1) team.setTeamId(String.valueOf(id));
                            listener.onTeamAdded(team);
                        } else {
                            listener.onError(obj.optString("message", "Add team failed"));
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
                p.put("name", team.getName());
                p.put("car_number", team.getCarNumber());
                p.put("car_plate", team.getCarPlate());
                p.put("available", "1"); // افتراضي متاح
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void updateTeam(String teamId, Team team, OnTeamUpdatedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                UPDATE_TEAM,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onTeamUpdated();
                        } else {
                            listener.onError(obj.optString("message", "Update team failed"));
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
                p.put("team_id", teamId);
                p.put("name", team.getName());
                p.put("car_number", team.getCarNumber());
                p.put("car_plate", team.getCarPlate());
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void deleteTeam(String teamId, OnTeamDeletedListener listener) {

        StringRequest req = new StringRequest(
                Request.Method.POST,
                DELETE_TEAM,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.optBoolean("success", false)) {
                            listener.onTeamDeleted();
                        } else {
                            listener.onError(obj.optString("message", "Delete team failed"));
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
                p.put("team_id", teamId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    // ===== Helper: parse Team from JSON =====
    private Team parseTeam(JSONObject t) {
        Team team = new Team();

        // عدّل أسماء الحقول إذا PHP بيرجع غير هيك
        team.setTeamId(t.optString("team_id", ""));
        team.setName(t.optString("name", ""));
        team.setCarNumber(t.optString("car_number", t.optString("carNumber", "")));
        team.setCarPlate(t.optString("car_plate", t.optString("carPlate", "")));

        // إذا الموديل عندك فيه available/isAvailable
        // team.setAvailable(t.optInt("available", 1) == 1);

        return team;
    }

    // ===== Callbacks =====
    public interface OnTeamsLoadedListener {
        void onTeamsLoaded(List<Team> teams);
        void onError(String error);
    }

    public interface OnTeamAddedListener {
        void onTeamAdded(Team team);
        void onError(String error);
    }

    public interface OnTeamUpdatedListener {
        void onTeamUpdated();
        void onError(String error);
    }

    public interface OnTeamDeletedListener {
        void onTeamDeleted();
        void onError(String error);
    }
}
