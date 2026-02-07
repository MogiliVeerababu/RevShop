# 📦 MODULAR DESIGN EXPLANATION
### RevShop is organized into well-defined functional modules, each responsible for a single concern.
### This modular approach improves maintainability, testability, and scalability.
___
## 🧩 Module 1: User & Authentication Module
```text
User/
├── AuthService.java        (Register / Login / Forgot Password)
├── UserDAO.java            (User persistence operations)
├── User.java               (Base user entity)
├── Buyer.java              (Buyer entity extends User)
├── Seller.java             (Seller entity extends User)
└── PasswordUtil.java       (Password hashing & verification)
```
### Responsibilities
✅ Buyer & Seller registration

✅ Secure login using hashed passwords

✅ Forgot password using security questions

✅ Role identification (BUYER / SELLER / ADMIN)

✅ User profile management

___
## 🧩 Module 2: Product Management Module
```text
Product/
├── ProductService.java     (Business logic for products)
├── ProductDAO.java         (Product persistence operations)
└── Product.java            (Product entity)
```
### Responsibilities
✅ Add / update / delete products (Seller)

✅ Browse products (Buyer)

✅ Category-based filtering

✅ Stock & pricing management

✅ Discount calculations

✅ Product search functionality
___
## 🧩 Module 3: Shopping Cart Module
```text
Cart/
├── CartService.java        (Add / remove / calculate cart)
├── CartDAO.java            (Cart persistence operations)
└── CartItem.java           (Cart item model)
```
### Responsibilities
✅ Add/remove items to/from cart

✅ Calculate cart totals

✅ Validate cart items

✅ Cart status management

✅ Merge cart sessions
___
## 🧩 Module 4: Order Processing Module
```text
Order/
├── OrderService.java       (Order placement & retrieval)
├── OrderDAO.java           (Order persistence)
├── OrderItemDAO.java       (Order-item persistence)
├── Order.java              (Order entity)
└── OrderItem.java          (Order item entity)
```
### Responsibilities
✅ Checkout flow management

✅ Order creation and validation

✅ Order-item mapping

✅ Seller-wise order views

✅ Transaction consistency

✅ Order status tracking
___
## 🧩 Module 5: Review & Rating Module
```text
Review/
├── ReviewService.java      (Review business logic)
├── ReviewDAO.java          (Review persistence)
└── Review.java             (Review entity)
```
### Responsibilities
✅ Product reviews & ratings

✅ Buyer-specific reviews

✅ Product feedback visibility

✅ Rating validation (1-5 stars)

✅ Purchase verification for reviews

✅ Review aggregation and averages
___
## 🧩 Module 6: Favorites (Wishlist) Module
```text
Favorites/
├── FavoriteService.java    (Wishlist operations)
├── FavoriteDAO.java        (Favorites persistence)
└── Favorite.java           (Favorite mapping entity)
```
### Responsibilities
✅ Add/remove favorite products

✅ View favorites list

✅ Add favorite items directly to cart

✅ Track favorite products per user

✅ Favorite count tracking

___
## 🧩 Module 7: Notification Module
```text
Notification/
├── NotificationService.java (Notification operations)
├── NotificationDAO.java     (Notification persistence)
└── Notification.java        (Notification entity)
```
### Responsibilities
✅ Order status notifications

✅ Price drop alerts

✅ Stock availability alerts

✅ Review reminders

✅ System announcements

✅ Notification read tracking
___
## 🧩 Module 8: Payment Processing Module
```text
Payment/
├── PaymentSimulator.java   (Payment simulation)
└── PaymentService.java     (Payment processing logic)
```
### Responsibilities
✅ Payment method simulation (UPI/CARD/COD)

✅ Payment status tracking

✅ Payment validation

✅ Order confirmation

