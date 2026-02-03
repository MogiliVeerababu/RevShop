package com.revshop.dao;

import com.revshop.model.Seller;
import java.sql.*;

public class SellerDAO extends BaseDAO {

    // Get seller by ID
    public Seller getSellerById(int sellerId) throws SQLException {
        String sql = "SELECT u.*, s.business_name, s.business_address, s.business_phone, s.tax_id " +
                "FROM users u JOIN sellers s ON u.user_id = s.seller_id " +
                "WHERE u.user_id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Seller seller = new Seller();
                seller.setUserId(rs.getInt("user_id"));
                seller.setUsername(rs.getString("username"));
                seller.setEmail(rs.getString("email"));
                seller.setPasswordHash(rs.getString("password_hash"));
                seller.setRole(rs.getString("role"));
                seller.setBusinessName(rs.getString("business_name"));
                seller.setBusinessAddress(rs.getString("business_address"));
                seller.setBusinessPhone(rs.getString("business_phone"));
                seller.setTaxId(rs.getString("tax_id"));
                return seller;
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update seller details
    public boolean updateSeller(Seller seller) throws SQLException {
        String userSql = "UPDATE users SET username = ?, email = ? WHERE user_id = ?";
        boolean userUpdated = executeUpdate(
                userSql,
                seller.getUsername(),
                seller.getEmail(),
                seller.getUserId()
        ) > 0;

        String sellerSql = """
            UPDATE sellers
            SET business_name = ?, business_address = ?, business_phone = ?, tax_id = ?
            WHERE seller_id = ?
        """;

        boolean sellerUpdated = executeUpdate(
                sellerSql,
                seller.getBusinessName(),
                seller.getBusinessAddress(),
                seller.getBusinessPhone(),
                seller.getTaxId(),
                seller.getUserId()
        ) > 0;

        return userUpdated && sellerUpdated;
    }
}