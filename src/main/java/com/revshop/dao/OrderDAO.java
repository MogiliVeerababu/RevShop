package com.revshop.dao;

import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends BaseDAO {

    // Create new order (MODIFIED: Accepts Order object with pre-set statuses)
    public int createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, shipping_address, " +
                "payment_method, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalAmount());
            stmt.setString(3, order.getStatus());           // Now will be "confirmed"
            stmt.setString(4, order.getShippingAddress());
            stmt.setString(5, order.getPaymentMethod());
            stmt.setString(6, order.getPaymentStatus());    // Now will be "completed"

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return generated order ID
                }
            }
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // NEW METHOD: Create order with specific status (for direct creation)
    public int createOrderDirect(int userId, double totalAmount, String shippingAddress,
                                 String paymentMethod, String status, String paymentStatus) throws SQLException {
        String sql = "INSERT INTO orders (user_id, total_amount, status, shipping_address, " +
                "payment_method, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalAmount);
            stmt.setString(3, status);           // e.g., "confirmed"
            stmt.setString(4, shippingAddress);
            stmt.setString(5, paymentMethod);
            stmt.setString(6, paymentStatus);    // e.g., "completed"

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // Return generated order ID
                }
            }
            return -1;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Add order item
    public boolean addOrderItem(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) " +
                "VALUES (?, ?, ?, ?)";
        return executeUpdate(sql,
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getPrice()) > 0;
    }

    // Get orders by user
    public List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            return orders;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get order by ID
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOrderFromResultSet(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get order items
    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT oi.*, p.name FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setProductName(rs.getString("name"));
                orderItems.add(item);
            }
            return orderItems;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        return executeUpdate(sql, status, orderId) > 0;
    }

    // Update payment status
    public boolean updatePaymentStatus(int orderId, String paymentStatus) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        return executeUpdate(sql, paymentStatus, orderId) > 0;
    }

    // Cancel order
    public boolean cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'cancelled' WHERE order_id = ? AND status IN ('pending', 'confirmed')";
        return executeUpdate(sql, orderId) > 0;
    }

    // Get orders by seller
    public List<Order> getOrdersBySeller(int sellerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT DISTINCT o.* FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE p.seller_id = ? ORDER BY o.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
            return orders;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get total sales for seller
    public double getTotalSalesBySeller(int sellerId) throws SQLException {
        String sql = "SELECT SUM(o.total_amount) as total_sales FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE p.seller_id = ? AND o.payment_status = 'completed'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_sales");
            }
            return 0.0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get pending orders count for seller
    public int getPendingOrdersCount(int sellerId) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT o.order_id) as pending_count FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE p.seller_id = ? AND o.status = 'pending'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("pending_count");
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setPaymentStatus(rs.getString("payment_status"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        return order;
    }
}