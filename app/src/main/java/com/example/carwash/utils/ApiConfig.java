package com.example.carwash.utils;

public class ApiConfig {
    // Emulator فقط
    public static final String BASE_URL = "http://10.0.2.2/carwash_api/";

    // Endpoints
    public static final String REGISTER = BASE_URL + "register.php";
    public static final String LOGIN    = BASE_URL + "login.php";

    // =========================
    // MANAGER - DASHBOARD / STATS
    // =========================
    public static final String MANAGER_STATS = BASE_URL + "manager_stats.php";

    // =========================
    // MANAGER - EMPLOYEES
    // =========================
    public static final String GET_ALL_EMPLOYEES = BASE_URL + "get_all_employees.php";
    public static final String ADD_EMPLOYEE      = BASE_URL + "add_employee.php";
    public static final String UPDATE_EMPLOYEE   = BASE_URL + "update_employee.php";
    public static final String DELETE_EMPLOYEE   = BASE_URL + "delete_employee.php";

    // =========================
    // SERVICES
    // =========================
    public static final String GET_ALL_SERVICES   = BASE_URL + "get_all_services.php";
    public static final String GET_SERVICE_BY_ID  = BASE_URL + "get_service_by_id.php";
    public static final String ADD_SERVICE        = BASE_URL + "add_service.php";
    public static final String UPDATE_SERVICE     = BASE_URL + "update_service.php";
    public static final String DELETE_SERVICE     = BASE_URL + "delete_service.php";

    // =========================
    // TEAMS
    // =========================
    public static final String GET_ALL_TEAMS        = BASE_URL + "get_all_teams.php";
    public static final String GET_AVAILABLE_TEAMS  = BASE_URL + "get_available_teams.php";
    public static final String ADD_TEAM             = BASE_URL + "add_team.php";
    public static final String UPDATE_TEAM          = BASE_URL + "update_team.php"; // إذا عملتيه
    public static final String DELETE_TEAM          = BASE_URL + "delete_team.php"; // إذا عملتيه
    public static final String UPDATE_TEAM_AVAIL    = BASE_URL + "update_team_availability.php";

    // =========================
    // TEAM MEMBERS (team_users)
    // =========================
    public static final String GET_TEAM_MEMBERS   = BASE_URL + "get_team_members.php";
    public static final String ADD_TEAM_MEMBER    = BASE_URL + "add_team_member.php";
    public static final String REMOVE_TEAM_MEMBER = BASE_URL + "remove_team_member.php";

    // =========================
    // BOOKINGS (Manager side)
    // =========================
    public static final String GET_ALL_BOOKINGS     = BASE_URL + "get_all_bookings.php";
    public static final String GET_PENDING_BOOKINGS = BASE_URL + "get_pending_bookings.php";
    public static final String ASSIGN_TEAM          = BASE_URL + "assign_team.php";

    // =========================
    // REVIEWS
    // =========================
    public static final String GET_ALL_REVIEWS = BASE_URL + "get_all_reviews.php";

    // =========================
    // CARS
    // =========================
    public static final String GET_USER_CARS = BASE_URL + "get_user_cars.php";
    public static final String ADD_CAR       = BASE_URL + "add_car.php";
    public static final String UPDATE_CAR    = BASE_URL + "update_car.php";
    public static final String DELETE_CAR    = BASE_URL + "delete_car.php";
}


