package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    // Add product
    public boolean addProduct(Product product) {
        try {
            return productDAO.addProduct(product) != null;
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());
            return false;
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            System.err.println("Error getting products: " + e.getMessage());
            return List.of();
        }
    }

    // Get product by ID
    public Product getProductById(int productId) {
        try {
            return productDAO.getProductById(productId);
        } catch (SQLException e) {
            System.err.println("Error getting product: " + e.getMessage());
            return null;
        }
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        try {
            return productDAO.getProductsByCategory(category);
        } catch (SQLException e) {
            System.err.println("Error getting products by category: " + e.getMessage());
            return List.of();
        }
    }

    // Get products by seller
    public List<Product> getProductsBySeller(int sellerId) {
        try {
            return productDAO.getProductsBySeller(sellerId);
        } catch (SQLException e) {
            System.err.println("Error getting seller products: " + e.getMessage());
            return List.of();
        }
    }

    // Search products
    public List<Product> searchProducts(String keyword) {
        try {
            return productDAO.searchProducts(keyword);
        } catch (SQLException e) {
            System.err.println("Error searching products: " + e.getMessage());
            return List.of();
        }
    }

    // Update product
    public boolean updateProduct(Product product) {
        try {
            return productDAO.updateProduct(product);
        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    // Delete product
    public boolean deleteProduct(int productId) {
        try {
            return productDAO.deleteProduct(productId);
        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    // Update stock
    public boolean updateStock(int productId, int quantity) {
        try {
            return productDAO.updateStockQuantity(productId, quantity);
        } catch (SQLException e) {
            System.err.println("Error updating stock: " + e.getMessage());
            return false;
        }
    }

    // Check low stock
    public List<Product> getLowStockProducts(int sellerId, int threshold) {
        try {
            return productDAO.getLowStockProducts(sellerId, threshold);
        } catch (SQLException e) {
            System.err.println("Error checking low stock: " + e.getMessage());
            return List.of();
        }
    }
}