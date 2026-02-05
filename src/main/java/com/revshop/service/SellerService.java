package com.revshop.service;

import com.revshop.dao.SellerDAO;
import com.revshop.model.Seller;
import java.sql.SQLException;

public class SellerService {
    private SellerDAO sellerDAO;

    public SellerService() {
        sellerDAO = new SellerDAO();
    }

    // Get seller details
    public Seller getSellerDetails(int sellerId) {
        try {
            return sellerDAO.getSellerById(sellerId);
        } catch (SQLException e) {
            System.err.println("Error getting seller details: " + e.getMessage());
            return null;
        }
    }

    // Update seller details
    public boolean updateSellerDetails(Seller seller) {
        try {
            return sellerDAO.updateSeller(seller);
        } catch (SQLException e) {
            System.err.println("Error updating seller details: " + e.getMessage());
            return false;
        }
    }
}