package com.revshop.util;

import java.util.Random;

public class PaymentSimulator {

    public static boolean processPayment(double amount, String paymentMethod) {
        System.out.println(ConsoleColors.YELLOW +
                "\n=== SIMULATING PAYMENT ===");
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Method: " + paymentMethod);
        System.out.println("Processing..." + ConsoleColors.RESET);

        try {
            // Simulate network delay
            Thread.sleep(2000);

            // Simulate payment success (90% success rate for demo)
            Random random = new Random();
            boolean success = random.nextInt(100) < 90;

            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "✓ Payment successful!" + ConsoleColors.RESET);
                return true;
            } else {
                System.out.println(ConsoleColors.RED +
                        "✗ Payment failed! Please try again." + ConsoleColors.RESET);
                return false;
            }
        } catch (InterruptedException e) {
            System.out.println(ConsoleColors.RED +
                    "Payment processing interrupted!" + ConsoleColors.RESET);
            return false;
        }
    }

    public static String generateTransactionId() {
        Random random = new Random();
        return "TXN" + System.currentTimeMillis() + random.nextInt(1000);
    }
}