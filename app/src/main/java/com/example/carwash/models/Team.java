package com.example.carwash.models;

import java.util.Objects;

public class Team {
    private String teamId;
    private String name;
    private String carNumber;
    private String carPlate;
    private boolean isAvailable;
    private long createdAt;

    public Team() {}

    public Team(String name, String carNumber, String carPlate) {
        this.name = name;
        this.carNumber = carNumber;
        this.carPlate = carPlate;
        this.isAvailable = true;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCarNumber() { return carNumber; }
    public void setCarNumber(String carNumber) { this.carNumber = carNumber; }
    public String getCarPlate() { return carPlate; }
    public void setCarPlate(String carPlate) { this.carPlate = carPlate; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return isAvailable == team.isAvailable &&
                createdAt == team.createdAt &&
                Objects.equals(teamId, team.teamId) &&
                Objects.equals(name, team.name) &&
                Objects.equals(carNumber, team.carNumber) &&
                Objects.equals(carPlate, team.carPlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, name, carNumber, carPlate, isAvailable, createdAt);
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", available=" + isAvailable +
                '}';
    }
}
