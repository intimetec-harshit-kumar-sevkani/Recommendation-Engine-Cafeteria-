package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Enums.FoodType;
import org.example.Enums.Originality;
import org.example.Enums.SpiceLevel;
import org.example.models.FoodItem;
import org.example.DTO.MessageDTO;
import org.example.models.Notification;
import org.example.utils.InputHandler;
import org.example.utils.MessageUtil;
import org.example.utils.EntityTablePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminService {
    public static void handleAddFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageDTO("ADD_FOOD_ITEM"), ip);
        System.out.println("Enter meal type ID:");
        int mealTypeId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter food name:");
        String name = scanner.nextLine();
        System.out.println("Enter food price:");
        BigDecimal price = new BigDecimal(scanner.nextLine());
        System.out.println("Is the food available? (true/false):");
        boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());
        FoodType foodType = InputHandler.getEnumInput(scanner, FoodType.class, "Enter Food Type: (press 1. Vegetarian 2. Non-Vegetarian): ");
        SpiceLevel spiceLevel = InputHandler.getEnumInput(scanner, SpiceLevel.class, "Enter Spice Level: (press 1. High 2. Medium 3. Low): ");
        Originality originality = InputHandler.getEnumInput(scanner, Originality.class, "Enter Originality: (press 1. North-Indian 2. South-Indian): ");
        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());
        FoodItem foodItem = new FoodItem(mealTypeId, name, price, isAvailable, false, foodType.toString(), spiceLevel.toString(), originality.toString(), sweetTooth);
        MessageUtil.sendMessage(out, gson, foodItem, ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

    public static void handleUpdateFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out,gson,new MessageDTO("UPDATE_FOOD_ITEM"),ip);
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
        FoodType foodType = InputHandler.getEnumInput(scanner, FoodType.class, "Enter Food Type: (press 1. Vegetarian 2. Non-Vegetarian): ");
        SpiceLevel spiceLevel = InputHandler.getEnumInput(scanner, SpiceLevel.class, "Enter Spice Level: (press 1. High 2. Medium 3. Low): ");
        Originality originality = InputHandler.getEnumInput(scanner, Originality.class, "Enter Originality: (press 1. North-Indian 2. South-Indian): ");
        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());
        FoodItem foodItem = new FoodItem(id, mealTypeId, name, price, isAvailable, false , foodType.toString(),spiceLevel.toString(),originality.toString(),sweetTooth);
        MessageUtil.sendMessage(out,gson,foodItem,ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

    public static void handleDeleteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson ,  InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out,gson,new MessageDTO("DELETE_FOOD_ITEM"),ip);
        System.out.println("Enter food ID to delete:");
        int id = Integer.parseInt(scanner.nextLine());
        MessageUtil.sendMessage(out,gson,id,ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson,  InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out,gson,new MessageDTO("VIEW_ALL_FOOD_ITEMS"),ip);
        List<FoodItem> foodItems = MessageUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        EntityTablePrinter.printEntitiesAsTable(foodItems);
    }
    public static void handleViewNotification(PrintWriter out, BufferedReader in, Gson gson,  InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out,gson,new MessageDTO("VIEW_NOTIFICATION"),ip);
        String notificationJson = MessageUtil.receiveMessage(in);
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        EntityTablePrinter.printEntitiesAsTable(notifications);
    }
   public static void handleDiscardMenuItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
       MessageUtil.sendMessage(out, gson, new MessageDTO("VIEW_DISCARD_ITEMS"), ip);
       List<FoodItem> foodItems = MessageUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
       EntityTablePrinter.printEntitiesAsTable(foodItems);
       System.out.println("Choose an option:");
       System.out.println("1. Discard a Food Item");
       System.out.println("2. Send Notification for Feedback");
       System.out.print("Enter your choice: ");
       Scanner scanner = new Scanner(System.in);
       String choice = scanner.nextLine();
           switch (choice) {
               case "1":
                   System.out.print("Enter the FoodItemIds to discard (comma-separated): ");
                   List<Integer> foodItemIds = new ArrayList<>();
                   String[] inputIds = scanner.nextLine().split(",");
                   boolean validIds = true;
                   for (String inputId : inputIds) {
                       int id = Integer.parseInt(inputId.trim());
                       boolean idExists = foodItems.stream().anyMatch(item -> item.getId() == id);
                       if (idExists) {
                           foodItemIds.add(id);
                       } else {
                           validIds = false;
                           System.out.println("Invalid Id entered: " + id);
                           break;
                       }
                   }
                   if (validIds) {
                       MessageUtil.sendMessage(out, gson, "Discard_Food_Items", ip);
                       MessageUtil.sendMessage(out, gson, foodItemIds, ip);
                       System.out.println(MessageUtil.receiveMessage(in));
                   } else {
                       MessageUtil.sendMessage(out, gson, "INVALID_IDS", ip);
                   }
                   break;
               case "2":
                   MessageUtil.sendMessage(out, gson, "Send_Notification", ip);
                   MessageUtil.sendMessage(out, gson, foodItems, ip);
                   System.out.println(MessageUtil.receiveMessage(in));
                   break;
               default:
                   System.out.println("Invalid Choice");
                   MessageUtil.sendMessage(out, gson, "INVALID_CHOICE", ip);
           }
   }
}
