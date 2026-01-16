package com.example.carwash.models;

public class Notification {

    private String notificationId;
    private String userId;
    private String title;
    private String message;
    private String type; // booking_confirmed, team_assigned, status_update, etc.
    private String relatedBookingId;
    private boolean isRead;

    // store datetime as String coming from MySQL (e.g. "2026-01-16 15:20:00")
    private String createdAt;

    public Notification() {}

    public Notification(String userId, String title, String message, String type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = false;
    }

    // Getters and Setters
    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRelatedBookingId() { return relatedBookingId; }
    public void setRelatedBookingId(String relatedBookingId) { this.relatedBookingId = relatedBookingId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Notification{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", read=" + isRead +
                '}';
    }
}
