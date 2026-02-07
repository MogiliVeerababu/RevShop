# 🛒 RevShop – E-Commerce Management System

RevShop is a **Java-based e-commerce application** designed using **clean architecture, modular design, and relational database principles**.  
It supports **Buyer & Seller workflows**, product management, cart handling, order processing, reviews, favorites, and notifications.

This project demonstrates **real-world backend design** using **Core Java, JDBC, MySQL, and Maven**, following industry best practices.

---

## 📌 Features

### 👤 User Management
- Buyer & Seller registration
- Secure login with hashed passwords
- Role-based access control
- Forgot password support

### 🛍️ Product Management
- Seller product listing
- Update & delete products
- Category-based browsing
- Stock & pricing management

### 🛒 Shopping Cart
- Add / remove products
- Quantity management
- In-memory cart handling

### 📦 Order Processing
- Checkout flow
- Order & order-item persistence
- Order status tracking
- Seller-wise order visibility

### ⭐ Reviews & Ratings
- Product reviews (1–5 rating)
- Only after successful delivery
- One review per product per user

### ❤️ Favorites (Wishlist)
- Add/remove favorite products
- View wishlist
- Add favorites directly to cart

### 🔔 Notifications
- Order confirmation alerts
- User-specific notifications

---

## 🧱 Project Architecture

-Presentation Layer (Console Menus)
- ↓
- Service Layer (Business Logic)
- ↓
- DAO Layer (JDBC Persistence)
- ↓
- MySQL Database


---

## 🧩 Modular Design

### 1️⃣ User & Authentication Module

- User/
- ├── UserService.java
- ├── UserDao.java
- ├── UserDaoImpl.java
- ├── User.java
- └── PasswordUtil.java

### 2️⃣ Product Management Module
- Product/
- ├── ProductService.java
- ├── ProductDao.java
- ├── ProductDaoImpl.java
- └── Product.java

### 3️⃣ Cart Module
- Cart/
- ├── CartService.java
- └── CartItem.java

### 4️⃣ Order Processing Module
- Order/
- ├── OrderService.java
- ├── OrderDao.java
- ├── OrderDaoImpl.java
- ├── OrderItemDao.java
- ├── OrderItemDaoImpl.java
- ├── Order.java
- └── OrderItem.java

### 5️⃣ Review Module
- Review/
- ├── ReviewService.java
- ├── ReviewDao.java
- ├── ReviewDaoImpl.java
- └── Review.java

### 6️⃣ Favorites Module
- Favorites/
- ├── FavoriteService.java
- ├── FavoriteDao.java
- ├── FavoriteDaoImpl.java
- └── Favorite.java

### 7️⃣ Payment & Notification Module
- Payment & Notification/
- ├── PaymentService.java
- └── NotificationService.java

---

## 📊 Database Design
- Fully normalized (3NF)
- Strong referential integrity using foreign keys
- Central **Users** table with Buyer & Seller extensions
- Separate tables for carts, orders, reviews, favorites, and notifications

### 📄 Documentation
- docs/
- ├── DATABASE_DOCUMENTATION.md
- ├── ERD.md
- ├── ERD_PUML.md
- ├── Architecture.md
- └── img.png

---

## 📐 ER Diagram
- Designed using **PlantUML**
- Available in: `docs/ERD_PUML.md`
- Can be viewed directly in **IntelliJ IDEA** using the PlantUML plugin

---

## 🧪 Testing

- Unit tests for DAO & Service layers
- JUnit-based test cases

- src/test/java/com/revshop/test/
- ├── AuthServiceTest.java
- ├── UserDAOTest.java
- ├── ProductServiceTest.java
- ├── CartDAOTest.java
- └── OrderServiceTest.java

---

## 🔐 Security Practices
- Password hashing (BCrypt / secure hashing utility)
- PreparedStatements to prevent SQL Injection
- Role-based authorization
- Input validation at service layer
- Logging using Log4j2

---

## 🛠️ Tech Stack

| Category      | Technology |
|--------------|------------|
| Language     | Java |
| Database     | MySQL |
| Persistence  | JDBC |
| Build Tool   | Maven |
| Testing      | JUnit |
| IDE          | IntelliJ IDEA |
| Diagramming  | PlantUML |
| Logging      | Log4j2 |

---

## 🚀 How to Run

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/revshop.git
cd revshop
```
# 2️⃣ Setup Database

- Create MySQL database

- Execute SQL scripts from documentation

## 3️⃣ Configure Database

- Update DB credentials in configuration file

## 4️⃣ Build & Run
```bash
 mvn clean install
```

- Run RevShopApplication.java from IntelliJ IDEA.
