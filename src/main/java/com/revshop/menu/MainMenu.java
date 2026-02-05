package com.revshop.menu;

import com.revshop.service.AuthService;
import com.revshop.util.ConsoleColors;
import com.revshop.util.ValidationUtil;
import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;
    private AuthService authService;
    private BuyerMenu buyerMenu;
    private SellerMenu sellerMenu;

    public MainMenu() {
        scanner = new Scanner(System.in);
        authService = new AuthService();
        buyerMenu = new BuyerMenu();
        sellerMenu = new SellerMenu();
    }

    public void show() {
        while (true) {
            System.out.println(ConsoleColors.CYAN_BOLD +
                    "\n=== REVSHOP MAIN MENU ===");
            System.out.println("1. Login");
            System.out.println("2. Register as Buyer");
            System.out.println("3. Register as Seller");
            System.out.println("4. Forgot Password");
            System.out.println("5. Forgot Email ID");
            System.out.println("6. Exit");
            System.out.print("Choose an option: " + ConsoleColors.RESET);

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    registerBuyer();
                    break;
                case 3:
                    registerSeller();
                    break;
                case 4:
                    forgotPassword();
                    break;
                case 5:
                    forgotEmailId();
                    break;
                case 6:
                    System.out.println(ConsoleColors.YELLOW +
                            "Exiting..." + ConsoleColors.RESET);
                    return;
                default:
                    System.out.println(ConsoleColors.RED +
                            "Invalid choice! Please try again." + ConsoleColors.RESET);
            }
        }
    }

    private void login() {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== LOGIN ===" + ConsoleColors.RESET);

        System.out.print("📩 Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("🔑 Password: ");
        String password = scanner.nextLine().trim();

        try {
            Object user = authService.login(email, password);

            if (user != null) {
                System.out.println(ConsoleColors.GREEN +
                        "😊 Login successful!" + ConsoleColors.RESET);

                if (user instanceof com.revshop.model.Buyer) {
                    buyerMenu.show((com.revshop.model.Buyer) user);
                } else if (user instanceof com.revshop.model.Seller) {
                    sellerMenu.show((com.revshop.model.Seller) user);
                }
            } else {
                System.out.println(ConsoleColors.RED +
                        "Invalid email or password!" + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Login failed: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void registerBuyer() {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== BUYER REGISTRATION ===" + ConsoleColors.RESET);

        try {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            String email = ValidationUtil.getValidatedEmail();
            String password = ValidationUtil.getValidatedPassword();

            System.out.print("First Name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Last Name: ");
            String lastName = scanner.nextLine().trim();

            String phone = ValidationUtil.getValidatedPhone();

            System.out.print("Address: ");
            String address = scanner.nextLine().trim();

            // Security question for account recovery
            System.out.println(ConsoleColors.PURPLE_BOLD +
                    "\n=== SECURITY SETUP ===" + ConsoleColors.RESET);
            System.out.println("Choose a security question for account recovery:");
            System.out.println("1. What is your father's name?");
            System.out.println("2. What is your mother's maiden name?");
            System.out.println("3. What is your birth city?");
            System.out.print("Select (1-3): ");

            int securityQuestion = getIntInputInRange(1, 3);

            System.out.print("Your answer: ");
            String securityAnswer = scanner.nextLine().trim().toLowerCase();

            // Create buyer object
            com.revshop.model.Buyer buyer = new com.revshop.model.Buyer(username, email, password);
            buyer.setFirstName(firstName);
            buyer.setLastName(lastName);
            buyer.setPhone(phone);
            buyer.setAddress(address);
            buyer.setSecurityQuestion(securityQuestion);
            buyer.setSecurityAnswer(securityAnswer);

            // Register buyer
            boolean success = authService.registerBuyer(buyer);

            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "✅ Buyer registration successful! You can now login." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "❌ Registration failed! Username or email already exists." + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Registration error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void registerSeller() {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== SELLER REGISTRATION ===" + ConsoleColors.RESET);

        try {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            String email = ValidationUtil.getValidatedEmail();
            String password = ValidationUtil.getValidatedPassword();

            System.out.print("Business Name: ");
            String businessName = scanner.nextLine().trim();

            System.out.print("Business Address: ");
            String businessAddress = scanner.nextLine().trim();

            String businessPhone = ValidationUtil.getValidatedPhone();

            System.out.print("Tax ID (optional): ");
            String taxId = scanner.nextLine().trim();

            // Security question for account recovery
            System.out.println(ConsoleColors.PURPLE_BOLD +
                    "\n=== SECURITY SETUP ===" + ConsoleColors.RESET);
            System.out.println("Choose a security question for account recovery:");
            System.out.println("1. What is your father's name?");
            System.out.println("2. What is your mother's maiden name?");
            System.out.println("3. What is your birth city?");
            System.out.print("Select (1-3): ");

            int securityQuestion = getIntInputInRange(1, 3);

            System.out.print("Your answer: ");
            String securityAnswer = scanner.nextLine().trim().toLowerCase();

            // Create seller object
            com.revshop.model.Seller seller = new com.revshop.model.Seller(username, email, password);
            seller.setBusinessName(businessName);
            seller.setBusinessAddress(businessAddress);
            seller.setBusinessPhone(businessPhone);
            seller.setTaxId(taxId);
            seller.setSecurityQuestion(securityQuestion);
            seller.setSecurityAnswer(securityAnswer);

            // Register seller
            boolean success = authService.registerSeller(seller);

            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "✅ Seller registration successful! You can now login." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "❌ Registration failed! Username or email already exists." + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Registration error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void forgotPassword() {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== FORGOT PASSWORD ===" + ConsoleColors.RESET);

        System.out.print("📩 Enter your email: ");
        String email = scanner.nextLine().trim();

        try {
            com.revshop.model.User user = authService.findUserByEmail(email);

            if (user == null) {
                System.out.println(ConsoleColors.RED +
                        "❌ No account found with this email!" + ConsoleColors.RESET);
                return;
            }

            System.out.println(ConsoleColors.PURPLE_BOLD +
                    "\n=== SECURITY VERIFICATION ===" + ConsoleColors.RESET);

            String questionText = getQuestionText(user.getSecurityQuestion());
            System.out.println("Question: " + questionText);
            System.out.print("Your answer: ");
            String userAnswer = scanner.nextLine().trim().toLowerCase();

            if (userAnswer.equals(user.getSecurityAnswer())) {
                System.out.println(ConsoleColors.GREEN +
                        "\n✅ Verification successful!" + ConsoleColors.RESET);

                System.out.println(ConsoleColors.BLUE_BOLD +
                        "\n=== SET NEW PASSWORD ===" + ConsoleColors.RESET);

                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine().trim();

                System.out.print("Confirm new password: ");
                String confirmPassword = scanner.nextLine().trim();

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println(ConsoleColors.RED +
                            "❌ Passwords do not match!" + ConsoleColors.RESET);
                    return;
                }

                if (newPassword.length() < 4) {
                    System.out.println(ConsoleColors.RED +
                            "❌ Password must be at least 4 characters!" + ConsoleColors.RESET);
                    return;
                }

                boolean success = authService.resetPassword(email, newPassword);

                if (success) {
                    System.out.println(ConsoleColors.GREEN +
                            "✅ Password reset successfully! You can now login with your new password." + ConsoleColors.RESET);
                } else {
                    System.out.println(ConsoleColors.RED +
                            "❌ Password reset failed!" + ConsoleColors.RESET);
                }
            } else {
                System.out.println(ConsoleColors.RED +
                        "\n❌ Incorrect answer! Password reset failed." + ConsoleColors.RESET);
            }

        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private void forgotEmailId() {
        System.out.println(ConsoleColors.BLUE_BOLD +
                "\n=== FORGOT EMAIL ID ===" + ConsoleColors.RESET);

        System.out.print("👤 Enter your username: ");
        String username = scanner.nextLine().trim();

        try {
            com.revshop.model.User user = authService.findUserByUsername(username);

            if (user == null) {
                System.out.println(ConsoleColors.RED +
                        "❌ No account found with this username!" + ConsoleColors.RESET);
                return;
            }

            System.out.println(ConsoleColors.PURPLE_BOLD +
                    "\n=== SECURITY VERIFICATION ===" + ConsoleColors.RESET);

            String questionText = getQuestionText(user.getSecurityQuestion());
            System.out.println("Question: " + questionText);
            System.out.print("Your answer: ");
            String userAnswer = scanner.nextLine().trim().toLowerCase();

            if (userAnswer.equals(user.getSecurityAnswer())) {
                System.out.println(ConsoleColors.GREEN +
                        "\n✅ Verification successful!" + ConsoleColors.RESET);

                String maskedEmail = user.getEmail();  // Show full email instead of masked
                System.out.println(ConsoleColors.CYAN_BOLD +
                        "\n📧 Your registered email address:" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "   " + maskedEmail + ConsoleColors.RESET);

                System.out.println(ConsoleColors.GREEN +
                        "\n💡 You can now use this email to login." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "\n❌ Incorrect answer! Cannot display email." + ConsoleColors.RESET);
            }

        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Error: " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private String getQuestionText(int questionNumber) {
        switch (questionNumber) {
            case 1: return "What is your father's name?";
            case 2: return "What is your mother's maiden name?";
            case 3: return "What is your birth city?";
            default: return "What is your father's name?";
        }
    }

    private String maskEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 2) {
            return localPart.charAt(0) + "***@" + domain;
        } else {
            char firstChar = localPart.charAt(0);
            char lastChar = localPart.charAt(localPart.length() - 1);
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < localPart.length() - 2; i++) {
                stars.append("*");
            }
            return firstChar + stars.toString() + lastChar + "@" + domain;
        }
    }

    private int getIntInput() {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                return choice;
            } catch (NumberFormatException e) {
                System.out.print(ConsoleColors.RED +
                        "Invalid input! Enter a number: " + ConsoleColors.RESET);
            }
        }
    }

    private int getIntInputInRange(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print(ConsoleColors.RED +
                            "Please enter a number between " + min + " and " + max + ": " + ConsoleColors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.print(ConsoleColors.RED +
                        "Invalid input! Enter a number: " + ConsoleColors.RESET);
            }
        }
    }
}