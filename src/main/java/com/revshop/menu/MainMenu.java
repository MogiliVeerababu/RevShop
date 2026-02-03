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
            System.out.println("4. Exit");
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

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            Object user = authService.login(email, password);

            if (user != null) {
                System.out.println(ConsoleColors.GREEN +
                        "Login successful!" + ConsoleColors.RESET);

                // Show appropriate menu based on user role
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
            // Get user details
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

            // Create buyer object
            com.revshop.model.Buyer buyer = new com.revshop.model.Buyer(username, email, password);
            buyer.setFirstName(firstName);
            buyer.setLastName(lastName);
            buyer.setPhone(phone);
            buyer.setAddress(address);

            // Register buyer
            boolean success = authService.registerBuyer(buyer);

            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "Buyer registration successful! You can now login." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "Registration failed! Username or email already exists." + ConsoleColors.RESET);
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
            // Get user details
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

            // Create seller object
            com.revshop.model.Seller seller = new com.revshop.model.Seller(username, email, password);
            seller.setBusinessName(businessName);
            seller.setBusinessAddress(businessAddress);
            seller.setBusinessPhone(businessPhone);
            seller.setTaxId(taxId);

            // Register seller
            boolean success = authService.registerSeller(seller);

            if (success) {
                System.out.println(ConsoleColors.GREEN +
                        "Seller registration successful! You can now login." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED +
                        "Registration failed! Username or email already exists." + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED +
                    "Registration error: " + e.getMessage() + ConsoleColors.RESET);
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
}