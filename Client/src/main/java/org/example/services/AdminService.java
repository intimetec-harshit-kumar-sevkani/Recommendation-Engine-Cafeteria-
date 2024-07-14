package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.FoodItem;
import org.example.models.MessageType;
import org.example.models.Notification;
import org.example.utils.ClientUtil;
import org.example.utils.EntityTablePrinter;
import org.example.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;

public class AdminService {


    public static void handleAddFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , InetAddress ip) throws IOException {
       // MessageUtils.sendMessage(out, gson, new MessageType("ADD_FOOD_ITEM"));
        ClientUtil.sendMessage(out,gson,new MessageType("ADD_FOOD_ITEM"),ip);

        System.out.println("Enter meal type ID:");
        int mealTypeId = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter food name:");
        String name = scanner.nextLine();

        System.out.println("Enter food price:");
        BigDecimal price = new BigDecimal(scanner.nextLine());

        System.out.println("Is the food available? (true/false):");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Enter Food Type: ");
        String foodType = scanner.nextLine().trim();

        System.out.print("Enter Spice Level: ");
        String spiceLevel = scanner.nextLine().trim();

        System.out.print("Enter Originality: ");
        String originality = scanner.nextLine().trim();

        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());

        FoodItem foodItem = new FoodItem(mealTypeId, name, price, isAvailable, false , foodType , spiceLevel , originality , sweetTooth);
       // MessageUtils.sendMessage(out, gson, foodItem);
        ClientUtil.sendMessage(out,gson,foodItem,ip);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    public static void handleUpdateFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
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

        System.out.print("Enter Food Type: ");
        String foodType = scanner.nextLine().trim();

        System.out.print("Enter Spice Level: ");
        String spiceLevel = scanner.nextLine().trim();

        System.out.print("Enter Originality: ");
        String originality = scanner.nextLine().trim();

        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());


        FoodItem foodItem = new FoodItem(id, mealTypeId, name, price, isAvailable, false , foodType,spiceLevel,originality,sweetTooth);
        MessageUtils.sendMessage(out, gson, foodItem);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    public static void handleDeleteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("DELETE_FOOD_ITEM"));

        System.out.println("Enter food ID to delete:");
        int id = Integer.parseInt(scanner.nextLine());
        out.println(id);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"));

        List<FoodItem> foodItems = MessageUtils.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        // foodItems.forEach(System.out::println);
        EntityTablePrinter.printEntitiesAsTable(foodItems);
    }
    public static void handleViewNotification(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"));
        String notificationJson = in.readLine();
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }

}
