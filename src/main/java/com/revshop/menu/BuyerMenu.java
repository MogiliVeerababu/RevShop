package com.revshop.menu;

import com.revshop.model.Buyer;
import com.revshop.model.Product;
import com.revshop.model.CartItem;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.model.Review;
import com.revshop.model.Notification;
import com.revshop.service.BuyerService;
import com.revshop.service.CartService;
import com.revshop.service.OrderService;
import com.revshop.service.ProductService;
import com.revshop.service.AuthService;
import com.revshop.util.ConsoleColors;
import com.revshop.util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

public class BuyerMenu {
    private Scanner scanner;
    private BuyerService buyerService;
    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;
    private AuthService authService;

    public BuyerMenu() {
        scanner = new Scanner(System.in);
        buyerService = new BuyerService();
        productService = new ProductService();
        cartService = new CartService();
        orderService = new OrderService();
        authService = new AuthService();
    }

    public void show(Buyer buyer) {
        System.out.println(ConsoleColors.GREEN +
                "\nWelcome, " + buyer.getFirstName() + "!" + ConsoleColors.RESET);

        while (true) {
            // Show notification count
            int unreadCount = buyerService.getUnreadNotificationCount(buyer.getUserId());
            if (unreadCount > 0) {
                System.out.println(ConsoleColors.YELLOW +
                        "You have " + unreadCount + " unread notifications!" + ConsoleColors.RESET);
            }

            System.out.println(ConsoleColors.CYAN_BOLD +
                    "\n=== BUYER DASHBOARD ===");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Products");
            System.out.println("3. View Cart");
            System.out.println("4. View Orders");
            System.out.println("5. View Favorites");  // NEW OPTION
            System.out.println("6. View Notifications");  // Renumbered from 5
            System.out.println("7. View Profile");  // Renumbered from 6
            System.out.println("8. Change Password");  // Renumbered from 7
            System.out.println("9. Logout");  // Renumbered from 8

            System.out.print("Choose an option: " + ConsoleColors.RESET);

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    browseProducts(buyer);
                    break;
                case 2:
                    searchProducts(buyer);
                    break;
                case 3:
                    viewCart(buyer);
                    break;
                case 4:
                    viewOrders(buyer);
                    break;
                case 5:  // NEW CASE FOR FAVORITES
                    viewFavorites(buyer);
                    break;
                case 6:  // Renumbered from 5
                    viewNotifications(buyer);
                    break;
                case 7:  // Renumbered from 6
                    viewProfile(buyer);
                    break;
                case 8:  // Renumbered from 7
                    changePassword(buyer);
                    break;
                case 9:  // Renumbered from 8
                    System.out.println(ConsoleColors.YELLOW +
                            "Logging out..." + ConsoleColors.RESET);
                    return;
                default:
                    System.out.println(ConsoleColors.RED +
                            "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void browseProducts(Buyer buyer) {
        while (true) {
            System.out.println(ConsoleColors.BLUE_BOLD +
                    "\n=== BROWSE PRODUCTS ===" + ConsoleColors.RESET);

            System.out.println("1. View All Products");
            System.out.println("2. View by Category");
            System.out.println("3. Back");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    viewAllProducts(buyer);
                    break;
                case 2:
                    browseByCategory(buyer);
                    break;
                case 3:
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void viewAllProducts(Buyer buyer) {
        try {
            List<Product> products = productService.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products available.");
                return;
            }

            displayProducts(products);
            System.out.println("\nEnter product ID to view details (0 to go back): ");
            int productId = getIntInput();

            if (productId > 0) {
                Product product = productService.getProductById(productId);
                if (product != null) {
                    viewProductDetails(product, buyer);
                } else {
                    System.out.println(ConsoleColors.RED + "Product not found!" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void browseByCategory(Buyer buyer) {
        System.out.print("Enter category: ");
        String category = scanner.nextLine().trim();

        try {
            List<Product> products = productService.getProductsByCategory(category);

            if (products.isEmpty()) {
                System.out.println("No products found in category: " + category);
                return;
            }

            displayProducts(products);
            System.out.println("\nEnter product ID to view details (0 to go back): ");
            int productId = getIntInput();

            if (productId > 0) {
                Product product = productService.getProductById(productId);
                if (product != null) {
                    viewProductDetails(product, buyer);
                } else {
                    System.out.println(ConsoleColors.RED + "Product not found!" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void searchProducts(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== SEARCH PRODUCTS ===" + ConsoleColors.RESET);

        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty!");
            return;
        }

        try {
            List<Product> products = productService.searchProducts(keyword);

            if (products.isEmpty()) {
                System.out.println("No products found matching: " + keyword);
                return;
            }

            displayProducts(products);
            System.out.println("\nEnter product ID to view details (0 to go back): ");
            int productId = getIntInput();

            if (productId > 0) {
                Product product = productService.getProductById(productId);
                if (product != null) {
                    viewProductDetails(product, buyer);
                } else {
                    System.out.println(ConsoleColors.RED + "Product not found!" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void viewProductDetails(Product product, Buyer buyer) {
        while (true) {
            System.out.println(ConsoleColors.BLUE_BOLD +
                    "\n=== PRODUCT DETAILS ===" + ConsoleColors.RESET);

            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: $" + String.format("%.2f", product.getPrice()));
            System.out.println("MRP: $" + String.format("%.2f", product.getMrp()));
            System.out.println("Discount: " + String.format("%.1f", product.getDiscountPercentage()) + "%");
            System.out.println("Stock: " + product.getStockQuantity());
            System.out.println("Category: " + product.getCategory());

            // Show average rating
            double avgRating = buyerService.getAverageRating(product.getProductId());
            System.out.println("Rating: " + String.format("%.1f", avgRating) + "/5");

            // Check if already in favorites
            boolean isFavorite = buyerService.isProductInFavorites(buyer.getUserId(), product.getProductId());
            if (isFavorite) {
                System.out.println(ConsoleColors.YELLOW + "★ Already in your favorites!" + ConsoleColors.RESET);
            }

            System.out.println("\nOptions:");
            System.out.println("1. Add to Cart");
            System.out.println("2. View Reviews");
            System.out.println("3. Add Review");
            if (isFavorite) {
                System.out.println("4. Remove from Favorites");
            } else {
                System.out.println("4. Add to Favorites");
            }
            System.out.println("5. Back");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addToCart(product, buyer);
                    break;
                case 2:
                    viewProductReviews(product.getProductId());
                    break;
                case 3:
                    addProductReview(product.getProductId(), buyer.getUserId());
                    break;
                case 4:
                    if (isFavorite) {
                        removeFromFavorites(buyer.getUserId(), product.getProductId());
                        System.out.println(ConsoleColors.GREEN + "✓ Removed from favorites!" + ConsoleColors.RESET);
                    } else {
                        addToFavorites(buyer.getUserId(), product.getProductId());
                        System.out.println(ConsoleColors.GREEN + "✓ Added to favorites!" + ConsoleColors.RESET);
                    }
                    // Refresh favorite status
                    isFavorite = !isFavorite;
                    break;
                case 5:
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void addToCart(Product product, Buyer buyer) {
        System.out.print("Enter quantity: ");
        int quantity = getIntInput();

        if (quantity <= 0) {
            System.out.println(ConsoleColors.RED + "Quantity must be positive!" + ConsoleColors.RESET);
            return;
        }

        if (quantity > product.getStockQuantity()) {
            System.out.println(ConsoleColors.RED +
                    "Only " + product.getStockQuantity() + " items available!" + ConsoleColors.RESET);
            return;
        }

        try {
            boolean success = cartService.addToCart(buyer.getUserId(), product.getProductId(), quantity);
            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "Added to cart successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "Failed to add to cart!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void viewProductReviews(int productId) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== PRODUCT REVIEWS ===" + ConsoleColors.RESET);

        List<Review> reviews = buyerService.getProductReviews(productId);

        if (reviews.isEmpty()) {
            System.out.println("No reviews yet.");
            return;
        }

        for (Review review : reviews) {
            System.out.println(review);
        }
    }

    private void addProductReview(int productId, int userId) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== ADD REVIEW ===" + ConsoleColors.RESET);

        System.out.print("Enter rating (1-5): ");
        int rating = getIntInput();

        if (rating < 1 || rating > 5) {
            System.out.println(ConsoleColors.RED + "Rating must be between 1 and 5!" + ConsoleColors.RESET);
            return;
        }

        System.out.print("Enter comment: ");
        String comment = scanner.nextLine().trim();

        if (comment.isEmpty()) {
            System.out.println("Comment cannot be empty!");
            return;
        }

        boolean success = buyerService.addReview(userId, productId, rating, comment);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Review added successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to add review!" + ConsoleColors.RESET);
        }
    }

    private void viewCart(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== SHOPPING CART ===" + ConsoleColors.RESET);

        try {
            List<CartItem> cartItems = cartService.getCartItems(buyer.getUserId());

            if (cartItems.isEmpty()) {
                System.out.println("Your cart is empty!");
                return;
            }

            displayCartItems(cartItems);

            System.out.println("\nOptions:");
            System.out.println("1. Checkout");
            System.out.println("2. Update Quantity");
            System.out.println("3. Remove Item");
            System.out.println("4. Clear Cart");
            System.out.println("5. Back");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    checkout(buyer, cartItems);
                    break;
                case 2:
                    updateCartQuantity(buyer);
                    break;
                case 3:
                    removeFromCart(buyer);
                    break;
                case 4:
                    clearCart(buyer);
                    break;
                case 5:
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void displayCartItems(List<CartItem> cartItems) {
        double total = 0;
        int index = 1;

        for (CartItem item : cartItems) {
            System.out.printf("%d. %s x%d @ $%.2f each = $%.2f%n",
                    index++,
                    item.getProductName(),
                    item.getQuantity(),
                    item.getProductPrice(),
                    item.getTotalPrice());
            total += item.getTotalPrice();
        }

        System.out.println(ConsoleColors.YELLOW_BOLD +
                "Total: $" + String.format("%.2f", total) + ConsoleColors.RESET);
    }

    private void checkout(Buyer buyer, List<CartItem> cartItems) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== CHECKOUT ===" + ConsoleColors.RESET);

        double total = cartService.getCartTotal(buyer.getUserId());
        System.out.println("Total Amount: $" + String.format("%.2f", total));

        System.out.print("Enter shipping address: ");
        String address = scanner.nextLine().trim();

        if (address.isEmpty()) {
            System.out.println("Address cannot be empty!");
            return;
        }

        System.out.println("\nPayment Methods:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. PayPal");
        System.out.println("4. Cancel");
        System.out.print("Choose payment method: ");

        int paymentChoice = getIntInput();
        if (paymentChoice == 4) {
            return;
        }

        String paymentMethod = getPaymentMethod(paymentChoice);
        if (paymentMethod.equals("Unknown")) {
            System.out.println(ConsoleColors.RED + "Invalid payment method!" + ConsoleColors.RESET);
            return;
        }

        try {
            int orderId = orderService.createOrder(buyer.getUserId(), cartItems, address, paymentMethod);
            if (orderId > 0) {
                System.out.println(ConsoleColors.GREEN +
                        "Order placed successfully! Order ID: " + orderId + ConsoleColors.RESET);
                cartService.clearCart(buyer.getUserId());
            } else {
                System.out.println(ConsoleColors.RED +
                        "Failed to place order!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Error during checkout: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private String getPaymentMethod(int choice) {
        switch (choice) {
            case 1: return "Credit Card";
            case 2: return "Debit Card";
            case 3: return "PayPal";
            default: return "Unknown";
        }
    }

    private void updateCartQuantity(Buyer buyer) {
        System.out.print("Enter item number to update: ");
        int itemNumber = getIntInput();

        List<CartItem> items = cartService.getCartItems(buyer.getUserId());
        if (itemNumber < 1 || itemNumber > items.size()) {
            System.out.println(ConsoleColors.RED + "Invalid item number!" + ConsoleColors.RESET);
            return;
        }

        CartItem item = items.get(itemNumber - 1);
        System.out.print("Enter new quantity: ");
        int newQuantity = getIntInput();

        if (newQuantity <= 0) {
            System.out.println(ConsoleColors.RED + "Quantity must be positive!" + ConsoleColors.RESET);
            return;
        }

        boolean success = cartService.updateCartItemQuantity(item.getCartItemId(), newQuantity);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Quantity updated successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to update quantity!" + ConsoleColors.RESET);
        }
    }

    private void removeFromCart(Buyer buyer) {
        System.out.print("Enter item number to remove: ");
        int itemNumber = getIntInput();

        List<CartItem> items = cartService.getCartItems(buyer.getUserId());
        if (itemNumber < 1 || itemNumber > items.size()) {
            System.out.println(ConsoleColors.RED + "Invalid item number!" + ConsoleColors.RESET);
            return;
        }

        CartItem item = items.get(itemNumber - 1);
        boolean success = cartService.removeFromCart(item.getCartItemId());
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Item removed successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to remove item!" + ConsoleColors.RESET);
        }
    }

    private void clearCart(Buyer buyer) {
        System.out.print("Are you sure you want to clear your cart? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            boolean success = cartService.clearCart(buyer.getUserId());
            if (success) {
                System.out.println(ConsoleColors.GREEN + "Cart cleared successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Failed to clear cart!" + ConsoleColors.RESET);
            }
        }
    }

    private void viewOrders(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== ORDER HISTORY ===" + ConsoleColors.RESET);

        List<Order> orders = orderService.getOrdersByUser(buyer.getUserId());

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            System.out.printf("Order #%d | Date: %s | Amount: $%.2f | Status: %s | Payment: %s%n",
                    order.getOrderId(),
                    order.getCreatedAt(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getPaymentStatus());
        }

        System.out.println("\nEnter order ID to view details (0 to go back): ");
        int orderId = getIntInput();

        if (orderId > 0) {
            viewOrderDetails(orderId, buyer);
        }
    }

    private void viewOrderDetails(int orderId, Buyer buyer) {
        Order order = orderService.getOrderById(orderId);

        if (order == null || order.getUserId() != buyer.getUserId()) {
            System.out.println(ConsoleColors.RED + "Order not found!" + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== ORDER DETAILS ===" + ConsoleColors.RESET);

        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Date: " + order.getCreatedAt());
        System.out.println("Total Amount: $" + String.format("%.2f", order.getTotalAmount()));
        System.out.println("Status: " + order.getStatus());
        System.out.println("Payment Method: " + order.getPaymentMethod());
        System.out.println("Payment Status: " + order.getPaymentStatus());
        System.out.println("Shipping Address: " + order.getShippingAddress());

        System.out.println("\nOrder Items:");
        List<OrderItem> items = orderService.getOrderItems(orderId);
        for (OrderItem item : items) {
            System.out.printf("  %s x%d @ $%.2f each = $%.2f%n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTotalPrice());
        }

        System.out.println("\nOptions:");
        System.out.println("1. Cancel Order");
        System.out.println("2. Add Review for Items");
        System.out.println("3. Back");
        System.out.print("Choose: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                cancelOrder(orderId, buyer);
                break;
            case 2:
                addOrderReviews(orderId, buyer.getUserId());
                break;
            case 3:
                return;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
        }
    }

    private void cancelOrder(int orderId, Buyer buyer) {
        System.out.print("Are you sure you want to cancel this order? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            boolean success = orderService.cancelOrder(orderId, buyer.getUserId());
            if (success) {
                System.out.println(ConsoleColors.GREEN + "Order cancelled successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "Cannot cancel order. It may already be shipped or delivered." + ConsoleColors.RESET);
            }
        }
    }

    private void addOrderReviews(int orderId, int userId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);

        if (items.isEmpty()) {
            System.out.println("No items in this order.");
            return;
        }

        for (OrderItem item : items) {
            System.out.println("\nProduct: " + item.getProductName());
            System.out.print("Would you like to review this product? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y") || response.equals("yes")) {
                addProductReview(item.getProductId(), userId);
            }
        }
    }

    private void viewNotifications(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== NOTIFICATIONS ===" + ConsoleColors.RESET);

        List<Notification> notifications = buyerService.getNotifications(buyer.getUserId());

        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        int index = 1;
        for (Notification notification : notifications) {
            String status = notification.isRead() ? "[Read]" : "[Unread]";
            System.out.printf("%d. %s %s - %s%n",
                    index++,
                    status,
                    notification.getType(),
                    notification.getMessage());
        }

        System.out.println("\nOptions:");
        System.out.println("1. Mark as Read");
        System.out.println("2. Mark All as Read");
        System.out.println("3. Back");
        System.out.print("Choose: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                markNotificationAsRead(buyer.getUserId());
                break;
            case 2:
                markAllNotificationsAsRead(buyer.getUserId());
                break;
            case 3:
                return;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
        }
    }

    private void markNotificationAsRead(int userId) {
        System.out.print("Enter notification number to mark as read: ");
        int notifNumber = getIntInput();

        List<Notification> notifications = buyerService.getNotifications(userId);
        if (notifNumber < 1 || notifNumber > notifications.size()) {
            System.out.println(ConsoleColors.RED + "Invalid notification number!" + ConsoleColors.RESET);
            return;
        }

        Notification notification = notifications.get(notifNumber - 1);
        boolean success = buyerService.markNotificationAsRead(notification.getNotificationId());
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Notification marked as read!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to mark notification!" + ConsoleColors.RESET);
        }
    }

    private void markAllNotificationsAsRead(int userId) {
        boolean success = buyerService.markAllNotificationsAsRead(userId);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "All notifications marked as read!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to mark notifications!" + ConsoleColors.RESET);
        }
    }

    private void viewProfile(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== YOUR PROFILE ===" + ConsoleColors.RESET);

        System.out.println("Username: " + buyer.getUsername());
        System.out.println("Email: " + buyer.getEmail());
        System.out.println("First Name: " + buyer.getFirstName());
        System.out.println("Last Name: " + buyer.getLastName());
        System.out.println("Phone: " + buyer.getPhone());
        System.out.println("Address: " + buyer.getAddress());

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void changePassword(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== CHANGE PASSWORD ===" + ConsoleColors.RESET);

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println(ConsoleColors.RED + "Passwords do not match!" + ConsoleColors.RESET);
            return;
        }

        boolean success = authService.changePassword(buyer.getUserId(), currentPassword, newPassword);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Password changed successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to change password!" + ConsoleColors.RESET);
        }
    }

    private void displayProducts(List<Product> products) {
        System.out.println("\nAvailable Products:");
        for (Product product : products) {
            System.out.printf("ID: %d | %s | $%.2f | Stock: %d | Category: %s%n",
                    product.getProductId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getCategory());
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }
    // ============ FAVORITE METHODS ============ //

    private void viewFavorites(Buyer buyer) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== MY FAVORITE PRODUCTS ===" + ConsoleColors.RESET);

        try {
            List<Product> favorites = buyerService.getFavorites(buyer.getUserId());

            if (favorites.isEmpty()) {
                System.out.println("You have no favorite products yet.");
                System.out.println("Browse products and add them to favorites!");
                return;
            }

            System.out.println("You have " + favorites.size() + " favorite product(s):");
            displayProducts(favorites);

            System.out.println("\nEnter product ID to view details (0 to go back): ");
            int productId = getIntInput();

            if (productId > 0) {
                // Check if product is in favorites
                if (buyerService.isProductInFavorites(buyer.getUserId(), productId)) {
                    Product product = productService.getProductById(productId);
                    if (product != null) {
                        viewFavoriteProductDetails(product, buyer);
                    } else {
                        System.out.println(ConsoleColors.RED + "Product not found!" + ConsoleColors.RESET);
                    }
                } else {
                    System.out.println(ConsoleColors.RED + "Product not in your favorites!" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void viewFavoriteProductDetails(Product product, Buyer buyer) {
        while (true) {
            System.out.println(ConsoleColors.BLUE_BOLD +
                    "\n=== FAVORITE PRODUCT DETAILS ===" + ConsoleColors.RESET);

            System.out.println("ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: $" + String.format("%.2f", product.getPrice()));
            System.out.println("MRP: $" + String.format("%.2f", product.getMrp()));
            System.out.println("Discount: " + String.format("%.1f", product.getDiscountPercentage()) + "%");
            System.out.println("Stock: " + product.getStockQuantity());
            System.out.println("Category: " + product.getCategory());

            double avgRating = buyerService.getAverageRating(product.getProductId());
            System.out.println("Rating: " + String.format("%.1f", avgRating) + "/5");

            System.out.println("\nOptions:");
            System.out.println("1. Add to Cart");
            System.out.println("2. View Reviews");
            System.out.println("3. Add Review");
            System.out.println("4. Remove from Favorites");
            System.out.println("5. Back to Favorites List");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addToCart(product, buyer);
                    break;
                case 2:
                    viewProductReviews(product.getProductId());
                    break;
                case 3:
                    addProductReview(product.getProductId(), buyer.getUserId());
                    break;
                case 4:
                    removeFromFavorites(buyer.getUserId(), product.getProductId());
                    System.out.println(ConsoleColors.GREEN + "✓ Removed from favorites!" + ConsoleColors.RESET);
                    return; // Go back to favorites list
                case 5:
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void addToFavorites(int userId, int productId) {
        try {
            boolean success = buyerService.addToFavorites(userId, productId);
            if (success) {
                System.out.println(ConsoleColors.GREEN + "✓ Added to favorites!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.YELLOW + "Already in favorites!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void removeFromFavorites(int userId, int productId) {
        try {
            boolean success = buyerService.removeFromFavorites(userId, productId);
            if (success) {
                System.out.println(ConsoleColors.GREEN + "✓ Removed from favorites!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Failed to remove from favorites!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}