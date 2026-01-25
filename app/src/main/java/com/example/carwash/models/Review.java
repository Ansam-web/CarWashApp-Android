package com.example.carwash.models;

public class Review {

    private int bookingId;
    private String customerName;
    private String serviceType;
    private int rating;
    private String comment;   // <-- هذا اللي كان ناقص
    private String reviewDate;

    public Review() {
    }

    public Review(int bookingId, String customerName, String serviceType,
                  int rating, String comment, String reviewDate) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.serviceType = serviceType;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public int getRating() {
        return rating;
    }

    /** ✅ هذا المهم */
    public String getComment() {
        return comment;
    }

    public String getReviewDate() {
        return reviewDate;
    }
}
