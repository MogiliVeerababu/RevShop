package com.revshop.service;

import com.revshop.dao.CartDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.model.CartItem;
import java.sql.SQLException;
import java.util.List;

public class CartService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;

    public CartService() {
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }

    // Add to cart
    public boolean addToCart(int userId, int productId, int quantity) {
        try {
            // First, check if user exists (this should be done during login)
            // Get or create cart for user
            int cartId = cartDAO.getOrCreateCart(userId);

            // Add to cart
            return cartDAO.addToCart(cartId, productId, quantity);
        } catch (SQLException e) {
            System.err.println("Error adding to cart: " + e.getMessage());
            return false;
        }
    }

    // Get cart items
    public List<CartItem> getCartItems(int userId) {
        try {
            return cartDAO.getCartItems(userId);
        } catch (SQLException e) {
            System.err.println("Error getting cart items: " + e.getMessage());
            return List.of();
        }
    }

    // Update cart item quantity
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {
        try {
            return cartDAO.updateCartItemQuantity(cartItemId, quantity);
        } catch (SQLException e) {
            System.err.println("Error updating cart item: " + e.getMessage());
            return false;
        }
    }

    // Remove from cart
    public boolean removeFromCart(int cartItemId) {
        try {
            return cartDAO.removeFromCart(cartItemId);
        } catch (SQLException e) {
            System.err.println("Error removing from cart: " + e.getMessage());
            return false;
        }
    }

    // Clear cart
    public boolean clearCart(int userId) {
        try {
            return cartDAO.clearCart(userId);
        } catch (SQLException e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            return false;
        }
    }

    // Get cart total
    public double getCartTotal(int userId) {
        try {
            List<CartItem> items = cartDAO.getCartItems(userId);
            double total = 0;
            for (CartItem item : items) {
                total += item.getTotalPrice();
            }
            return total;
        } catch (SQLException e) {
            System.err.println("Error calculating cart total: " + e.getMessage());
            return 0.0;
        }
    }

    // Get cart item count
    public int getCartItemCount(int userId) {
        try {
            List<CartItem> items = cartDAO.getCartItems(userId);
            return items.size();
        } catch (SQLException e) {
            System.err.println("Error getting cart item count: " + e.getMessage());
            return 0;
        }
    }
}