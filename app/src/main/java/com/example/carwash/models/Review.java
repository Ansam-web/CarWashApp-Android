package com.example.carwash.models;

public class Review {

    private String reviewId;
    private String bookingId;
    private String userId;

    // rating from 1 to 5
    private int rating;

    private String comment;

    // stored as String from MySQL (e.g. "2026-01-16 16:30:00")
    private String createdAt;

    public Review() {
        // empty constructor required
    }

    public Review(String bookingId, String userId, int rating, String comment) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // ================= GETTERS & SETTERS =================

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        // simple validation
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
