# 🏛️ APPLICATION ARCHITECTURE DIAGRAM
___
## 🏗️ SYSTEM ARCHITECTURE OVERVIEW
### Application Architecture Diagram
```text
┌──────────────────────────────────────────────────────────────────────┐
│                    CONSOLE INTERFACE LAYER                           │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌──────────────────────────────┐    ┌──────────────────────────┐  │
│   │    MainApplication.java      │    │   RevShopApplication     │  │
│   │   (Entry Point & Router)     │    │   (Main Controller)      │  │
│   └─────────────┬────────────────┘    └─────────────┬────────────┘  │
│                 │                                    │               │
│   ┌─────────────▼────────────────┐   ┌──────────────▼─────────────┐ │
│   │        MainMenu.java         │   │     Menu Controllers       │ │
│   │   (Primary Navigation Hub)   │   │   --------------------     │ │
│   └─────────────┬────────────────┘   │  • BuyerMenu.java          │ │
│                 │                     │  • SellerMenu.java         │ │
│           ┌─────▼──────┐             │  • Auth Menu Flows         │ │
│           │ MenuRouter │             │  • Forgot Password Flow    │ │
│           └─────┬──────┘             └────────────────────────────┘ │
│                 │                                                    │
└─────────────────┼────────────────────────────────────────────────────┘
                  │
┌──────────────────────────────────────────────────────────────────────┐
│                 SERVICE LAYER (Business Logic)                       │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │     AuthService       │   │     BuyerService       │            │
│   │   • Registration     │   │   • Browse Products    │            │
│   │   • Login/Logout     │   │   • Cart Management    │            │
│   │   • Password Reset   │   │   • Place Orders       │            │
│   │   • Security Q/A     │   │   • View Order History │            │
│   └───────────────────────┘   └────────────────────────┘            │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │     CartService       │   │     OrderService       │            │
│   │   • Add/Remove Items  │   │   • Process Orders     │            │
│   │   • Calculate Totals  │   │   • Update Status      │            │
│   │   • Cart Validation   │   │   • Payment Simulation │            │
│   └───────────────────────┘   └────────────────────────┘            │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │   ProductService      │   │    SellerService       │            │
│   │   • CRUD Operations   │   │   • Manage Products    │            │
│   │   • Inventory Mgmt    │   │   • View Sales         │            │
│   │   • Category Filter   │   │   • Handle Orders      │            │
│   └───────────────────────┘   └────────────────────────┘            │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │    ReviewService      │   │  NotificationService   │            │
│   │   • Add Reviews      │   │   • Send Alerts        │            │
│   │   • Calculate Ratings │   │   • Track Read Status  │            │
│   │   • Validate Review  │   └────────────────────────┘            │
│   └───────────────────────┘                                         │
│                                                                      │
└───────────────────────────┬──────────────────────────────────────────┘
                            │
┌──────────────────────────────────────────────────────────────────────┐
│                 DATA ACCESS LAYER (DAO Pattern)                      │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │      BaseDAO.java     │   │      UserDAO.java      │            │
│   │  (Abstract Template)  │   │   • User CRUD          │            │
│   │ • Connection Mgmt     │   │   • Authentication     │            │
│   │ • Resource Cleanup    │   │   • Role Management    │            │
│   │ • Template Methods    │   └────────────────────────┘            │
│   └───────────────────────┘                                         │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │    ProductDAO.java    │   │      OrderDAO.java     │            │
│   │   • Product CRUD      │   │   • Order CRUD         │            │
│   │   • Search/Filters    │   │   • Order Items        │            │
│   │   • Stock Updates     │   │   • Status Updates     │            │
│   └───────────────────────┘   └────────────────────────┘            │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │      CartDAO.java     │   │     ReviewDAO.java     │            │
│   │   • Cart Operations   │   │   • Review CRUD        │            │
│   │   • Cart Items        │   │   • Rating Aggregation │            │
│   │   • Cart State        │   └────────────────────────┘            │
│   └───────────────────────┘                                         │
│                                                                      │
│   ┌───────────────────────┐   ┌────────────────────────┐            │
│   │    SellerDAO.java     │   │  NotificationDAO.java  │            │
│   │   • Seller Profile    │   │   • Notification CRUD  │            │
│   │   • Product Listings  │   │   • Read Status        │            │
│   └───────────────────────┘   └────────────────────────┘            │
│                                                                      │
└───────────────────────────┬──────────────────────────────────────────┘
                            │
┌──────────────────────────────────────────────────────────────────────┐
│                 MODEL LAYER (Entity Objects)                         │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────┐   ┌─────────────┐   ┌────────────────────┐        │
│   │    User     │   │   Buyer     │   │      Seller        │        │
│   │ • userId    │   │ • firstName │   │ • businessName    │        │
│   │ • username  │   │ • lastName  │   │ • businessPhone   │        │
│   │ • email     │   │ • phone     │   │ • taxId           │        │
│   │ • role      │   │ • address   │   └────────────────────┘        │
│   └─────────────┘   └─────────────┘                                  │
│                                                                      │
│   ┌─────────────┐   ┌─────────────┐   ┌────────────────────┐        │
│   │   Product   │   │   Order     │   │    OrderItem       │        │
│   │ • productId │   │ • orderId   │   │ • orderItemId     │        │
│   │ • name      │   │ • userId    │   │ • orderId         │        │
│   │ • price     │   │ • status    │   │ • productId       │        │
│   │ • category  │   │ • totalAmt  │   │ • quantity        │        │
│   └─────────────┘   └─────────────┘   └────────────────────┘        │
│                                                                      │
│   ┌─────────────┐   ┌─────────────┐   ┌────────────────────┐        │
│   │  CartItem   │   │   Review    │   │   Notification     │        │
│   │ • cartItemId│   │ • reviewId  │   │ • notificationId  │        │
│   │ • cartId    │   │ • rating    │   │ • type            │        │
│   │ • productId │   │ • comment   │   │ • message         │        │
│   │ • quantity  │   │ • createdAt │   │ • isRead          │        │
│   └─────────────┘   └─────────────┘   └────────────────────┘        │
│                                                                      │
└───────────────────────────┬──────────────────────────────────────────┘
                            │
┌──────────────────────────────────────────────────────────────────────┐
│                 DATABASE LAYER (MySQL)                               │
├──────────────────────────────────────────────────────────────────────┤
│                                                                      │
│               ┌────────────────────────────────┐                     │
│               │         MySQL 8.0+             │                     │
│               │       revshop Database         │                     │
│               │   -------------------------    │                     │
│               │  • users (PK: user_id)        │                     │
│               │  • buyers (PK: buyer_id)      │                     │
│               │  • sellers (PK: seller_id)    │                     │
│               │  • products (PK: product_id)  │                     │
│               │  • orders (PK: order_id)      │                     │
│               │  • order_items                │                     │
│               │  • carts                      │                     │
│               │  • cart_items                 │                     │
│               │  • reviews                    │                     │
│               │  • favorites                  │                     │
│               │  • notifications              │                     │
│               └────────────────────────────────┘                     │
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘
```
___
## 🔄 DATA FLOW PROCESS
```text
┌──────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌──────────┐
│   USER   ├────►│  CONSOLE    ├────►│   SERVICE   ├────►│     DAO     ├────►│ DATABASE │
│  INPUT   │     │   LAYER     │     │   LAYER     │     │   LAYER     │     │  (MySQL) │
│          │     │ (Menus)     │     │ (Business)  │     │   (JDBC)    │     │          │
└──────────┘     └─────────────┘     └─────────────┘     └─────────────┘     └──────────┘
```
___
## 🏗️ DEPENDENCY FLOW
```text
┌─────────────────┐
│  MainApplication│
└────────┬────────┘
         │
┌────────▼────────┐
│    MainMenu     │
└────────┬────────┘
         │
┌────────▼────────┐    ┌──────────────────┐
│   BuyerMenu     │    │   SellerMenu     │
└────────┬────────┘    └────────┬─────────┘
         │                      │
    ┌────▼──────────────────────▼────┐
    │        Service Layer           │
    └──────────────┬─────────────────┘
                   │
    ┌──────────────▼─────────────────┐
    │          DAO Layer             │
    └──────────────┬─────────────────┘
                   │
    ┌──────────────▼─────────────────┐
    │       DatabaseUtil.java        │
    └──────────────┬─────────────────┘
                   │
    ┌──────────────▼─────────────────┐
    │       JDBC Driver              │
    └──────────────┬─────────────────┘
                   │
    ┌──────────────▼─────────────────┐
    │       MySQL Database           │
    └────────────────────────────────┘
```
