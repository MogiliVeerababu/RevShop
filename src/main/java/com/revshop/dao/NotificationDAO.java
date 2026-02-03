package com.revshop.dao;

import com.revshop.model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO extends BaseDAO {

    // Add notification
    public boolean addNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, type, message) VALUES (?, ?, ?)";
        return executeUpdate(sql,
                notification.getUserId(),
                notification.getType(),
                notification.getMessage()) > 0;
    }

    // Get notifications by user
    public List<Notification> getNotificationsByUser(int userId) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 10";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notification_id"));
                notification.setUserId(rs.getInt("user_id"));
                notification.setType(rs.getString("type"));
                notification.setMessage(rs.getString("message"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setCreatedAt(rs.getTimestamp("created_at"));
                notifications.add(notification);
            }
            return notifications;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Mark notification as read
    public boolean markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        return executeUpdate(sql, notificationId) > 0;
    }

    // Mark all notifications as read
    public boolean markAllAsRead(int userId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ?";
        return executeUpdate(sql, userId) > 0;
    }

    // Get unread notification count
    public int getUnreadCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM notifications WHERE user_id = ? AND is_read = false";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
}