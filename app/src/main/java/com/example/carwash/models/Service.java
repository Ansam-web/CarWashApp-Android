package com.example.carwash.models;

public class Service {
    private String serviceId;
    private String type;
    private String description;
    private int duration; // in minutes
    private double price;
    private boolean isActive;
    private String imageUrl;

    public Service() {}

    public Service(String serviceId, String type, String description, int duration, double price) {
        this.serviceId = serviceId;
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.isActive = true;
    }

    // Getters and Setters
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId='" + serviceId + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                '}';
    }
}
