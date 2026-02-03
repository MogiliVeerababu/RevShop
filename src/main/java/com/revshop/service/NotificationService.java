package com.revshop.service;

import com.revshop.dao.NotificationDAO;
import com.revshop.model.Notification;
import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private NotificationDAO notificationDAO;

    public NotificationService() {
        notificationDAO = new NotificationDAO();
    }

    // Send notification
    public boolean sendNotification(int userId, String type, String message) {
        try {
            Notification notification = new Notification(userId, type, message);
            return notificationDAO.addNotification(notification);
        } catch (SQLException e) {
            System.err.println("Error sending notification: " + e.getMessage());
            return false;
        }
    }

    // Send order notification to seller
    public boolean sendOrderNotificationToSeller(int sellerId, int orderId) {
        String message = "New order #" + orderId + " has been placed for your product.";
        return sendNotification(sellerId, "new_order", message);
    }
}