package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTO.MessageDTO;
import org.example.DTO.RecommendedDTO;
import org.example.DTO.RollOutFoodItemsDTO;
import org.example.models.*;
import org.example.utils.MessageUtil;
import org.example.utils.EntityTablePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;

public class ChefService {
    public static void handleGetRecommendedItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageDTO("GET_RECOMMENDED_ITEMS"),ip);
        RecommendedDTO recommendedDTO = new RecommendedDTO();
        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));
        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            recommendedDTO.setMealType(mealType);
            System.out.println("Enter Number of Items:");
            recommendedDTO.setNumberOfItems(scanner.nextInt());
            scanner.nextLine();
            MessageUtil.sendMessage(out, gson, recommendedDTO,ip);
            List<FoodItem> recommendedItems = MessageUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
            EntityTablePrinter.printEntitiesAsTable(recommendedItems);
        } else {
            out.println("Invalid Meal Type");
            System.out.println(MessageUtil.receiveMessage(in));
        }

    }

    public static void handleRollOutFoodItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageDTO("GET_VOTED_ITEMS"),ip);
        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));
        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            MessageUtil.sendMessage(out,gson,mealType,ip);
            List<RollOutFoodItemsDTO> foodItemList = MessageUtil.receiveMessage(in, gson, new TypeToken<List<RollOutFoodItemsDTO>>() {}.getType());
            EntityTablePrinter.printEntitiesAsTable(foodItemList);
            List<Integer> foodItemIds = new ArrayList<>();
            System.out.println("Enter the IDs of the food items to roll out:");
            String[] inputIds = scanner.nextLine().split(",");
            for (String inputId : inputIds) {
                foodItemIds.add(Integer.parseInt(inputId.trim()));
            }
            MessageUtil.sendMessage(out, gson, foodItemIds,ip);
            System.out.println(MessageUtil.receiveMessage(in));
        } else {
            out.println("Invalid Meal Type");
            System.out.println(MessageUtil.receiveMessage(in));
        }
    }

    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageDTO("VIEW_ALL_FOOD_ITEMS"),ip);
        List<FoodItem> foodItems = MessageUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        EntityTablePrinter.printEntitiesAsTable(foodItems);
    }

    public static void handleViewNotification(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageDTO("VIEW_NOTIFICATION"),ip);
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
