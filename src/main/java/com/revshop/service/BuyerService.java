package com.revshop.service;

import com.revshop.dao.ReviewDAO;
import com.revshop.dao.NotificationDAO;
import com.revshop.dao.FavoriteDAO;
import com.revshop.model.Review;
import java.util.ArrayList;
import com.revshop.model.Product;
import com.revshop.model.Notification;
import java.sql.SQLException;
import java.util.List;



public class BuyerService {
    private ReviewDAO reviewDAO;
    private NotificationDAO notificationDAO;
    private FavoriteDAO favoriteDAO;

    public BuyerService() {
        reviewDAO = new ReviewDAO();
        notificationDAO = new NotificationDAO();
        favoriteDAO = new FavoriteDAO();
    }

    // Add review
    public boolean addReview(int userId, int productId, int rating, String comment) {
        try {
            Review review = new Review(productId, userId, rating, comment);
            return reviewDAO.addReview(review);
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
            return false;
        }
    }

    // Get product reviews
    public List<Review> getProductReviews(int productId) {
        try {
            return reviewDAO.getReviewsByProduct(productId);
        } catch (SQLException e) {
            System.err.println("Error getting reviews: " + e.getMessage());
            return List.of();
        }
    }

    // Get average rating
    public double getAverageRating(int productId) {
        try {
            return reviewDAO.getAverageRating(productId);
        } catch (SQLException e) {
            System.err.println("Error getting average rating: " + e.getMessage());
            return 0.0;
        }
    }

    // Get notifications
    public List<Notification> getNotifications(int userId) {
        try {
            return notificationDAO.getNotificationsByUser(userId);
        } catch (SQLException e) {
            System.err.println("Error getting notifications: " + e.getMessage());
            return List.of();
        }
    }

    // Mark notification as read
    public boolean markNotificationAsRead(int notificationId) {
        try {
            return notificationDAO.markAsRead(notificationId);
        } catch (SQLException e) {
            System.err.println("Error marking notification: " + e.getMessage());
            return false;
        }
    }

    // Mark all notifications as read
    public boolean markAllNotificationsAsRead(int userId) {
        try {
            return notificationDAO.markAllAsRead(userId);
        } catch (SQLException e) {
            System.err.println("Error marking all notifications: " + e.getMessage());
            return false;
        }
    }

    // Get unread notification count
    public int getUnreadNotificationCount(int userId) {
        try {
            return notificationDAO.getUnreadCount(userId);
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
            return 0;
        }
    }

    // ============ FAVORITE METHODS ============ //

    // Get favorite products
    public List<Product> getFavorites(int userId) {
        try {
            return favoriteDAO.getFavorites(userId);
        } catch (SQLException e) {
            System.err.println("Error getting favorites: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Add to favorites
    public boolean addToFavorites(int userId, int productId) {
        try {
            return favoriteDAO.addToFavorites(userId, productId);
        } catch (SQLException e) {
            System.err.println("Error adding to favorites: " + e.getMessage());
            return false;
        }
    }

    // Remove from favorites
    public boolean removeFromFavorites(int userId, int productId) {
        try {
            return favoriteDAO.removeFromFavorites(userId, productId);
        } catch (SQLException e) {
            System.err.println("Error removing from favorites: " + e.getMessage());
            return false;
        }
    }

    // Check if product is in favorites
    public boolean isProductInFavorites(int userId, int productId) {
        try {
            return favoriteDAO.isFavorite(userId, productId);
        } catch (SQLException e) {
            System.err.println("Error checking favorite: " + e.getMessage());
            return false;
        }
    }

    // Get favorite count
    public int getFavoriteCount(int userId) {
        try {
            return favoriteDAO.getFavoriteCount(userId);
        } catch (SQLException e) {
            System.err.println("Error getting favorite count: " + e.getMessage());
            return 0;
        }
    }
}