# RevShop - Entity Relationship Diagram
## 📊 ERD (Entity Relationship Diagram)
___
## 🏗️ Database Schema Visualization
___
## 🔗 Entity Relationship Diagram
___
```text
┌─────────────────────┐          ┌─────────────────────┐          ┌─────────────────────┐
│        USERS        │          │       BUYERS        │          │      SELLERS        │
├─────────────────────┤          ├─────────────────────┤          ├─────────────────────┤
│ • user_id (PK)      │◄───1:1───┤ • buyer_id (PK,FK)  │          │ • seller_id (PK,FK) │
│ • username (UQ)     │          │ • first_name        │          │ • business_name     │
│ • email (UQ)        │          │ • last_name         │          │ • business_address  │
│ • password_hash     │          │ • phone             │          │ • business_phone    │
│ • role              │          │ • address           │          │ • tax_id            │
│ • created_at        │          └─────────────────────┘          └──────────┬──────────┘
└──────────┬──────────┘                                                     │
           │                                                                │
           │ 1:N                                                            │ 1:N
           │                                                                │
    ┌──────▼──────┐                                                  ┌──────▼──────┐
    │             │                                                  │             │
┌───┼─────────────┼───┐                                        ┌─────┼─────────────┼─────┐
│   ▼             ▼   │                                        │     ▼             ▼     │
│┌─────────┐ ┌─────────┐                                      │ ┌─────────┐ ┌─────────┐ │
││ CARTS   │ │ ORDERS  │                                      │ │PRODUCTS │ │PRODUCTS │ │
│├─────────┤ ├─────────┤                                      │ ├─────────┤ ├─────────┤ │
││cart_id  │ │order_id │                                      │ │prod_id  │ │prod_id  │ │
││user_id  │ │user_id  │                                      │ │name     │ │name     │ │
││status   │ │total_amt│                                      │ │price    │ │price    │ │
│└────┬────┘ │status   │                                      │ │stock    │ │stock    │ │
│     │      └────┬────┘                                      │ │seller_id│ │seller_id│ │
│     │ 1:N       │ 1:N                                       │ └────┬────┘ └────┬────┘ │
│     ▼           ▼                                           │      │            │      │
│┌─────────┐ ┌─────────┐                                     │      │ 1:N        │ 1:N  │
││CART_ITEMS│ │ORDER_ITEMS                                    │      ▼            ▼      │
│├─────────┤ ├─────────┤                                     │┌─────────┐ ┌─────────┐ │
││cart_item│ │order_item│                                     ││CART_ITEMS│ │ORDER_ITEMS│
││cart_id  │ │order_id  │◄───────────────────────────────────┼┤cart_id  │ │order_id  │ │
││prod_id  │ │prod_id   │◄───────────────────────────────────┼┤prod_id  │ │prod_id   │ │
││quantity │ │quantity  │                                     ││quantity │ │quantity  │ │
│└─────────┘ │price     │                                     │└─────────┘ │price     │ │
│            └────┬─────┘                                     │            └────┬─────┘ │
│                 │                                           │                 │       │
│            ┌────▼─────┐                                     │            ┌────▼─────┐ │
│            │          │                                     │            │          │ │
│            ▼          ▼                                     │            ▼          ▼ │
│    ┌─────────────┐ ┌─────────────┐                         │    ┌─────────────┐ ┌─────────────┐
│    │   REVIEWS   │ │  FAVORITES  │                         │    │   REVIEWS   │ │  FAVORITES  │
│    ├─────────────┤ ├─────────────┤                         │    ├─────────────┤ ├─────────────┤
│    │ review_id   │ │ favorite_id │                         │    │ review_id   │ │ favorite_id │
│    │ prod_id     │ │ user_id     │◄────────────────────────┤    │ prod_id     │ │ user_id     │
│    │ user_id     │ │ prod_id     │◄────────────────────────┤    │ user_id     │ │ prod_id     │
│    │ order_id    │◄─┼────────────┤                         │    │ order_id    │ │ created_at  │
│    │ rating      │  │ created_at │                         │    │ rating      │ └─────────────┘
│    │ comment     │  └────────────┘                         │    │ comment     │
│    └─────────────┘                                         │    └─────────────┘
│                                                            │
│    ┌─────────────┐                                         │    ┌─────────────┐
│    │NOTIFICATIONS│                                         │    │NOTIFICATIONS│
│    ├─────────────┤                                         │    ├─────────────┤
│    │ notif_id    │                                         │    │ notif_id    │
│    │ user_id     │◄────────────────────────────────────────┼────┤ user_id     │
│    │ type        │                                         │    │ type        │
│    │ message     │                                         │    │ message     │
│    │ is_read     │                                         │    │ is_read     │
│    │ created_at  │                                         │    │ created_at  │
│    └─────────────┘                                         │    └─────────────┘
│                                                            │
└────────────────────────────────────────────────────────────┴─────────────────────────────────

```
___
# 📋 COMPLETE RELATIONSHIP MATRIX
```text
┌────────────────┬──────────────┬─────────────────┬─────────────────────────────────────┐
│   PARENT       │    CHILD     │   RELATIONSHIP  │         DESCRIPTION                 │
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│    USERS       │    BUYERS    │      1:1        │ User specialization as buyer        │
│    USERS       │    SELLERS   │      1:1        │ User specialization as seller       │
│    USERS       │    CARTS     │      1:N        │ User can have multiple carts        │
│    USERS       │    ORDERS    │      1:N        │ User can place multiple orders      │
│    USERS       │    REVIEWS   │      1:N        │ User can write multiple reviews     │
│    USERS       │   FAVORITES  │      1:N        │ User can favorite multiple products │
│    USERS       │ NOTIFICATIONS│      1:N        │ User receives multiple notifications│
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│    SELLERS     │   PRODUCTS   │      1:N        │ Seller lists multiple products      │
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│   PRODUCTS     │  CART_ITEMS  │      1:N        │ Product appears in cart items       │
│   PRODUCTS     │ ORDER_ITEMS  │      1:N        │ Product appears in order items      │
│   PRODUCTS     │   REVIEWS    │      1:N        │ Product receives multiple reviews   │
│   PRODUCTS     │  FAVORITES   │      1:N        │ Product favorited by multiple users │
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│    CARTS       │  CART_ITEMS  │      1:N        │ Cart contains multiple items        │
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│    ORDERS      │ ORDER_ITEMS  │      1:N        │ Order contains multiple items       │
│    ORDERS      │   REVIEWS    │      1:N        │ Order generates reviews             │
├────────────────┼──────────────┼─────────────────┼─────────────────────────────────────┤
│ ORDER_ITEMS    │   REVIEWS    │      1:1        │ Each order item can have one review │
└────────────────┴──────────────┴─────────────────┴─────────────────────────────────────┘
```
___

