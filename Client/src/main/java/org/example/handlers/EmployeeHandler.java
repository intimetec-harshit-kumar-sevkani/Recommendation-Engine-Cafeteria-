package org.example.handlers;

public class EmployeeHandler {

    public static boolean handleEmployeeSelection(String selection) {
        switch (selection) {
            case "1":
                System.out.println("Viewing inventory...");
                return false;
            case "2":
                System.out.println("Updating inventory...");
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
