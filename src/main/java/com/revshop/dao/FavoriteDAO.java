package com.revshop.dao;

import com.revshop.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO extends BaseDAO {

    // Add the product to favorites
    public boolean addToFavorites(int userId, int productId) throws SQLException {
        String sql = "INSERT INTO favorites (user_id, product_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Check if already favorited
            if (isFavorite(userId, productId)) {
                return false; // Already in favorites
            }

            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            return stmt.executeUpdate() > 0;
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // Remove product from favorites
    public boolean removeFromFavorites(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE user_id = ? AND product_id = ?";
        return executeUpdate(sql, userId, productId) > 0;
    }

    // Check if product is in favorites
    public boolean isFavorite(int userId, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get all favorite products for a user
    public List<Product> getFavorites(int userId) throws SQLException {
        List<Product> favorites = new ArrayList<>();
        String sql = "SELECT p.* FROM products p " +
                "JOIN favorites f ON p.product_id = f.product_id " +
                "WHERE f.user_id = ? " +
                "ORDER BY f.created_at DESC";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setMrp(rs.getDouble("mrp"));
                product.setDiscountedPrice(rs.getDouble("discounted_price"));
                product.setStockQuantity(rs.getInt("stock_quantity"));
                product.setCategory(rs.getString("category"));
                product.setSellerId(rs.getInt("seller_id"));

                favorites.add(product);
            }
            return favorites;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get favorite count for a user
    public int getFavoriteCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
}