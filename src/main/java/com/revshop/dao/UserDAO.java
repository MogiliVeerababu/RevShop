package com.revshop.dao;

import com.revshop.model.User;
import com.revshop.model.Buyer;
import com.revshop.model.Seller;
import java.sql.*;

public class UserDAO extends BaseDAO {

    // Register a new user
    public User registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, role, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getSecurityQuestion());
            stmt.setString(6, user.getSecurityAnswer());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));

                    // Insert into specific table based on role
                    if (user.getRole().equals("buyer")) {
                        insertBuyerDetails((Buyer) user);
                    } else if (user.getRole().equals("seller")) {
                        insertSellerDetails((Seller) user);
                    }

                    return user;
                }
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    private void insertBuyerDetails(Buyer buyer) throws SQLException {
        String sql = "INSERT INTO buyers (buyer_id, first_name, last_name, phone, address) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql,
                buyer.getUserId(),
                buyer.getFirstName(),
                buyer.getLastName(),
                buyer.getPhone(),
                buyer.getAddress());
    }

    private void insertSellerDetails(Seller seller) throws SQLException {
        String sql = "INSERT INTO sellers (seller_id, business_name, business_address, business_phone, tax_id) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql,
                seller.getUserId(),
                seller.getBusinessName(),
                seller.getBusinessAddress(),
                seller.getBusinessPhone(),
                seller.getTaxId());
    }

    // Login user
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setSecurityQuestion(rs.getInt("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                return user;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Check if email exists
    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Check if username exists
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get user by ID
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setSecurityQuestion(rs.getInt("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                return user;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get user by email (NEW for forgot password)
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setSecurityQuestion(rs.getInt("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                return user;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get user by username (NEW for forgot email)
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                user.setSecurityQuestion(rs.getInt("security_question"));
                user.setSecurityAnswer(rs.getString("security_answer"));
                return user;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update user password
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        return executeUpdate(sql, newPasswordHash, userId) > 0;
    }

    // Update password by email (NEW for forgot password)
    public boolean updatePasswordByEmail(String email, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE email = ?";
        return executeUpdate(sql, newPasswordHash, email) > 0;
    }

    // Get buyer details by user ID
    public Buyer getBuyerDetails(int userId) throws SQLException {
        String sql = "SELECT u.*, b.first_name, b.last_name, b.phone, b.address " +
                "FROM users u " +
                "LEFT JOIN buyers b ON u.user_id = b.buyer_id " +
                "WHERE u.user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                Buyer buyer = new Buyer();
                buyer.setUserId(rs.getInt("user_id"));
                buyer.setUsername(rs.getString("username"));
                buyer.setEmail(rs.getString("email"));
                buyer.setPasswordHash(rs.getString("password_hash"));
                buyer.setRole(rs.getString("role"));
                buyer.setSecurityQuestion(rs.getInt("security_question"));
                buyer.setSecurityAnswer(rs.getString("security_answer"));

                buyer.setFirstName(rs.getString("first_name"));
                buyer.setLastName(rs.getString("last_name"));
                buyer.setPhone(rs.getString("phone"));
                buyer.setAddress(rs.getString("address"));

                return buyer;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update buyer details
    public boolean updateBuyerDetails(Buyer buyer) throws SQLException {
        String sql = "UPDATE buyers SET first_name = ?, last_name = ?, phone = ?, address = ? " +
                "WHERE buyer_id = ?";
        return executeUpdate(sql,
                buyer.getFirstName(),
                buyer.getLastName(),
                buyer.getPhone(),
                buyer.getAddress(),
                buyer.getUserId()) > 0;
    }

    // Get all users
    public java.util.List<User> getAllUsers() throws SQLException {
        java.util.List<User> users = new java.util.ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
            return users;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Delete user
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return executeUpdate(sql, userId) > 0;
    }

    // Update user email or username
    public boolean updateUserInfo(int userId, String username, String email) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ? WHERE user_id = ?";
        return executeUpdate(sql, username, email, userId) > 0;
    }
}