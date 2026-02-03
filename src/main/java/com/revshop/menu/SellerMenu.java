
package com.revshop.menu;

import com.revshop.model.Seller;
import com.revshop.model.Product;
import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import com.revshop.service.SellerService;
import com.revshop.service.ProductService;
import com.revshop.service.OrderService;
import com.revshop.service.AuthService;
import com.revshop.service.NotificationService;
import com.revshop.util.ConsoleColors;
import com.revshop.util.ValidationUtil;
import java.util.List;
import java.util.Scanner;

public class SellerMenu {
    private Scanner scanner;
    private SellerService sellerService;
    private ProductService productService;
    private OrderService orderService;
    private AuthService authService;
    private NotificationService notificationService;

    public SellerMenu() {
        scanner = new Scanner(System.in);
        sellerService = new SellerService();
        productService = new ProductService();
        orderService = new OrderService();
        authService = new AuthService();
        notificationService = new NotificationService();
    }

    public void show(Seller seller) {
        System.out.println(ConsoleColors.GREEN +
                "\nWelcome, " + seller.getBusinessName() + "!" + ConsoleColors.RESET);

        while (true) {
            System.out.println(ConsoleColors.CYAN_BOLD +
                    "\n=== SELLER DASHBOARD ===");
            System.out.println("1. Manage Products");
            System.out.println("2. View Orders");
            System.out.println("3. View Low Stock Products");
            System.out.println("4. View Profile");
            System.out.println("5. Change Password");
            System.out.println("6. Logout");
            System.out.print("Choose an option: " + ConsoleColors.RESET);

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    manageProducts(seller);
                    break;
                case 2:
                    viewOrders(seller);
                    break;
                case 3:
                    viewLowStockProducts(seller);
                    break;
                case 4:
                    viewProfile(seller);
                    break;
                case 5:
                    changePassword(seller);
                    break;
                case 6:
                    System.out.println(ConsoleColors.YELLOW +
                            "Logging out..." + ConsoleColors.RESET);
                    return;
                default:
                    System.out.println(ConsoleColors.RED +
                            "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void manageProducts(Seller seller) {
        while (true) {
            System.out.println(ConsoleColors.BLUE_BOLD +
                    "\n=== MANAGE PRODUCTS ===" + ConsoleColors.RESET);

            System.out.println("1. Add New Product");
            System.out.println("2. View My Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Back");
            System.out.print("Choose: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addProduct(seller);
                    break;
                case 2:
                    viewMyProducts(seller);
                    break;
                case 3:
                    updateProduct(seller);
                    break;
                case 4:
                    deleteProduct(seller);
                    break;
                case 5:
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
            }
        }
    }

    private void addProduct(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== ADD NEW PRODUCT ===" + ConsoleColors.RESET);

        try {
            System.out.print("Product Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            double price = ValidationUtil.getPositiveDouble("Price: $");
            double mrp = ValidationUtil.getPositiveDouble("MRP: $");

            System.out.print("Category: ");
            String category = scanner.nextLine().trim();

            int stockQuantity = ValidationUtil.getPositiveInt("Stock Quantity: ");

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setMrp(mrp);
            product.setDiscountedPrice(price); // Initially same as price
            product.setCategory(category);
            product.setStockQuantity(stockQuantity);
            product.setSellerId(seller.getUserId());

            boolean success = productService.addProduct(product);
            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "Product added successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "Failed to add product!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void viewMyProducts(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== MY PRODUCTS ===" + ConsoleColors.RESET);

        List<Product> products = productService.getProductsBySeller(seller.getUserId());

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        displaySellerProducts(products);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void displaySellerProducts(List<Product> products) {
        for (Product product : products) {
            System.out.printf("ID: %d | %s | Price: $%.2f | MRP: $%.2f | Stock: %d | Category: %s%n",
                    product.getProductId(),
                    product.getName(),
                    product.getPrice(),
                    product.getMrp(),
                    product.getStockQuantity(),
                    product.getCategory());
        }
    }

    private void updateProduct(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== UPDATE PRODUCT ===" + ConsoleColors.RESET);

        System.out.print("Enter Product ID to update: ");
        int productId = getIntInput();

        Product product = productService.getProductById(productId);
        if (product == null || product.getSellerId() != seller.getUserId()) {
            System.out.println(ConsoleColors.RED +
                    "Product not found or you don't have permission!" + ConsoleColors.RESET);
            return;
        }

        System.out.println("\nCurrent Details:");
        System.out.println("1. Name: " + product.getName());
        System.out.println("2. Description: " + product.getDescription());
        System.out.println("3. Price: $" + product.getPrice());
        System.out.println("4. MRP: $" + product.getMrp());
        System.out.println("5. Stock: " + product.getStockQuantity());
        System.out.println("6. Category: " + product.getCategory());

        System.out.print("\nEnter field number to update (0 to cancel): ");
        int field = getIntInput();

        if (field == 0) return;

        switch (field) {
            case 1:
                System.out.print("New Name: ");
                product.setName(scanner.nextLine().trim());
                break;
            case 2:
                System.out.print("New Description: ");
                product.setDescription(scanner.nextLine().trim());
                break;
            case 3:
                product.setPrice(ValidationUtil.getPositiveDouble("New Price: $"));
                break;
            case 4:
                product.setMrp(ValidationUtil.getPositiveDouble("New MRP: $"));
                break;
            case 5:
                product.setStockQuantity(ValidationUtil.getPositiveInt("New Stock Quantity: "));
                break;
            case 6:
                System.out.print("New Category: ");
                product.setCategory(scanner.nextLine().trim());
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid field!" + ConsoleColors.RESET);
                return;
        }

        boolean success = productService.updateProduct(product);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Product updated successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to update product!" + ConsoleColors.RESET);
        }
    }

    private void deleteProduct(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== DELETE PRODUCT ===" + ConsoleColors.RESET);

        System.out.print("Enter Product ID to delete: ");
        int productId = getIntInput();

        Product product = productService.getProductById(productId);
        if (product == null || product.getSellerId() != seller.getUserId()) {
            System.out.println(ConsoleColors.RED +
                    "Product not found or you don't have permission!" + ConsoleColors.RESET);
            return;
        }

        System.out.print("Are you sure you want to delete \"" + product.getName() + "\"? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            boolean success = productService.deleteProduct(productId);
            if (success) {
                System.out.println(ConsoleColors.GREEN + "Product deleted successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Failed to delete product!" + ConsoleColors.RESET);
            }
        }
    }

    private void viewOrders(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== ORDERS ===" + ConsoleColors.RESET);

        List<Order> orders = orderService.getOrdersBySeller(seller.getUserId());

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            System.out.printf("Order #%d | Date: %s | Amount: $%.2f | Status: %s%n",
                    order.getOrderId(),
                    order.getCreatedAt(),
                    order.getTotalAmount(),
                    order.getStatus());
        }

        System.out.println("\nEnter order ID to view details (0 to go back): ");
        int orderId = getIntInput();

        if (orderId > 0) {
            viewOrderDetails(orderId, seller);
        }
    }

    private void viewOrderDetails(int orderId, Seller seller) {
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
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

        System.out.println("\nOrder Items (Your Products):");
        List<OrderItem> allItems = orderService.getOrderItems(orderId);
        List<OrderItem> sellerItems = allItems.stream()
                .filter(item -> {
                    Product product = productService.getProductById(item.getProductId());
                    return product != null && product.getSellerId() == seller.getUserId();
                })
                .toList();

        if (sellerItems.isEmpty()) {
            System.out.println("No items from your products in this order.");
        } else {
            for (OrderItem item : sellerItems) {
                System.out.printf("  %s x%d @ $%.2f each = $%.2f%n",
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getTotalPrice());
            }
        }

        if (order.getStatus().equals("pending") || order.getStatus().equals("confirmed")) {
            System.out.println("\nOptions:");
            System.out.println("1. Update Order Status");
            System.out.println("2. Back");
            System.out.print("Choose: ");

            int choice = getIntInput();

            if (choice == 1) {
                updateOrderStatus(orderId);
            }
        }
    }

    private void updateOrderStatus(int orderId) {
        System.out.println("\nUpdate Order Status:");
        System.out.println("1. Mark as Shipped");
        System.out.println("2. Mark as Delivered");
        System.out.println("3. Cancel");
        System.out.print("Choose: ");

        int choice = getIntInput();

        String newStatus = "";
        switch (choice) {
            case 1:
                newStatus = "shipped";
                break;
            case 2:
                newStatus = "delivered";
                break;
            case 3:
                return;
            default:
                System.out.println(ConsoleColors.RED + "Invalid choice!" + ConsoleColors.RESET);
                return;
        }

        boolean success = orderService.updateOrderStatus(orderId, newStatus);
        if (success) {
            System.out.println(ConsoleColors.GREEN +
                    "Order status updated to: " + newStatus + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to update order status!" + ConsoleColors.RESET);
        }
    }

    private void viewLowStockProducts(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== LOW STOCK PRODUCTS ===" + ConsoleColors.RESET);

        System.out.print("Enter stock threshold: ");
        int threshold = getIntInput();

        List<Product> lowStockProducts = productService.getLowStockProducts(seller.getUserId(), threshold);

        if (lowStockProducts.isEmpty()) {
            System.out.println("No products below threshold of " + threshold);
            return;
        }

        System.out.println(ConsoleColors.YELLOW +
                "Warning: The following products are low in stock:" + ConsoleColors.RESET);

        for (Product product : lowStockProducts) {
            System.out.printf("ID: %d | %s | Stock: %d | Category: %s%n",
                    product.getProductId(),
                    product.getName(),
                    product.getStockQuantity(),
                    product.getCategory());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private void viewProfile(Seller seller) {
        Seller currentSeller = sellerService.getSellerDetails(seller.getUserId());

        if (currentSeller == null) {
            System.out.println(ConsoleColors.RED + "Error loading profile!" + ConsoleColors.RESET);
            return;
        }

        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== YOUR PROFILE ===" + ConsoleColors.RESET);

        System.out.println("Username: " + currentSeller.getUsername());
        System.out.println("Email: " + currentSeller.getEmail());
        System.out.println("Business Name: " + currentSeller.getBusinessName());
        System.out.println("Business Address: " + currentSeller.getBusinessAddress());
        System.out.println("Business Phone: " + currentSeller.getBusinessPhone());
        System.out.println("Tax ID: " + currentSeller.getTaxId());

        System.out.println("\nOptions:");
        System.out.println("1. Update Profile");
        System.out.println("2. Back");
        System.out.print("Choose: ");

        int choice = getIntInput();

        if (choice == 1) {
            updateProfile(currentSeller);
        }
    }

    private void updateProfile(Seller seller) {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== UPDATE PROFILE ===" + ConsoleColors.RESET);

        System.out.println("Current Details:");
        System.out.println("1. Business Name: " + seller.getBusinessName());
        System.out.println("2. Business Address: " + seller.getBusinessAddress());
        System.out.println("3. Business Phone: " + seller.getBusinessPhone());
        System.out.println("4. Tax ID: " + seller.getTaxId());

        System.out.print("\nEnter field number to update (0 to cancel): ");
        int field = getIntInput();

        if (field == 0) return;

        switch (field) {
            case 1:
                System.out.print("New Business Name: ");
                seller.setBusinessName(scanner.nextLine().trim());
                break;
            case 2:
                System.out.print("New Business Address: ");
                seller.setBusinessAddress(scanner.nextLine().trim());
                break;
            case 3:
                System.out.print("New Business Phone: ");
                seller.setBusinessPhone(scanner.nextLine().trim());
                break;
            case 4:
                System.out.print("New Tax ID: ");
                seller.setTaxId(scanner.nextLine().trim());
                break;
            default:
                System.out.println(ConsoleColors.RED + "Invalid field!" + ConsoleColors.RESET);
                return;
        }

        boolean success = sellerService.updateSellerDetails(seller);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Profile updated successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to update profile!" + ConsoleColors.RESET);
        }
    }

    private void changePassword(Seller seller) {
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

        boolean success = authService.changePassword(seller.getUserId(), currentPassword, newPassword);
        if (success) {
            System.out.println(ConsoleColors.GREEN + "Password changed successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Failed to change password!" + ConsoleColors.RESET);
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
}