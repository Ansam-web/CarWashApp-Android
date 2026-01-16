package com.example.carwash.models;

public class Car {
    private String carId;
    private String userId;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String plateNumber;
    private String carType; // sedan, suv, truck, hatchback
    private long createdAt;

    public Car() {}

    public Car(String userId, String brand, String model, int year, String color,
               String plateNumber, String carType) {
        this.userId = userId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.plateNumber = plateNumber;
        this.carType = carType;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getCarId() { return carId; }
    public void setCarId(String carId) { this.carId = carId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getDisplayName() {
        return brand + " " + model + " (" + year + ")";
    }

    @Override
    public String toString() {
        return getDisplayName() + " - " + plateNumber;
    }
}