✅ Payment receipt generation
___
## 📐 CLASS DIAGRAM (SIMPLIFIED)
```text
┌──────────────────────┐
│ RevShopApplication   │
├──────────────────────┤
│ +main()              │
│ +run()               │
│ +initialize()        │
└───────────┬──────────┘
            │
┌───────────▼──────────┐
│     MainMenu.java    │
├──────────────────────┤
│ +displayMenu()       │
│ +handleChoice()      │
│ +navigateToRoleMenu()│
└───────────┬──────────┘
            │
    ┌───────▼───────┐    ┌───────────────┐
    │  BuyerMenu    │    │  SellerMenu   │
    ├───────────────┤    ├───────────────┤
    │ +showMenu()   │    │ +showMenu()   │
    │ +handleCart() │    │ +addProduct() │
    │ +placeOrder() │    │ +viewOrders() │
    └───────┬───────┘    └───────┬───────┘
            │                    │
    ┌───────▼───────┐    ┌───────▼───────┐
    │ BuyerService  │    │ SellerService │
    ├───────────────┤    ├───────────────┤
    │ -buyerDAO     │    │ -sellerDAO    │
    │ -cartService  │    │ -productDAO   │
    │ -orderService │    │ -orderDAO     │
    └───────┬───────┘    └───────┬───────┘
            │                    │
    ┌───────▼────────────────────▼───────┐
    │            Service Layer           │
    ├────────────────────────────────────┤
    │  • AuthService                     │
    │  • ProductService                  │
    │  • CartService                     │
    │  • OrderService                    │
    │  • ReviewService                   │
    │  • NotificationService             │
    └─────────────────┬──────────────────┘
                      │
    ┌─────────────────▼──────────────────┐
    │            DAO Layer               │
    ├────────────────────────────────────┤
    │  • UserDAO                         │
    │  • ProductDAO                      │
    │  • OrderDAO                        │
    │  • CartDAO                         │
    │  • ReviewDAO                       │
    │  • NotificationDAO                 │
    └─────────────────┬──────────────────┘
                      │
    ┌─────────────────▼──────────────────┐
    │      DatabaseUtil.java             │
    ├────────────────────────────────────┤
    │ +getConnection()                   │
    │ +closeResources()                  │
    │ +executeUpdate()                   │
    └─────────────────┬──────────────────┘
                      │
    ┌─────────────────▼──────────────────┐
    │          MySQL Database            │
    └────────────────────────────────────┘
```
___
## 🧩 COMPONENT DIAGRAM
```text
┌─────────────────────────────────────────────┐
│          RevShop Console Application        │
├─────────────────────────────────────────────┤
│  Input Handler → Menu System → Services     │
│                                             │
│  ┌──────────────┐    ┌────────────────┐    │
│  │  User & Auth │    │  Product Mgmt  │    │
│  │   Module     │    │    Module      │    │
│  │              │    │                │    │
│  │ • Registration│   │ • Add/View     │    │
│  │ • Login       │   │ • Update/Del   │    │
│  │ • Forgot Pwd  │   │ • Categories   │    │
│  └──────────────┘    └────────────────┘    │
│                                             │
│  ┌──────────────┐    ┌────────────────┐    │
│  │  Order Mgmt  │    │ Review System  │    │
│  │   Module     │    │   Module       │    │
│  │              │    │                │    │
│  │ • Checkout   │   │ • Ratings       │    │
│  │ • Tracking   │   │ • Reviews       │    │
│  │ • History    │   │ • Validation    │    │
│  └──────────────┘    └────────────────┘    │
│                                             │
│  ┌──────────────┐    ┌────────────────┐    │
│  │  Cart System │    │  Favorites     │    │
│  │   Module     │    │   Module       │    │
│  │              │    │                │    │
│  │ • Add/Remove │   │ • Wishlist      │    │
│  │ • Calculate  │   │ • Quick Add     │    │
│  │ • Validate   │   │ • Tracking      │    │
│  └──────────────┘    └────────────────┘    │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Notification & Payment Modules      │  │
│  │                                      │  │
│  │ • Order Alerts                       │  │
│  │ • Payment Simulation                 │  │
│  │ • System Messages                    │  │
│  └──────────────────────────────────────┘  │
└─────────────────┬──────────────────────────┘
                  │
┌─────────────────▼──────────────────────────┐
│             DAO Abstraction Layer          │
├────────────────────────────────────────────┤
│  • BaseDAO (Template Pattern)              │
│  • JDBC Implementation                     │
│  • Connection Pooling                      │
│  • Transaction Management                  │
└─────────────────┬──────────────────────────┘
                  │
┌─────────────────▼──────────────────────────┐
│            MySQL Database                  │
│        (revshop Schema)                    │
├────────────────────────────────────────────┤
│  • 10 Normalized Tables                    │
│  • Foreign Key Constraints                 │
│  • Indexed Columns                         │
│  • ACID Compliance                         │
└────────────────────────────────────────────┘
```
___
## ⚡ SEQUENCE DIAGRAM – LOGIN FLOW
```text
┌───────┐   ┌─────────┐   ┌────────────┐   ┌─────────┐   ┌──────────┐
│ User  │   │ Main    │   │ Auth       │   │ User    │   │ Database │
│       │   │ Menu    │   │ Service    │   │ DAO     │   │ (MySQL)  │
└───┬───┘   └────┬────┘   └─────┬──────┘   └────┬────┘   └────┬─────┘
    │            │              │                │             │
    │──Select Login────────────►│                │             │
    │            │              │                │             │
    │            │◄──Ask Credentials───│         │             │
    │            │              │                │             │
    │──Enter Credentials───────►│                │             │
    │            │              │                │             │
    │            │──validateCredentials()───────►│             │
    │            │              │                │             │
    │            │              │                │──SELECT * FROM─►
    │            │              │                │   users      │
    │            │              │                │              │
    │            │              │                │◄──User Record──
    │            │              │                │              │
    │            │              │◄──User Data────│              │
    │            │              │                │              │
    │            │              │──verifyHash()──│              │
    │            │              │                │              │
    │            │◄──Login Result───────────────│              │
    │            │              │                │              │
    │◄──Welcome Message─────────│                │              │
    │            │              │                │              │
    │──Navigate to Role Menu───►│                │              │
    │            │              │                │              │
```
## ⚡ SEQUENCE DIAGRAM – PLACE ORDER FLOW
```text
┌───────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌──────────┐
│ Buyer │   │ Buyer   │   │ Cart    │   │ Order   │   │ Payment │   │ Database │
│       │   │ Menu    │   │ Service │   │ Service │   │ Service │   │ (MySQL)  │
└───┬───┘   └────┬────┘   └────┬────┘   └────┬────┘   └────┬────┘   └────┬─────┘
    │            │              │              │             │             │
    │──View Cart───────────────►│              │             │             │
    │            │              │              │             │             │
    │            │──getCart()───►│              │             │             │
    │            │              │              │             │             │
    │            │              │──loadCartItems()──────────►│             │
    │            │              │              │             │             │
    │            │              │              │◄──Cart Items───────────────
    │            │              │              │             │             │
    │            │◄──Cart Display──────────────│             │             │
    │            │              │              │             │             │
    │──Proceed to Checkout─────►│              │             │             │
    │            │              │              │             │             │
    │            │──placeOrder()───────────────►│             │             │
    │            │              │              │             │             │
    │            │              │              │──validateOrder()──────────►
    │            │              │              │             │             │
    │            │              │              │◄──Validation Result───────
    │            │              │              │             │             │
    │            │              │              │──processPayment()────────►
    │            │              │              │             │             │
    │            │              │              │             │◄──Payment Success
    │            │              │              │             │             │
    │            │              │              │──createOrder()───────────►
    │            │              │              │             │             │
    │            │              │              │             │◄──Order Created
    │            │              │              │             │             │
    │            │              │              │──clearCart()─────────────►
    │            │              │              │             │             │
    │            │◄──Order Confirmation────────│             │             │
    │            │              │              │             │             │
    │◄──Order Details───────────│              │             │             │
    │            │              │              │             │             │
```
___
## 🚀 DEPLOYMENT ARCHITECTURE
```text
┌─────────────────────────────────────────────────────┐
│              Developer Environment                   │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │         IntelliJ IDEA / Eclipse             │   │
│  │  • Java IDE with Maven integration          │   │
│  │  • Code editing and debugging               │   │
│  │  • Version control (Git)                    │   │
│  └─────────────────────────────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │           Maven Build System                 │   │
│  │  • Dependency management (pom.xml)           │   │
│  │  • Compilation and packaging                 │   │
│  │  • Test execution                            │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │        Java Virtual Machine (JVM)            │   │
│  │  • Java 8+ runtime environment               │   │
│  │  • Memory management and optimization        │   │
│  │  • Garbage collection                        │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │         RevShop Console Application          │   │
│  │  • Three-tier architecture                   │   │
│  │  • Modular design                            │   │
│  │  • Console-based interface                   │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │          MySQL Database Server               │   │
│  │  • Localhost:3306                            │   │
│  │  • revshop database schema                   │   │
│  │  • 10+ tables with relationships             │   │
│  │  • ACID transaction support                  │   │
│  └─────────────────────────────────────────────┘   │
│                                                     │
└─────────────────────────────────────────────────────┘
```
## 🔐 SECURITY ARCHITECTURE
```text
┌─────────────────────────────────────────────────────┐
│               Multi-Layer Security                  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │        Layer 1: Input Validation            │   │
│  │  • SQL injection prevention                │   │
│  │  • XSS attack prevention                   │   │
│  │  • Data type validation                    │   │
│  │  • Length and format checks                │   │
│  └─────────────────────────────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │        Layer 2: Authentication               │   │
│  │  • Password hashing (BCrypt)                │   │
│  │  • Security questions                       │   │
│  │  • Session management                       │   │
│  │  • Role-based access control                │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │        Layer 3: Authorization                │   │
│  │  • Buyer/Seller role separation             │   │
│  │  • Menu-level access control                │   │
│  │  • Data ownership validation                │   │
│  │  • Transaction permissions                  │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │        Layer 4: Data Protection             │   │
│  │  • Prepared statements (JDBC)               │   │
│  │  • Parameterized queries                    │   │
│  │  • Connection pooling security              │   │
│  │  • Database constraint enforcement          │   │
│  └──────────────────────┬───────────────────────┘   │
│                         │                           │
│  ┌──────────────────────▼───────────────────────┐   │
│  │        Layer 5: Logging & Auditing          │   │
│  │  • Log4j2 logging framework                 │   │
│  │  • User activity tracking                   │   │
│  │  • Error logging and monitoring             │   │
│  │  • Security incident recording              │   │
│  └─────────────────────────────────────────────┘   │
│                                                     │
└─────────────────────────────────────────────────────┘
```
