package com.revshop.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static Scanner scanner = new Scanner(System.in);

    // Email validation regex
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Password validation: at least 8 chars, 1 uppercase, 1 lowercase, 1 digit
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    // Phone validation: 10 digits
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{10}$");

    public static String getValidatedEmail() {
        while (true) {
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email cannot be empty!");
                continue;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("Invalid email format!");
                continue;
            }

            return email;
        }
    }

    public static String getValidatedPassword() {
        while (true) {
            System.out.print("Enter password (min 8 chars, 1 uppercase, 1 lowercase, 1 digit): ");
            String password = scanner.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("Password cannot be empty!");
                continue;
            }

            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                System.out.println("Password must have at least 8 characters, " +
                        "including uppercase, lowercase, and a digit!");
                continue;
            }

            System.out.print("Confirm password: ");
            String confirmPassword = scanner.nextLine().trim();

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match!");
                continue;
            }

            return password;
        }
    }

    public static String getValidatedPhone() {
        while (true) {
            System.out.print("Enter phone number (10 digits): ");
            String phone = scanner.nextLine().trim();

            if (PHONE_PATTERN.matcher(phone).matches()) {
                return phone;
            }
            System.out.println("Invalid phone number! Must be 10 digits.");
        }
    }

    public static String getNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty!");
        }
    }

    public static double getPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be positive!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format!");
            }
        }
    }

    public static int getPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be positive!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format!");
            }
        }
    }
}