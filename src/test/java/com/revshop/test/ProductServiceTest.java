package com.revshop.test;

import com.revshop.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Test
    void testProductModel() {
        Product product = new Product();
        product.setProductId(1);
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(999.99);
        product.setStockQuantity(25);
        product.setCategory("Electronics");
        product.setSellerId(100);

        assertEquals(1, product.getProductId());
        assertEquals("Laptop", product.getName());
        assertEquals("High-performance laptop", product.getDescription());
        assertEquals(999.99, product.getPrice(), 0.001);
        assertEquals(25, product.getStockQuantity());
        assertEquals("Electronics", product.getCategory());
        assertEquals(100, product.getSellerId());
    }
}