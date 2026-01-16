package com.example.carwash.utils;

/**
 * Constants.java
 * Contains all constant values used throughout the app
 */
public class Constants {

    // ========================================================================
    // FIREBASE COLLECTIONS
    // ========================================================================
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_CARS = "cars";
    public static final String COLLECTION_SERVICES = "services";
    public static final String COLLECTION_TEAMS = "teams";
    public static final String COLLECTION_BOOKINGS = "bookings";
    public static final String COLLECTION_NOTIFICATIONS = "notifications";
    public static final String COLLECTION_WALLETS = "wallets";
    public static final String COLLECTION_TRANSACTIONS = "transactions";

    // ========================================================================
    // USER ROLES
    // ========================================================================
    public static final String ROLE_CUSTOMER = "customer";
    public static final String ROLE_EMPLOYEE = "employee";
    public static final String ROLE_MANAGER = "manager";

    // ========================================================================
    // BOOKING STATUS
    // ========================================================================
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ASSIGNED = "assigned";
    public static final String STATUS_ON_THE_WAY = "on_the_way";
    public static final String STATUS_ARRIVED = "arrived";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED_CUSTOMER = "cancelled_by_customer";
    public static final String STATUS_CANCELLED_COMPANY = "cancelled_by_company";

    // ========================================================================
    // SHARED PREFERENCES KEYS
    // ========================================================================
    public static final String PREFS_NAME = "CarWashPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_PHONE = "user_phone";
    public static final String KEY_USER_ROLE = "user_role";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_FIRST_TIME = "first_time";

    // ========================================================================
    // INTENT KEYS
    // ========================================================================
    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_SERVICE_ID = "service_id";
    public static final String EXTRA_SERVICE = "service";
    public static final String EXTRA_BOOKING_ID = "booking_id";
    public static final String EXTRA_BOOKING = "booking";
    public static final String EXTRA_CAR_ID = "car_id";
    public static final String EXTRA_CAR = "car";
    public static final String EXTRA_TEAM_ID = "team_id";
    public static final String EXTRA_TEAM = "team";
    public static final String EXTRA_USER_ID = "user_id";

    // ========================================================================
    // MODES
    // ========================================================================
    public static final String MODE_LOGIN = "login";
    public static final String MODE_REGISTER = "register";
    public static final String MODE_FORGOT = "forgot";
    public static final String MODE_ADD = "add";
    public static final String MODE_EDIT = "edit";
    public static final String MODE_VIEW = "view";

    // ========================================================================
    // CAR TYPES
    // ========================================================================
    public static final String CAR_TYPE_SEDAN = "sedan";
    public static final String CAR_TYPE_SUV = "suv";
    public static final String CAR_TYPE_TRUCK = "truck";
    public static final String CAR_TYPE_HATCHBACK = "hatchback";

    // ========================================================================
    // TRANSACTION TYPES
    // ========================================================================
    public static final String TRANSACTION_RECHARGE = "recharge";
    public static final String TRANSACTION_PAYMENT = "payment";
    public static final String TRANSACTION_REFUND = "refund";

    // ========================================================================
    // NOTIFICATION TYPES
    // ========================================================================
    public static final String NOTIF_BOOKING_CONFIRMED = "booking_confirmed";
    public static final String NOTIF_TEAM_ASSIGNED = "team_assigned";
    public static final String NOTIF_STATUS_UPDATE = "status_update";
    public static final String NOTIF_BOOKING_COMPLETED = "booking_completed";
    public static final String NOTIF_BOOKING_CANCELLED = "booking_cancelled";

    // ========================================================================
    // REQUEST CODES
    // ========================================================================
    public static final int REQUEST_ADD_CAR = 1001;
    public static final int REQUEST_EDIT_CAR = 1002;
    public static final int REQUEST_BOOK_SERVICE = 1003;
    public static final int REQUEST_LOCATION_PERMISSION = 1004;

    // ========================================================================
    // VALIDATION
    // ========================================================================
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_RATING = 5;
    public static final int MIN_RATING = 1;

    // ========================================================================
    // UI
    // ========================================================================
    public static final int SPLASH_DURATION = 2000; // 2 seconds
    public static final int ANIMATION_DURATION = 300; // milliseconds

    // ========================================================================
    // DATE & TIME FORMATS
    // ========================================================================
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm";

    // ========================================================================
    // ERROR MESSAGES
    // ========================================================================
    public static final String ERROR_EMPTY_FIELD = "This field is required";
    public static final String ERROR_INVALID_EMAIL = "Invalid email address";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number";
    public static final String ERROR_PASSWORD_SHORT = "Password must be at least 6 characters";
    public static final String ERROR_PASSWORDS_DONT_MATCH = "Passwords don't match";
    public static final String ERROR_NO_INTERNET = "No internet connection";
    public static final String ERROR_SOMETHING_WRONG = "Something went wrong";
}