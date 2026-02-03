-- Clean setup script
DROP DATABASE IF EXISTS revshop;
CREATE DATABASE revshop;
USE revshop;

-- Users table
CREATE TABLE users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('buyer', 'seller', 'admin') NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Buyers table
CREATE TABLE buyers (
                        buyer_id INT PRIMARY KEY,
                        first_name VARCHAR(50),
                        last_name VARCHAR(50),
                        phone VARCHAR(20),
                        address TEXT,
                        FOREIGN KEY (buyer_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Sellers table
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
                          FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE CASCADE
);

-- Carts table
CREATE TABLE carts (
                       cart_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_id INT NOT NULL,
                       status ENUM('active', 'checked_out', 'abandoned') DEFAULT 'active',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
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

-- Insert test data
INSERT INTO users (username, email, password_hash, role) VALUES
                                                             ('test_buyer', 'buyer@test.com', 'hashed_password_123', 'buyer'),
                                                             ('test_seller', 'seller@test.com', 'hashed_password_456', 'seller');

INSERT INTO buyers (buyer_id, first_name, last_name, phone, address) VALUES
    (1, 'Test', 'Buyer', '1234567890', '123 Test Street');

INSERT INTO sellers (seller_id, business_name, business_address, business_phone) VALUES
    (2, 'Test Store', '456 Business Ave', '0987654321');

INSERT INTO products (name, description, price, mrp, discounted_price, stock_quantity, category, seller_id) VALUES
                                                                                                                ('Wireless Headphones', 'Noise cancelling wireless headphones', 199.99, 249.99, 199.99, 100, 'Electronics', 2),
                                                                                                                ('Smartphone', 'Latest smartphone model', 699.99, 799.99, 699.99, 50, 'Electronics', 2);

-- Create a cart for the test buyer
INSERT INTO carts (user_id, status) VALUES (1, 'active');

-- Show all data
SELECT 'Users:' as '';
SELECT * FROM users;

SELECT 'Buyers:' as '';
SELECT * FROM buyers;

SELECT 'Sellers:' as '';
SELECT * FROM sellers;

SELECT 'Products:' as '';
SELECT * FROM products;

SELECT 'Carts:' as '';
SELECT * FROM carts;