# 🔗 RELATIONSHIP VISUALIZATION

```text
                      ┌─────────────────────┐
                      │        USERS        │
                      │   (Central Entity)  │
                      └──────────┬──────────┘
                                 │
                ┌────────────────┼─────────────────┐
                │                │                 │
        ┌───────▼──────┐ ┌───────▼──────┐ ┌───────▼──────┐
        │    BUYERS    │ │    SELLERS   │ │    CARTS     │
        │   (1:1)      │ │   (1:1)      │ │   (1:N)      │
        └───────┬──────┘ └───────┬──────┘ └───────┬──────┘
                │                │                 │
        ┌───────▼──────┐ ┌───────▼──────┐ ┌───────▼──────┐
        │    ORDERS    │ │   PRODUCTS   │ │  CART_ITEMS  │
        │   (1:N)      │ │   (1:N)      │ │   (1:N)      │
        └───────┬──────┘ └───────┬──────┘ └───────┬──────┘
                │                │                 │
        ┌───────▼──────┐         │          ┌─────┼─────┐
        │ ORDER_ITEMS  │         │          │     │     │
        │   (1:N)      │         │    ┌─────▼─────▼─────▼─────┐
        └───────┬──────┘         │    │       REVIEWS         │
                │                │    │    (Junction Entity)   │
                └────────────────┼────┤                       │
                                 │    │ • product_id (FK)     │
                                 │    │ • user_id (FK)        │
                                 │    │ • order_id (FK)       │
                                 │    │ • rating              │
                                 │    └──────────┬────────────┘
                                 │               │
                           ┌─────▼───────────────▼─────┐
                           │        FAVORITES          │
                           │     (Junction Entity)     │
                           │                           │
                           │ • user_id (FK)            │
                           │ • product_id (FK)         │
                           └───────────────────────────┘
                           
```
___

# 🔑 Relationships Summary

```text
┌────────────────────────────────┬─────────┬─────────────────────────────────────────────┐
│         RELATIONSHIP           │  TYPE   │               DESCRIPTION                   │
├────────────────────────────────┼─────────┼─────────────────────────────────────────────┤
│ Users ↔ Buyers                 │  1:1    │ User specialization as buyer                 │
│ Users ↔ Sellers                │  1:1    │ User specialization as seller                │
│ Users → Carts                  │  1:N    │ A user can have multiple carts               │
│ Users → Orders                 │  1:N    │ A user can place multiple orders             │
│ Users → Reviews                │  1:N    │ A user can write multiple reviews            │
│ Users → Favorites              │  1:N    │ A user can favorite multiple products        │
│ Users → Notifications          │  1:N    │ A user can receive multiple notifications    │
│ Sellers → Products             │  1:N    │ A seller can list multiple products          │
│ Products → Cart Items          │  1:N    │ A product can be in multiple cart items      │
│ Products → Order Items         │  1:N    │ A product can appear in multiple orders      │
│ Products → Reviews             │  1:N    │ A product can have multiple reviews          │
│ Products → Favorites           │  1:N    │ A product can be favorited by multiple users │
│ Carts → Cart Items             │  1:N    │ A cart can contain multiple items            │
│ Orders → Order Items           │  1:N    │ An order can contain multiple items          │
│ Orders → Reviews               │  1:N    │ An order can generate multiple reviews       │
└────────────────────────────────┴─────────┴─────────────────────────────────────────────┘
```