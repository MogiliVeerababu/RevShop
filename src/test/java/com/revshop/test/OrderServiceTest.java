package com.revshop.test;

import com.revshop.model.Order;
import com.revshop.model.OrderItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void testOrderModel() {
        Order order = new Order();
        order.setOrderId(1);
        order.setUserId(100);
        order.setTotalAmount(149.99);
        order.setStatus("Shipped");
        order.setShippingAddress("123 Main St");

        assertEquals(1, order.getOrderId());
        assertEquals(100, order.getUserId());
        assertEquals(149.99, order.getTotalAmount(), 0.001);
        assertEquals("Shipped", order.getStatus());
        assertEquals("123 Main St", order.getShippingAddress());
    }

    @Test
    void testOrderItemModel() {
        OrderItem item = new OrderItem();
        item.setOrderItemId(1);
        item.setOrderId(100);
        item.setProductId(50);
        item.setQuantity(2);
        item.setPrice(24.99);

        assertEquals(1, item.getOrderItemId());
        assertEquals(100, item.getOrderId());
        assertEquals(50, item.getProductId());
        assertEquals(2, item.getQuantity());
        assertEquals(24.99, item.getPrice(), 0.001);
    }
}