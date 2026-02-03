package com.revshop.model;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private double mrp;
    private double discountedPrice;
    private int stockQuantity;
    private String category;
    private int sellerId;

    public Product() {}

    public Product(String name, String description, double price, int stockQuantity,
                   String category, int sellerId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = price;
        this.discountedPrice = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
        // Ensure price is the discounted price
        if (discountedPrice > 0) {
            this.price = discountedPrice;
        }
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public double getDiscountPercentage() {
        if (mrp > 0 && price < mrp) {
            return ((mrp - price) / mrp) * 100;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Product [ID: %d, Name: %s, Price: $%.2f, MRP: $%.2f, Discount: %.1f%%, Stock: %d, Category: %s]",
                productId, name, price, mrp, getDiscountPercentage(), stockQuantity, category);
    }
}