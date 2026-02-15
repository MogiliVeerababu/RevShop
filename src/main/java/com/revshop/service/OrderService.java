package com.revshop.service;

import com.revshop.dao.OrderDAO;
import com.revshop.dao.ProductDAO;
import com.revshop.dao.NotificationDAO;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private NotificationDAO notificationDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        notificationDAO = new NotificationDAO();
    }

    // OPTION 1: Use existing createOrder method with Order object
    public int createOrder(int userId, List<CartItem> cartItems, String shippingAddress, String paymentMethod) {
        try {
            // Calculate total amount
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotalPrice();
            }

            // Create order with confirmed/completed status (payment already done)
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus("confirmed"); // Already confirmed since payment is done
            order.setShippingAddress(shippingAddress);
            order.setPaymentMethod(paymentMethod);
            order.setPaymentStatus("completed"); // Payment already completed

            // Save order
            int orderId = orderDAO.createOrder(order);
            if (orderId == -1) {
                System.err.println("Failed to create order in database");
                return -1;
            }

            // Save order items and update stock
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProductPrice());

                orderDAO.addOrderItem(orderItem);

                // Update product stock
                productDAO.updateStockQuantity(cartItem.getProductId(), -cartItem.getQuantity());
            }

            // Send notification
            notificationDAO.addNotification(
                    new com.revshop.model.Notification(
                            userId,
                            "order_placed",
                            "Your order #" + orderId + " has been placed successfully!"
                    )
            );

            return orderId;
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            return -1;
        }
    }

    // OPTION 2: Use new direct method (more efficient)
    public int createOrderDirect(int userId, List<CartItem> cartItems, String shippingAddress, String paymentMethod) {
        try {
            // Calculate total amount
            double totalAmount = 0;
            for (CartItem item : cartItems) {
                totalAmount += item.getTotalPrice();
            }

            // Payment already processed in BuyerMenu, so create order directly with confirmed status
            int orderId = orderDAO.createOrderDirect(userId, totalAmount, shippingAddress,
                    paymentMethod, "confirmed", "completed");

            if (orderId == -1) {
                System.err.println("Failed to create order in database");
                return -1;
            }

            // Save order items and update stock
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProductPrice());

                orderDAO.addOrderItem(orderItem);

                // Update product stock
                productDAO.updateStockQuantity(cartItem.getProductId(), -cartItem.getQuantity());
            }

            // Send notification
            notificationDAO.addNotification(
                    new com.revshop.model.Notification(
                            userId,
                            "order_placed",
                            "Your order #" + orderId + " has been placed successfully!"
                    )
            );

            return orderId;
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            return -1;
        }
    }

    // Get orders by user
    public List<Order> getOrdersByUser(int userId) {
        try {
            return orderDAO.getOrdersByUser(userId);
        } catch (SQLException e) {
            System.err.println("Error getting orders: " + e.getMessage());
            return List.of();
        }
    }

    // Get order details
    public Order getOrderById(int orderId) {
        try {
            return orderDAO.getOrderById(orderId);
        } catch (SQLException e) {
            System.err.println("Error getting order: " + e.getMessage());
            return null;
        }
    }

    // Get order items
    public List<OrderItem> getOrderItems(int orderId) {
        try {
            return orderDAO.getOrderItems(orderId);
        } catch (SQLException e) {
            System.err.println("Error getting order items: " + e.getMessage());
            return List.of();
        }
    }

    // Cancel order
    public boolean cancelOrder(int orderId, int userId) {
        try {
            Order order = orderDAO.getOrderById(orderId);
            if (order == null || order.getUserId() != userId) {
                return false;
            }

            if (orderDAO.cancelOrder(orderId)) {
                // Restore stock
                List<OrderItem> items = orderDAO.getOrderItems(orderId);
                for (OrderItem item : items) {
                    productDAO.updateStockQuantity(item.getProductId(), item.getQuantity());
                }

                // Send notification
                notificationDAO.addNotification(
                        new com.revshop.model.Notification(
                                userId,
                                "order_cancelled",
                                "Your order #" + orderId + " has been cancelled."
                        )
                );
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error cancelling order: " + e.getMessage());
            return false;
        }
    }

    // Get orders by seller
    public List<Order> getOrdersBySeller(int sellerId) {
        try {
            return orderDAO.getOrdersBySeller(sellerId);
        } catch (SQLException e) {
            System.err.println("Error getting seller orders: " + e.getMessage());
            return List.of();
        }
    }

    // Get total sales for seller
    public double getTotalSalesBySeller(int sellerId) {
        try {
            return orderDAO.getTotalSalesBySeller(sellerId);
        } catch (SQLException e) {
            System.err.println("Error getting total sales: " + e.getMessage());
            return 0.0;
        }
    }

    // Get pending orders count for seller
    public int getPendingOrdersCount(int sellerId) {
        try {
            return orderDAO.getPendingOrdersCount(sellerId);
        } catch (SQLException e) {
            System.err.println("Error getting pending orders count: " + e.getMessage());
            return 0;
        }
    }

    // Update order status (for sellers/admin)
    public boolean updateOrderStatus(int orderId, String status) {
        try {
            boolean success = orderDAO.updateOrderStatus(orderId, status);

            if (success) {
                // Get order to notify user
                Order order = orderDAO.getOrderById(orderId);
                if (order != null) {
                    notificationDAO.addNotification(
                            new com.revshop.model.Notification(
                                    order.getUserId(),
                                    "order_updated",
                                    "Your order #" + orderId + " status updated to: " + status
                            )
                    );
                }
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }
}