package com.revshop.service;

import com.revshop.dao.ReviewDAO;
import com.revshop.model.Review;
import java.sql.SQLException;
import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;

    public ReviewService() {
        reviewDAO = new ReviewDAO();
    }

    // Check if buyer can review product (has purchased and received it)
    public boolean canReviewProduct(int buyerId, int productId) {
        try {
            return reviewDAO.canReviewProduct(buyerId, productId);
        } catch (SQLException e) {
            System.err.println("❌ Error checking review eligibility: " + e.getMessage());
            return false;
        }
    }

    // Get message explaining why buyer can't review
    public String getReviewEligibilityMessage(int buyerId, int productId) {
        try {
            // Check if purchased
            String checkPurchaseSql = "SELECT COUNT(*) FROM order_items oi " +
                    "JOIN orders o ON oi.order_id = o.order_id " +
                    "WHERE o.user_id = ? AND oi.product_id = ?";
            // You'd need to implement this in DAO or do it here

            if (!canReviewProduct(buyerId, productId)) {
                return "You can only review products you've purchased and received. " +
                        "Please check your delivered orders first.";
            }
            return null; // Eligible
        } catch (Exception e) {
            return "Error checking review eligibility.";
        }
    }

    // Add review with validation
    public boolean addReviewWithValidation(Review review, int buyerId) {
        try {
            // Validate buyer is the review author
            if (review.getUserId() != buyerId) {
                System.err.println("❌ User ID mismatch!");
                return false;
            }

            // Check if buyer can review
            if (!reviewDAO.canReviewProduct(buyerId, review.getProductId())) {
                System.err.println("❌ Buyer cannot review product: " + review.getProductId());
                return false;
            }

            // Get order_id and order_item_id
            int orderId = reviewDAO.getOrderIdForReview(buyerId, review.getProductId());
            int orderItemId = reviewDAO.getOrderItemIdForReview(buyerId, review.getProductId());

            if (orderId > 0) {
                boolean success = reviewDAO.addReviewWithOrder(review, orderId, orderItemId);
                if (success) {
                    System.out.println("✅ Review added successfully with order link!");
                }
                return success;
            }

            System.err.println("❌ No valid delivered order found for this product.");
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error adding review: " + e.getMessage());
            return false;
        }
    }

    // Get reviews for seller's products
    public List<Review> getReviewsForSeller(int sellerId) {
        try {
            List<Review> reviews = reviewDAO.getReviewsForSeller(sellerId);
            System.out.println("📊 Found " + reviews.size() + " reviews for seller ID: " + sellerId);
            return reviews;
        } catch (SQLException e) {
            System.err.println("❌ Error getting seller reviews: " + e.getMessage());
            return List.of(); // Return empty list
        }
    }

    // Get reviews by product
    public List<Review> getReviewsByProduct(int productId) {
        try {
            return reviewDAO.getReviewsByProduct(productId);
        } catch (SQLException e) {
            System.err.println("❌ Error getting product reviews: " + e.getMessage());
            return List.of();
        }
    }

    // Get average rating for product
    public double getAverageRating(int productId) {
        try {
            return reviewDAO.getAverageRating(productId);
        } catch (SQLException e) {
            System.err.println("❌ Error getting average rating: " + e.getMessage());
            return 0.0;
        }
    }

    // Simple add review (for backward compatibility)
    public boolean addReview(Review review) {
        try {
            // Try to find order_id for backward compatibility
            int orderId = reviewDAO.getOrderIdForReview(review.getUserId(), review.getProductId());
            int orderItemId = reviewDAO.getOrderItemIdForReview(review.getUserId(), review.getProductId());

            if (orderId > 0) {
                // Use new method with order linking
                return reviewDAO.addReviewWithOrder(review, orderId, orderItemId);
            } else {
                // Fallback to old method if no order found
                return reviewDAO.addReview(review);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error in addReview: " + e.getMessage());
            return false;
        }
    }

    // Check if product has any reviews
    public boolean hasReviews(int productId) {
        try {
            List<Review> reviews = reviewDAO.getReviewsByProduct(productId);
            return !reviews.isEmpty();
        } catch (SQLException e) {
            return false;
        }
    }

    // Get review count for seller
    public int getReviewCountForSeller(int sellerId) {
        try {
            List<Review> reviews = reviewDAO.getReviewsForSeller(sellerId);
            return reviews.size();
        } catch (SQLException e) {
            return 0;
        }
    }
}