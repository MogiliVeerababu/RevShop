package com.revshop;

import com.revshop.dao.CartDAO;
import com.revshop.dao.UserDAO;
import com.revshop.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DebugCart {
    public static void main(String[] args) {
        try {
            System.out.println("=== Debugging Cart Issue ===");

            // Test database connection
            Connection conn = DatabaseUtil.getConnection();
            System.out.println("1. Database connection: OK");

            // Check users table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                System.out.println("2. Users in database: " + rs.getInt("count"));
            }

            // Check specific user (assuming you're using user ID 1)
            rs = stmt.executeQuery("SELECT * FROM users WHERE user_id = 1");
            if (rs.next()) {
                System.out.println("3. User ID 1 exists: " + rs.getString("username"));
            } else {
                System.out.println("3. User ID 1 does NOT exist!");
            }

            // Check carts table
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM carts WHERE user_id = 1");
            if (rs.next()) {
                System.out.println("4. Carts for user 1: " + rs.getInt("count"));
            }

            // Test CartDAO directly
            CartDAO cartDAO = new CartDAO();
            try {
                int cartId = cartDAO.getOrCreateCart(1);
                System.out.println("5. CartDAO.getOrCreateCart(1): Success, cart ID = " + cartId);
            } catch (Exception e) {
                System.out.println("5. CartDAO.getOrCreateCart(1): FAILED - " + e.getMessage());
            }

            // Check if we can add to cart
            try {
                boolean success = cartDAO.addToCart(1, 2, 1); // cartId=1, productId=2, quantity=1
                System.out.println("6. Add to cart test: " + (success ? "SUCCESS" : "FAILED"));
            } catch (Exception e) {
                System.out.println("6. Add to cart test: FAILED - " + e.getMessage());
                e.printStackTrace();
            }

            DatabaseUtil.closeStatement(stmt);
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeConnection(conn);

        } catch (Exception e) {
            System.err.println("Debug error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}