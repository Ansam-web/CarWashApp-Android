package com.example.carwash.models;

public class WalletTransaction {

    private String transactionId;
    private String walletId;
    private String bookingId;

    private double amount;

    // recharge, payment, refund
    private String type;

    private String description;

    // stored as String from MySQL (e.g. "2026-01-16 18:05:00")
    private String transactionDate;

    public WalletTransaction() {
        // empty constructor required
    }

    public WalletTransaction(String walletId, double amount, String type, String description) {
        this.walletId = walletId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    // ================= GETTERS & SETTERS =================

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    /**
     * recharge, payment, refund
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "WalletTransaction{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
