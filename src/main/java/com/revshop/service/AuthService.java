package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.dao.SellerDAO;
import com.revshop.model.Buyer;
import com.revshop.model.Seller;
import com.revshop.model.User;
import com.revshop.util.PasswordUtil;
import java.sql.SQLException;

public class AuthService {
    private UserDAO userDAO;
    private SellerDAO sellerDAO;

    public AuthService() {
        userDAO = new UserDAO();
        sellerDAO = new SellerDAO();
    }

    // Register buyer
    public boolean registerBuyer(Buyer buyer) {
        try {
            // Check if email or username already exists
            if (userDAO.emailExists(buyer.getEmail())) {
                System.out.println("Email already registered!");
                return false;
            }
            if (userDAO.usernameExists(buyer.getUsername())) {
                System.out.println("Username already taken!");
                return false;
            }

            // Hash password
            buyer.setPasswordHash(PasswordUtil.hashPassword(buyer.getPasswordHash()));

            // Register user
            User registeredUser = userDAO.registerUser(buyer);

            if (registeredUser != null) {
                // Set the user ID from registration
                buyer.setUserId(registeredUser.getUserId());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Register seller
    public boolean registerSeller(Seller seller) {
        try {
            // Check if email or username already exists
            if (userDAO.emailExists(seller.getEmail())) {
                System.out.println("Email already registered!");
                return false;
            }
            if (userDAO.usernameExists(seller.getUsername())) {
                System.out.println("Username already taken!");
                return false;
            }

            // Hash password
            seller.setPasswordHash(PasswordUtil.hashPassword(seller.getPasswordHash()));

            // Register user
            User registeredUser = userDAO.registerUser(seller);

            if (registeredUser != null) {
                // Set the user ID from registration
                seller.setUserId(registeredUser.getUserId());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Login - FIXED VERSION
    public Object login(String email, String password) {
        try {
            // Hash password for comparison
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Authenticate user
            User user = userDAO.login(email, hashedPassword);

            if (user != null) {
                // Return appropriate user type with FULL user ID
                if (user.getRole().equals("buyer")) {
                    // Create buyer object with ALL properties including user ID
                    Buyer buyer = new Buyer();
                    buyer.setUserId(user.getUserId());  // CRITICAL: Set user ID
                    buyer.setUsername(user.getUsername());
                    buyer.setEmail(user.getEmail());
                    buyer.setPasswordHash(user.getPasswordHash());
                    buyer.setRole(user.getRole());

                    // Try to get additional buyer details
                    try {
                        Buyer detailedBuyer = userDAO.getBuyerDetails(user.getUserId());
                        if (detailedBuyer != null) {
                            buyer.setFirstName(detailedBuyer.getFirstName());
                            buyer.setLastName(detailedBuyer.getLastName());
                            buyer.setPhone(detailedBuyer.getPhone());
                            buyer.setAddress(detailedBuyer.getAddress());
                        }
                    } catch (SQLException e) {
                        System.err.println("Could not load buyer details: " + e.getMessage());
                    }

                    return buyer;
                } else if (user.getRole().equals("seller")) {
                    // Create seller object with ALL properties including user ID
                    Seller seller = new Seller();
                    seller.setUserId(user.getUserId());  // CRITICAL: Set user ID
                    seller.setUsername(user.getUsername());
                    seller.setEmail(user.getEmail());
                    seller.setPasswordHash(user.getPasswordHash());
                    seller.setRole(user.getRole());

                    // Try to get additional seller details
                    try {
                        Seller detailedSeller = sellerDAO.getSellerById(user.getUserId());
                        if (detailedSeller != null) {
                            seller.setBusinessName(detailedSeller.getBusinessName());
                            seller.setBusinessAddress(detailedSeller.getBusinessAddress());
                            seller.setBusinessPhone(detailedSeller.getBusinessPhone());
                            seller.setTaxId(detailedSeller.getTaxId());
                        }
                    } catch (SQLException e) {
                        System.err.println("Could not load seller details: " + e.getMessage());
                    }

                    return seller;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    // Change password
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                return false;
            }

            // Verify old password
            if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
                return false;
            }

            // Update with new hashed password
            String newHashedPassword = PasswordUtil.hashPassword(newPassword);
            return userDAO.updatePassword(userId, newHashedPassword);
        } catch (SQLException e) {
            System.err.println("Change password error: " + e.getMessage());
            return false;
        }
    }

    // Helper method to get full user details (optional)
    public User getUserDetails(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            System.err.println("Error getting user details: " + e.getMessage());
            return null;
        }
    }
}
