package com.revshop.test;

import com.revshop.dao.CartDAO;
import com.revshop.model.CartItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartDAOTest {

    @Test
    void testCartDAOInstantiation() {
        CartDAO cartDAO = new CartDAO();
        assertNotNull(cartDAO);
    }

    @Test
    void testCartItemModel() {
        CartItem item = new CartItem();
        item.setCartItemId(1);
        item.setCartId(100);
        item.setProductId(50);
        item.setQuantity(3);
        item.setProductName("Test Product");
        item.setProductPrice(29.99);

        assertEquals(1, item.getCartItemId());
        assertEquals(100, item.getCartId());
        assertEquals(50, item.getProductId());
        assertEquals(3, item.getQuantity());
        assertEquals("Test Product", item.getProductName());
        assertEquals(29.99, item.getProductPrice());
    }
}