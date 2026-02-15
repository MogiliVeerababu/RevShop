package com.revshop.util;

import java.util.Random;

public class PaymentSimulator {

    public static boolean processPayment(double amount, String paymentMethod) {
        // Payment already processed in BuyerMenu, just return true
        // No console output to avoid duplicate messages
        return true;
    }

    public static String generateTransactionId() {
        Random random = new Random();
        return "TXN" + System.currentTimeMillis() + random.nextInt(1000);
    }
}