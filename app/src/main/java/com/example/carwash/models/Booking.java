package com.example.carwash.models;

public class Booking {

    private String bookingId;
    private String userId;
    private String carId;
    private String serviceId;
    private String teamId;

    // pending, assigned, on_the_way, arrived, in_progress, completed, cancelled
    private String status;

    private String bookingDate;
    private String bookingTime;
    private String locationAddress;

    private double locationLat;
    private double locationLng;
    private double totalPrice;

    // ✅ لازم تكون nullable لأنها بتجي من API أحياناً null
    private Integer rating;
    private String review;

    // Dates as STRING (from MySQL)
    private String createdAt;
    private String completedAt;

    // Display-only fields (JOIN results)
    private String serviceName;
    private String carModel;
    private String userName;

    // ✅ Display fields from Manager JOIN (API)
    private String customerName;  // customer_name
    private String serviceType;   // service_type
    private String teamName;      // team_name

    public Booking() {
        // Required empty constructor
    }

    public Booking(String userId, String carId, String serviceId,
                   String bookingDate, String bookingTime,
                   String locationAddress, double totalPrice) {

        this.userId = userId;
        this.carId = carId;
        this.serviceId = serviceId;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.locationAddress = locationAddress;
        this.totalPrice = totalPrice;
        this.status = "pending";
    }

    // ================= GETTERS & SETTERS =================

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }

    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }

    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    public String getLocationAddress() { return locationAddress; }
    public void setLocationAddress(String locationAddress) { this.locationAddress = locationAddress; }

    public double getLocationLat() { return locationLat; }
    public void setLocationLat(double locationLat) { this.locationLat = locationLat; }

    public double getLocationLng() { return locationLng; }
    public void setLocationLng(double locationLng) { this.locationLng = locationLng; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    // ✅ nullable rating
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getCompletedAt() { return completedAt; }
    public void setCompletedAt(String completedAt) { this.completedAt = completedAt; }

    // ============== DISPLAY (JOIN) FIELDS =================

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    // ============== MANAGER API FIELDS ====================

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", status='" + status + '\'' +
                ", date='" + bookingDate + '\'' +
                ", time='" + bookingTime + '\'' +
                '}';
    }
}
