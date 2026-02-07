# RevShop Database Documentation

## Database Schema
The RevShop database consists of 10 interconnected tables supporting a complete e-commerce platform.

## Table Structure

### 1. `users` - Central User Management
- **Primary Key**: `user_id`
- **Purpose**: Stores all user credentials and roles
- **Roles**: buyer, seller, admin
- **Unique Constraints**: username, email

### 2. `buyers` - Buyer Information
- **Foreign Key**: `buyer_id` references `users.user_id`
- **Purpose**: Extends user table for buyer-specific data
- **Fields**: Contact information, shipping address

### 3. `sellers` - Seller Information
- **Foreign Key**: `seller_id` references `users.user_id`
- **Purpose**: Extends user table for seller-specific data
- **Fields**: Business details, tax information

### 4. `products` - Product Catalog
- **Primary Key**: `product_id`
- **Foreign Key**: `seller_id` references `sellers.seller_id`
- **Purpose**: Manages product inventory and pricing
- **Features**: MRP pricing, stock tracking, categories

### 5. `carts` - Shopping Carts
- **Primary Key**: `cart_id`
- **Foreign Key**: `user_id` references `users.user_id`
- **Status**: active, checked_out, abandoned

### 6. `cart_items` - Cart Contents
- **Primary Key**: `cart_item_id`
- **Foreign Keys**: `cart_id`, `product_id`
- **Constraint**: Unique product per cart

### 7. `orders` - Order Management
- **Primary Key**: `order_id`
- **Foreign Key**: `user_id` references `users.user_id`
- **Status**: pending, confirmed, shipped, delivered, cancelled

### 8. `order_items` - Order Details
- **Primary Key**: `order_item_id`
- **Foreign Keys**: `order_id`, `product_id`
- **Features**: Quantity, price at time of purchase

### 9. `reviews` - Product Reviews
- **Primary Key**: `review_id`
- **Foreign Keys**: `product_id`, `user_id`, `order_id`
- **Constraint**: One review per product per user
- **Rating**: 1-5 scale

### 10. `favorites` - Favorite Products
- **Primary Key**: `favorite_id`
- **Foreign Keys**: `user_id`, `product_id`
- **Constraint**: Unique user-product combination

### 11. `notifications` - User Notifications
- **Primary Key**: `notification_id`
- **Foreign Key**: `user_id` references `users.user_id`
- **Features**: Read status, timestamp

## Relationships Diagram


users
├── buyers (1:1)
├── sellers (1:1)
├── carts (1:M)
├── orders (1:M)
├── reviews (1:M)
├── favorites (1:M)
└── notifications (1:M)

sellers ── products (1:M)

products
├── cart_items (M:M via carts)
├── order_items (M:M via orders)
├── reviews (M:M via users)
└── favorites (M:M via users)



## SQL Schema Location
The complete SQL schema can be found at: `src/sql/schema.sql`

-- Your schema shows excellent database design principles:

-- STRENGTHS IDENTIFIED:
-- 1. Proper normalization (3NF compliant)
-- 2. Correct use of foreign keys with CASCADE
-- 3. Appropriate indexing for performance
-- 4. ENUM types for fixed value sets
-- 5. Timestamp tracking on all transactional tables
-- 6. Unique constraints where needed
-- 7. Specialization pattern (users → buyers/sellers)

-- SUGGESTED ENHANCEMENTS (Optional):
-- 1. Add audit columns (updated_at, updated_by)
-- 2. Consider soft deletes (is_deleted flag)
-- 3. Add product images table
-- 4. Consider full-text search on product names/descriptions
-- 5. Add transaction/audit log table