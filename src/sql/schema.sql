-- Create database
CREATE DATABASE IF NOT EXISTS revshop;
USE revshop;

-- Users table (common for both buyers and sellers)
CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('buyer', 'seller', 'admin') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       INDEX idx_email (email),
                       INDEX idx_role (role)
);

-- Buyers table (extends users)
CREATE TABLE buyers (
                        buyer_id INT PRIMARY KEY,
                        first_name VARCHAR(50),
                        last_name VARCHAR(50),
                        phone VARCHAR(20),
                        address TEXT,
                        FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Sellers table (extends users)
CREATE TABLE sellers (
                         seller_id INT PRIMARY KEY,
                         business_name VARCHAR(100),
                         business_address TEXT,
                         business_phone VARCHAR(20),
                         tax_id VARCHAR(50),
                         FOREIGN KEY (seller_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Products table
CREATE TABLE products (
                          product_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(200) NOT NULL,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL,
                          mrp DECIMAL(10,2),
                          discounted_price DECIMAL(10,2),
                          stock_quantity INT NOT NULL DEFAULT 0,
                          category VARCHAR(100),
                          seller_id INT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE CASCADE,
                          INDEX idx_category (category),
                          INDEX idx_seller (seller_id)
);

-- Carts table
CREATE TABLE carts (
                       cart_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_id INT NOT NULL,
                       status ENUM('active', 'checked_out', 'abandoned') DEFAULT 'active',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                       INDEX idx_user_status (user_id, status)
);

-- Cart Items table
CREATE TABLE cart_items (
                            cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
                            cart_id INT NOT NULL,
                            product_id INT NOT NULL,
                            quantity INT NOT NULL DEFAULT 1,
                            added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                            UNIQUE KEY unique_cart_product (cart_id, product_id)
);

-- Orders table
CREATE TABLE orders (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
                        shipping_address TEXT NOT NULL,
                        payment_method VARCHAR(50),
                        payment_status ENUM('pending', 'completed', 'failed') DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                        INDEX idx_user (user_id),
                        INDEX idx_status (status)
);

-- Order Items table
CREATE TABLE order_items (
                             order_item_id INT PRIMARY KEY AUTO_INCREMENT,
                             order_id INT NOT NULL,
                             product_id INT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                             FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Reviews table
CREATE TABLE reviews (
                         review_id INT PRIMARY KEY AUTO_INCREMENT,
                         product_id INT NOT NULL,
                         user_id INT NOT NULL,
                         rating INT CHECK (rating >= 1 AND rating <= 5),
                         comment TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                         FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                         UNIQUE KEY unique_product_user (product_id, user_id)
);

-- Favorites table
CREATE TABLE favorites (
                           favorite_id INT PRIMARY KEY AUTO_INCREMENT,
                           user_id INT NOT NULL,
                           product_id INT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                           FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
                           UNIQUE KEY unique_user_product (user_id, product_id)
);

-- Notifications table
CREATE TABLE notifications (
                               notification_id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT NOT NULL,
                               type VARCHAR(50),
                               message TEXT NOT NULL,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                               INDEX idx_user_read (user_id, is_read)
);

-- Sample data
INSERT INTO users (username, email, password_hash, role) VALUES
                                                             ('john_doe', 'john@example.com', '$2a$10$YourHashedPasswordHere1', 'buyer'),
                                                             ('jane_smith', 'jane@example.com', '$2a$10$YourHashedPasswordHere2', 'buyer'),
                                                             ('tech_store', 'tech@example.com', '$2a$10$YourHashedPasswordHere3', 'seller'),
                                                             ('fashion_hub', 'fashion@example.com', '$2a$10$YourHashedPasswordHere4', 'seller');

INSERT INTO buyers (buyer_id, first_name, last_name, phone, address) VALUES
                                                                         (1, 'John', 'Doe', '1234567890', '123 Main St, City'),
                                                                         (2, 'Jane', 'Smith', '0987654321', '456 Oak Ave, Town');

INSERT INTO sellers (seller_id, business_name, business_address, business_phone) VALUES
                                                                                     (3, 'Tech Store', '789 Tech Park, Silicon Valley', '555-1234'),
                                                                                     (4, 'Fashion Hub', '321 Fashion Street, NYC', '555-5678');

INSERT INTO products (name, description, price, mrp, stock_quantity, category, seller_id) VALUES
                                                                                              ('Smartphone X', 'Latest smartphone with amazing features', 699.99, 799.99, 50, 'Electronics', 3),
                                                                                              ('Wireless Headphones', 'Noise cancelling wireless headphones', 199.99, 249.99, 100, 'Electronics', 3),
                                                                                           ('Cotton T-Shirt', '100% cotton premium t-shirt', 24.99, 29.99, 200, 'Clothing', 4),
                                                                                              ('Denim Jeans', 'Classic blue denim jeans', 49.99, 59.99, 150, 'Clothing', 4);

-- ============================================
-- REVIEW SYSTEM ENHANCEMENTS
-- ============================================

-- Add reviewed flag to order_items table
ALTER TABLE order_items ADD COLUMN reviewed BOOLEAN DEFAULT FALSE;

-- Add order_id to reviews table to link reviews with orders
ALTER TABLE reviews ADD COLUMN order_id INT;
ALTER TABLE reviews ADD FOREIGN KEY (order_id) REFERENCES orders(order_id);

-- Add average_rating to products for quick access
ALTER TABLE products ADD COLUMN average_rating DECIMAL(3,2) DEFAULT 0.00;

-- Update existing reviews to have order_id (run once)
-- Note: This assumes all existing reviews are for delivered orders
-- If not, you may need to adjust the query
UPDATE reviews r
    JOIN order_items oi ON r.product_id = oi.product_id AND r.user_id = (
        SELECT user_id FROM orders WHERE order_id = oi.order_id
    )
    JOIN orders o ON oi.order_id = o.order_id
SET r.order_id = oi.order_id
WHERE o.status = 'delivered'
  AND r.order_id IS NULL;

