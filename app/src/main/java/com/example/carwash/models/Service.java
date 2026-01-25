package com.example.carwash.models;

public class Service {
    private String serviceId;
    private String type;
    private String description;
    private int duration;
    private double price;

    public Service() {}

    public Service(String type, String description, int duration, double price) {
        this.type = type;
        this.description = description;
        this.duration = duration;
        this.price = price;
    }

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
}
