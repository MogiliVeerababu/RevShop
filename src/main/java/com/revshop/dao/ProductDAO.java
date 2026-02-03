package com.revshop.dao;

import com.revshop.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends BaseDAO {

    // Add new product
    public Product addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, mrp, discounted_price, " +
                "stock_quantity, category, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getMrp());
            stmt.setDouble(5, product.getDiscountedPrice());
            stmt.setInt(6, product.getStockQuantity());
            stmt.setString(7, product.getCategory());
            stmt.setInt(8, product.getSellerId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                    return product;
                }
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get product by ID
    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
            return null;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock_quantity > 0 ORDER BY created_at DESC";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ? AND stock_quantity > 0 ORDER BY name";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);

            rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Get products by seller
    public List<Product> getProductsBySeller(int sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE seller_id = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Search products
    public List<Product> searchProducts(String keyword) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE (name LIKE ? OR description LIKE ? OR category LIKE ?) " +
                "AND stock_quantity > 0";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            String searchTerm = "%" + keyword + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            stmt.setString(3, searchTerm);

            rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Update product
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, mrp = ?, " +
                "discounted_price = ?, stock_quantity = ?, category = ? WHERE product_id = ?";
        return executeUpdate(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getMrp(),
                product.getDiscountedPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.getProductId()) > 0;
    }

    // Update stock quantity
    public boolean updateStockQuantity(int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
        return executeUpdate(sql, quantity, productId) > 0;
    }

    // Delete product
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        return executeUpdate(sql, productId) > 0;
    }

    // Check low stock products for a seller
    public List<Product> getLowStockProducts(int sellerId, int threshold) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE seller_id = ? AND stock_quantity <= ? ORDER BY stock_quantity";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            stmt.setInt(2, threshold);

            rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // Helper method to extract product from ResultSet
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setMrp(rs.getDouble("mrp"));
        product.setDiscountedPrice(rs.getDouble("discounted_price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setCategory(rs.getString("category"));
        product.setSellerId(rs.getInt("seller_id"));
        return product;
    }
}