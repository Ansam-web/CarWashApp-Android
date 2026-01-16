package com.example.carwash.models;

public class Wallet {
    private String walletId;
    private String userId;
    private double balance;
    private long updatedAt;

    public Wallet() {}

    public Wallet(String userId, double balance) {
        this.userId = userId;
        this.balance = balance;
        this.updatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getWalletId() { return walletId; }
    public void setWalletId(String walletId) { this.walletId = walletId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) {
        this.balance = balance;
        this.updatedAt = System.currentTimeMillis();
    }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Wallet{" +
                "userId='" + userId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
