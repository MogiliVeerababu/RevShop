package com.revshop.model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int productId;
    private int userId;
    private int rating;
    private String comment;
    private String username;
    private Timestamp createdAt;

    // NEW FIELDS ADDED BELOW - KEEP ALL EXISTING CODE ABOVE
    private String productName;  // To show product name in seller view
    private int orderId;         // To link review with order

    public Review() {}

    public Review(int productId, int userId, int rating, String comment) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // NEW GETTERS AND SETTERS ADDED BELOW
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return String.format("Review [Rating: %d/5, By: %s, Comment: %s, Date: %s]",
                rating, username, comment, createdAt);
    }
}