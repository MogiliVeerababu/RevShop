package com.revshop.model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int userId;
    private double totalAmount;
    private String status; // pending, confirmed, shipped, delivered, cancelled
    private String shippingAddress;
    private String paymentMethod;
    private String paymentStatus; // pending, completed, failed
    private Timestamp createdAt;

    public Order() {}

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Order [ID: %d, Amount: $%.2f, Status: %s, Payment: %s]",
                orderId, totalAmount, status, paymentStatus);
    }
}