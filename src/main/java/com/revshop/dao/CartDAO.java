package com.revshop.dao;

import com.revshop.model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends BaseDAO {

    // Get or create active cart for user
    public int getOrCreateCart(int userId) throws SQLException {
        // First, check if user exists
        if (!userExists(userId)) {
            throw new SQLException("User with ID " + userId + " does not exist");
        }

        String sql = "SELECT cart_id FROM carts WHERE user_id = ? AND status = 'active'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("cart_id");
            } else {
                // Create new cart
                return createCart(userId);
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Check if user exists
    private boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Create new cart
    private int createCart(int userId) throws SQLException {
        String sql = "INSERT INTO carts (user_id, status) VALUES (?, 'active')";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userId);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("Failed to create cart, no ID obtained");
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Add item to cart
    public boolean addToCart(int cartId, int productId, int quantity) throws SQLException {
        // First check if product exists and has enough stock
        if (!isProductAvailable(productId, quantity)) {
            throw new SQLException("Product not available or insufficient stock");
        }

        // Check if item already in cart
        String checkSql = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(checkSql);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                // Update existing item quantity
                int existingQuantity = rs.getInt("quantity");
                int cartItemId = rs.getInt("cart_item_id");
                String updateSql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";

                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, existingQuantity + quantity);
                    updateStmt.setInt(2, cartItemId);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Insert new item
                String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, cartId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Check product availability
    private boolean isProductAvailable(int productId, int quantity) throws SQLException {
        String sql = "SELECT stock_quantity FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int stockQuantity = rs.getInt("stock_quantity");
                return stockQuantity >= quantity;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get cart items
    public List<CartItem> getCartItems(int userId) throws SQLException {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.cart_item_id, ci.cart_id, ci.product_id, ci.quantity, " +
                "p.name, p.price FROM cart_items ci " +
                "JOIN products p ON ci.product_id = p.product_id " +
                "JOIN carts c ON ci.cart_id = c.cart_id " +
                "WHERE c.user_id = ? AND c.status = 'active'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("name"));
                item.setProductPrice(rs.getDouble("price"));
                cartItems.add(item);
            }
            return cartItems;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get cart item by ID
    public CartItem getCartItemById(int cartItemId) throws SQLException {
        String sql = "SELECT ci.*, p.name, p.price FROM cart_items ci " +
                "JOIN products p ON ci.product_id = p.product_id " +
                "WHERE ci.cart_item_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartItemId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("name"));
                item.setProductPrice(rs.getDouble("price"));
                return item;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update cart item quantity
    public boolean updateCartItemQuantity(int cartItemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            return removeFromCart(cartItemId);
        }

        // First get the product ID to check stock
        CartItem item = getCartItemById(cartItemId);
        if (item == null) {
            return false;
        }

        if (!isProductAvailable(item.getProductId(), quantity)) {
            throw new SQLException("Insufficient stock");
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";
        return executeUpdate(sql, quantity, cartItemId) > 0;
    }

    // Remove item from cart
    public boolean removeFromCart(int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";
        return executeUpdate(sql, cartItemId) > 0;
    }

    // Clear cart
    public boolean clearCart(int userId) throws SQLException {
        // Get active cart ID
        int cartId = getOrCreateCart(userId);

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        return executeUpdate(sql, cartId) > 0;
    }
}