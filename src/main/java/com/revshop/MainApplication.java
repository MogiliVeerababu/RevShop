package com.revshop;

import com.revshop.menu.MainMenu;
import com.revshop.util.ConsoleColors;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println(ConsoleColors.CYAN_BOLD +
                "==========================================");
        System.out.println("      Welcome to RevShop E-Commerce");
        System.out.println("==========================================" +
                ConsoleColors.RESET);

        try {
            // Start the main menu
            MainMenu mainMenu = new MainMenu();
            mainMenu.show();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println(ConsoleColors.GREEN +
                    "\nThank you for using RevShop! Goodbye!" + ConsoleColors.RESET);
        }
    }
}