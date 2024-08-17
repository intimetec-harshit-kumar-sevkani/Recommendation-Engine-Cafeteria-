package org.example.utils;

import java.util.Scanner;

public class InputHandler {
    public static <T extends Enum<T>> T getEnumInput(Scanner scanner, Class<T> enumClass, String prompt) {
        T[] enumConstants = enumClass.getEnumConstants();
        int min = enumConstants[0].ordinal() + 1;
        int max = enumConstants[enumConstants.length - 1].ordinal() + 1;

        while (true) {
            System.out.print(prompt);
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < min || choice > max) {
                    throw new NumberFormatException();
                }
                return enumClass.getEnumConstants()[choice - 1];
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            }
        }
    }

}
