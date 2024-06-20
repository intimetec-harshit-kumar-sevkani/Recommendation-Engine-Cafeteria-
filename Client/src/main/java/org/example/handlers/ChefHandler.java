package org.example.handlers;

public class ChefHandler {

    public static boolean handleChefSelection(String selection) {
        switch (selection) {
            case "1":
                System.out.println("Viewing recipes...");
                return false;
            case "2":
                System.out.println("Adding a recipe...");
                return false;
            case "3":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }
}
