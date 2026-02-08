package com.revshop;

import com.revshop.menu.MainMenu;
import com.revshop.util.ConsoleColors;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println(ConsoleColors.PURPLE_BOLD+
                "╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              Welcome to RevShop                          ║");
        System.out.println("║              E-Commerce Platform                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝" +
                ConsoleColors.RESET);

        try {
            // Start the main menu
            MainMenu mainMenu = new MainMenu();
            mainMenu.show();

        } catch (Exception e) {
            System.err.println("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println(ConsoleColors.PURPLE +
                    "\nThank you for using RevShop! Visit Again" + ConsoleColors.RESET);
        }
    }
}
