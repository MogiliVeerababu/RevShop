package com.revshop.dao;

import com.revshop.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO extends BaseDAO {

    // Add review
    public boolean addReview(Review review) throws SQLException {
        // Check if user has already reviewed this product
        String checkSql = "SELECT COUNT(*) FROM reviews WHERE product_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement checkStmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, review.getProductId());
            checkStmt.setInt(2, review.getUserId());
            rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Update existing review
                String updateSql = "UPDATE reviews SET rating = ?, comment = ? WHERE product_id = ? AND user_id = ?";
                return executeUpdate(updateSql,
                        review.getRating(),
                        review.getComment(),
                        review.getProductId(),
                        review.getUserId()) > 0;
            } else {
                // Insert new review
                String insertSql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
                return executeUpdate(insertSql,
                        review.getProductId(),
                        review.getUserId(),
                        review.getRating(),
                        review.getComment()) > 0;
            }
        } finally {
            closeResources(conn, checkStmt, rs);
        }
    }

    // Get reviews by product
    public List<Review> getReviewsByProduct(int productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM reviews r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setUsername(rs.getString("username"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                reviews.add(review);
            }
            return reviews;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get average rating for product
    public double getAverageRating(int productId) throws SQLException {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
            return 0.0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Delete review
    public boolean deleteReview(int reviewId) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        return executeUpdate(sql, reviewId) > 0;
    }
    // =========== NEW METHODS TO ADD ===========

    // 1. Check if buyer can review this product
    public boolean canReviewProduct(int buyerId, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.user_id = ? " +
                "AND oi.product_id = ? " +
                "AND o.status = 'delivered' " +
                "AND (oi.reviewed IS NULL OR oi.reviewed = false)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, buyerId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // 2. Get order_id for review
    public int getOrderIdForReview(int buyerId, int productId) throws SQLException {
        String sql = "SELECT oi.order_id FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.user_id = ? " +
                "AND oi.product_id = ? " +
                "AND o.status = 'delivered' " +
                "AND (oi.reviewed IS NULL OR oi.reviewed = false) " +
                "LIMIT 1";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, buyerId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_id");
            }
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // 3. Get order_item_id for review
    public int getOrderItemIdForReview(int buyerId, int productId) throws SQLException {
        String sql = "SELECT oi.order_item_id FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE o.user_id = ? " +
                "AND oi.product_id = ? " +
                "AND o.status = 'delivered' " +
                "AND (oi.reviewed IS NULL OR oi.reviewed = false) " +
                "LIMIT 1";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, buyerId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("order_item_id");
            }
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // 4. Mark order_item as reviewed
    public boolean markOrderItemAsReviewed(int orderItemId) throws SQLException {
        String sql = "UPDATE order_items SET reviewed = true WHERE order_item_id = ?";
        return executeUpdate(sql, orderItemId) > 0;
    }

    // 5. Get reviews for seller's products
    public List<Review> getReviewsForSeller(int sellerId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username, p.name as product_name, p.product_id, r.order_id " +
                "FROM reviews r " +
                "JOIN users u ON r.user_id = u.user_id " +
                "JOIN products p ON r.product_id = p.product_id " +
                "WHERE p.seller_id = ? " +
                "ORDER BY r.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                review.setUsername(rs.getString("username"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                review.setProductName(rs.getString("product_name"));
                review.setOrderId(rs.getInt("order_id"));
                reviews.add(review);
            }
            return reviews;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // 6. New method: Add review with order_id (use this instead of old addReview for validated reviews)
    public boolean addReviewWithOrder(Review review, int orderId, int orderItemId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // First check if review already exists for this order_item
            String checkSql = "SELECT COUNT(*) FROM reviews WHERE order_id = ? AND user_id = ? AND product_id = ?";
            stmt = conn.prepareStatement(checkSql);
            stmt.setInt(1, orderId);
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getProductId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Update existing review
                String updateSql = "UPDATE reviews SET rating = ?, comment = ? WHERE order_id = ? AND user_id = ? AND product_id = ?";
                stmt = conn.prepareStatement(updateSql);
                stmt.setInt(1, review.getRating());
                stmt.setString(2, review.getComment());
                stmt.setInt(3, orderId);
                stmt.setInt(4, review.getUserId());
                stmt.setInt(5, review.getProductId());
                stmt.executeUpdate();
            } else {
                // Insert new review
                String insertSql = "INSERT INTO reviews (product_id, user_id, rating, comment, order_id) VALUES (?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, review.getProductId());
                stmt.setInt(2, review.getUserId());
                stmt.setInt(3, review.getRating());
                stmt.setString(4, review.getComment());
                stmt.setInt(5, orderId);
                stmt.executeUpdate();
            }

            // Mark order_item as reviewed
            if (orderItemId > 0) {
                String updateItemSql = "UPDATE order_items SET reviewed = true WHERE order_item_id = ?";
                stmt = conn.prepareStatement(updateItemSql);
                stmt.setInt(1, orderItemId);
                stmt.executeUpdate();
            }

            // Update product average rating
            updateProductAverageRating(review.getProductId(), conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // 7. Helper method to update product average rating
    private void updateProductAverageRating(int productId, Connection conn) throws SQLException {
        String sql = "UPDATE products p " +
                "SET average_rating = (" +
                "    SELECT COALESCE(AVG(rating), 0) FROM reviews WHERE product_id = ?" +
                ") WHERE product_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

// 8. Optional: Update existing addReview method to include order_id when possible
// You can keep your existing addReview method as is for backward compatibility
// OR replace it with this enhanced version:
/*
public boolean addReview(Review review) throws SQLException {
    // Try to find if user has purchased this product
    int orderId = getOrderIdForReview(review.getUserId(), review.getProductId());
    int orderItemId = getOrderItemIdForReview(review.getUserId(), review.getProductId());

    if (orderId > 0) {
        // User has purchased, use new method
        return addReviewWithOrder(review, orderId, orderItemId);
    } else {
        // User hasn't purchased (or old data), use old method
        return addReviewOldMethod(review);
    }
}

private boolean addReviewOldMethod(Review review) throws SQLException {
    // Your original addReview logic here
    String checkSql = "SELECT COUNT(*) FROM reviews WHERE product_id = ? AND user_id = ?";
    // ... rest of original code
}
*/
}