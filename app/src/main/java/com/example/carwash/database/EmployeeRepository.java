package com.example.carwash.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.carwash.models.Employee;
import com.example.carwash.models.EmployeeTeam;
import com.example.carwash.utils.ApiConfig;
import com.example.carwash.utils.Constants;
import com.example.carwash.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeRepository {

    private final Context context;

    public EmployeeRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    // نفس فكرة Teams: بدون لمس SharedPrefManager
    private String safeUserId() {
        SharedPreferences sp = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            return sp.getString("user_id", "");
        } catch (ClassCastException ex) {
            int id = sp.getInt("user_id", -1);
            return String.valueOf(id);
        }
    }

    public void getAllEmployees(OnEmployeesLoadedListener listener) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.GET_ALL_EMPLOYEES,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.optBoolean("success", false)) {
                            listener.onError(obj.optString("message", "Failed to load employees"));
                            return;
                        }

                        JSONArray arr = obj.optJSONArray("employees");
                        List<Employee> list = new ArrayList<>();

                        if (arr != null) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject e = arr.getJSONObject(i);
                                list.add(parseEmployee(e));
                            }
                        }

                        listener.onEmployeesLoaded(list);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.onError("Parse error");
                    }
                },
                error -> {
                    error.printStackTrace();
                    listener.onError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", safeUserId());
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void addEmployee(String name, String email, String phone, String password, OnEmployeeAddedListener listener) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.ADD_EMPLOYEE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onEmployeeAdded();
                        } else {
                            listener.onError(obj.optString("message", "Add employee failed"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.onError("Parse error");
                    }
                },
                error -> {
                    error.printStackTrace();
                    listener.onError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", safeUserId());
                p.put("name", name);
                p.put("email", email);
                p.put("phone", phone);
                p.put("password", password);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void updateEmployee(String employeeId, String name, String email, String phone, OnEmployeeUpdatedListener listener) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.UPDATE_EMPLOYEE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onEmployeeUpdated();
                        } else {
                            listener.onError(obj.optString("message", "Update employee failed"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.onError("Parse error");
                    }
                },
                error -> {
                    error.printStackTrace();
                    listener.onError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", safeUserId());
                p.put("employee_id", employeeId);
                p.put("name", name);
                p.put("email", email);
                p.put("phone", phone);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    public void deleteEmployee(String employeeId, OnEmployeeDeletedListener listener) {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                ApiConfig.DELETE_EMPLOYEE,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.optBoolean("success", false)) {
                            listener.onEmployeeDeleted();
                        } else {
                            listener.onError(obj.optString("message", "Delete employee failed"));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        listener.onError("Parse error");
                    }
                },
                error -> {
                    error.printStackTrace();
                    listener.onError("Network error");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> p = new HashMap<>();
                p.put("manager_id", safeUserId());
                p.put("employee_id", employeeId);
                return p;
            }
        };

        VolleySingleton.getInstance(context).add(req);
    }

    private Employee parseEmployee(JSONObject e) throws Exception {
        Employee emp = new Employee();
        emp.setId(String.valueOf(e.optInt("id", -1)));
        emp.setName(e.optString("name", ""));
        emp.setEmail(e.optString("email", ""));
        emp.setPhone(e.optString("phone", ""));

        JSONArray teamsArr = e.optJSONArray("teams");
        List<EmployeeTeam> teams = new ArrayList<>();

        if (teamsArr != null) {
            for (int j = 0; j < teamsArr.length(); j++) {
                JSONObject t = teamsArr.getJSONObject(j);
                EmployeeTeam et = new EmployeeTeam();
                et.setTeamId(t.optInt("team_id", -1));
                et.setTeamName(t.optString("team_name", ""));
                et.setRoleInTeam(t.optString("role_in_team", "member"));
                teams.add(et);
            }
        }

        emp.setTeams(teams);
        return emp;
    }

    // Callbacks
    public interface OnEmployeesLoadedListener {
        void onEmployeesLoaded(List<Employee> employees);
        void onError(String error);
    }

    public interface OnEmployeeAddedListener {
        void onEmployeeAdded();
        void onError(String error);
    }

    public interface OnEmployeeUpdatedListener {
        void onEmployeeUpdated();
        void onError(String error);
    }

    public interface OnEmployeeDeletedListener {
        void onEmployeeDeleted();
        void onError(String error);
    }
}
