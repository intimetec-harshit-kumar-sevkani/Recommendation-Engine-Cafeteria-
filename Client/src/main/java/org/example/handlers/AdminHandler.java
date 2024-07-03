package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.FoodItem;
import org.example.models.MessageType;
import org.example.models.Notification;
import org.example.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AdminHandler {

    public static boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        switch (selection) {
            case "1":
                handleAddFoodItem(scanner, out, in, gson);
                return false;
            case "2":
                handleUpdateFoodItem(scanner, out, in, gson);
                return false;
            case "3":
                handleDeleteFoodItem(scanner, out, in, gson);
                return false;
            case "4":
                handleViewAllFoodItems(out, in, gson);
                return false;
            case "5":
                handleViewNotification(out, in , gson);
                return false;
            case "6":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

    private static void handleAddFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("ADD_FOOD_ITEM"));

        System.out.println("Enter meal type ID:");
        int mealTypeId = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter food name:");
        String name = scanner.nextLine();

        System.out.println("Enter food price:");
        BigDecimal price = new BigDecimal(scanner.nextLine());

        System.out.println("Is the food available? (true/false):");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());

        FoodItem foodItem = new FoodItem(mealTypeId, name, price, isAvailable, false);
        MessageUtils.sendMessage(out, gson, foodItem);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    private static void handleUpdateFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("UPDATE_FOOD_ITEM"));

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
        MessageUtils.sendMessage(out, gson, foodItem);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    private static void handleDeleteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("DELETE_FOOD_ITEM"));

        System.out.println("Enter food ID to delete:");
        int id = Integer.parseInt(scanner.nextLine());
        out.println(id);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    private static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"));

        List<FoodItem> foodItems = MessageUtils.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        foodItems.forEach(System.out::println);
    }
    private static void handleViewNotification(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"));
        String notificationJson = in.readLine();
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }

}
