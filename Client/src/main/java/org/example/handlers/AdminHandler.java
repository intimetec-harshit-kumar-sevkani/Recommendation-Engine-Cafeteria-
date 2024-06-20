package org.example.handlers;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.FoodItem;
import org.example.models.MessageType;

public class AdminHandler {

    public static boolean handleAdminSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        switch (selection) {
            case "1":
                MessageType addFoodItem = new MessageType("ADD_FOOD_ITEM");
                String addFoodItemJson = gson.toJson(addFoodItem);
                out.println(addFoodItemJson);
                addFoodItem(scanner, out, in, gson);
                return false;
            case "2":
                MessageType updateFoodItem = new MessageType("UPDATE_FOOD_ITEM");
                String updateFoodItemJson = gson.toJson(updateFoodItem);
                out.println(updateFoodItemJson);
                updateFoodItem(scanner, out,in, gson);
                return false;
            case "3":
                MessageType deleteFoodItem = new MessageType("DELETE_FOOD_ITEM");
                String deleteFoodItemJson = gson.toJson(deleteFoodItem);
                out.println(deleteFoodItemJson);
                deleteFoodItem(scanner, out, in,  gson);
                return false;
            case "4":
                MessageType viewAllFoodItems = new MessageType("VIEW_ALL_FOOD_ITEMS");
                String viewAllFoodItemsJson = gson.toJson(viewAllFoodItems);
                out.println(viewAllFoodItemsJson);
                viewAllFoodItems(out, in, gson);
                return false;
            case "5":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

    private static void addFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        System.out.println("Enter meal type ID:");
        int mealTypeId = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter food name:");
        String name = scanner.nextLine();

        System.out.println("Enter food price:");
        BigDecimal price = new BigDecimal(scanner.nextLine());

        System.out.println("Is the food available? (true/false):");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());

        FoodItem foodItem = new FoodItem(mealTypeId, name, price, isAvailable, false);
        String foodItemJson = gson.toJson(foodItem);
        out.println(foodItemJson);
        String response = in.readLine();
        System.out.println(response);
    }

    private static void updateFoodItem(Scanner scanner, PrintWriter out,  BufferedReader in ,  Gson gson) throws IOException {
        System.out.println("Enter food ID to update:");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter new meal type ID:");
        int mealTypeId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter new food name:");
        String name = scanner.nextLine();
        System.out.println("Enter new price:");
        BigDecimal price = new BigDecimal(scanner.nextLine());
        System.out.println("Is the food available? (true/false):");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());

        FoodItem foodItem = new FoodItem(id, mealTypeId, name, price, isAvailable, false);
        String foodItemJson = gson.toJson(foodItem);
        out.println(foodItemJson);
        System.out.println(in.readLine());
    }

    private static void deleteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in,  Gson gson) throws IOException {
        System.out.println("Enter food ID to delete:");
        int id = Integer.parseInt(scanner.nextLine());
        out.println(id);
        System.out.println(in.readLine());
    }

    private static void viewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        String response = in.readLine();
        List<FoodItem> foodItems = gson.fromJson(response, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }
}
