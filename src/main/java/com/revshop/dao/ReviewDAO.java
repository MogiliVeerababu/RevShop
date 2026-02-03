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
}