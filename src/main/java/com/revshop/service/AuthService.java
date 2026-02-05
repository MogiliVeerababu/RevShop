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

    // Register buyer - WITH DEBUG LOGGING
    public boolean registerBuyer(Buyer buyer) {
        System.out.println("🔍 [DEBUG] Starting buyer registration...");
        System.out.println("🔍 [DEBUG] Username: " + buyer.getUsername());
        System.out.println("🔍 [DEBUG] Email: " + buyer.getEmail());
        System.out.println("🔍 [DEBUG] Security Q: " + buyer.getSecurityQuestion());
        System.out.println("🔍 [DEBUG] Security A: " + buyer.getSecurityAnswer());

        try {
            // Check if email already exists
            System.out.println("🔍 [DEBUG] Checking if email exists: " + buyer.getEmail());
            boolean emailExists = userDAO.emailExists(buyer.getEmail());
            System.out.println("🔍 [DEBUG] Email exists? " + emailExists);

            if (emailExists) {
                System.out.println("❌ [DEBUG] Email already registered: " + buyer.getEmail());
                return false;
            }

            // Check if username already exists
            System.out.println("🔍 [DEBUG] Checking if username exists: " + buyer.getUsername());
            boolean usernameExists = userDAO.usernameExists(buyer.getUsername());
            System.out.println("🔍 [DEBUG] Username exists? " + usernameExists);

            if (usernameExists) {
                System.out.println("❌ [DEBUG] Username already taken: " + buyer.getUsername());
                return false;
            }

            // Hash password
            System.out.println("🔍 [DEBUG] Hashing password...");
            String hashedPassword = PasswordUtil.hashPassword(buyer.getPasswordHash());
            buyer.setPasswordHash(hashedPassword);

            // Register user
            System.out.println("🔍 [DEBUG] Calling userDAO.registerUser()...");
            User registeredUser = userDAO.registerUser(buyer);

            if (registeredUser != null) {
                // Set the user ID from registration
                buyer.setUserId(registeredUser.getUserId());
                System.out.println("✅ [DEBUG] Registration SUCCESS! User ID: " + buyer.getUserId());
                return true;
            } else {
                System.out.println("❌ [DEBUG] Registration failed - registeredUser is null");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ [DEBUG] SQL Exception during registration: " + e.getMessage());
            e.printStackTrace();  // This will show the full error
            return false;
        }
    }

    // Register seller - WITH DEBUG LOGGING
    public boolean registerSeller(Seller seller) {
        System.out.println("🔍 [DEBUG] Starting seller registration...");
        System.out.println("🔍 [DEBUG] Username: " + seller.getUsername());
        System.out.println("🔍 [DEBUG] Email: " + seller.getEmail());

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
            e.printStackTrace();
            return false;
        }
    }

    // Login - UPDATED to include security info
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
                    buyer.setUserId(user.getUserId());
                    buyer.setUsername(user.getUsername());
                    buyer.setEmail(user.getEmail());
                    buyer.setPasswordHash(user.getPasswordHash());
                    buyer.setRole(user.getRole());
                    buyer.setSecurityQuestion(user.getSecurityQuestion());
                    buyer.setSecurityAnswer(user.getSecurityAnswer());

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
                    seller.setUserId(user.getUserId());
                    seller.setUsername(user.getUsername());
                    seller.setEmail(user.getEmail());
                    seller.setPasswordHash(user.getPasswordHash());
                    seller.setRole(user.getRole());
                    seller.setSecurityQuestion(user.getSecurityQuestion());
                    seller.setSecurityAnswer(user.getSecurityAnswer());

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

    // NEW: Find user by email (for forgot password)
    public User findUserByEmail(String email) {
        try {
            return userDAO.getUserByEmail(email);
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
            return null;
        }
    }

    // NEW: Find user by username (for forgot email)
    public User findUserByUsername(String username) {
        try {
            return userDAO.getUserByUsername(username);
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            return null;
        }
    }

    // NEW: Reset password
    public boolean resetPassword(String email, String newPassword) {
        try {
            // Hash the new password
            String hashedPassword = PasswordUtil.hashPassword(newPassword);
            return userDAO.updatePasswordByEmail(email, hashedPassword);
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        }
    }

    // Change password (existing)
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

    // Helper method to get full user details
    public User getUserDetails(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (SQLException e) {
            System.err.println("Error getting user details: " + e.getMessage());
            return null;
        }
    }
}