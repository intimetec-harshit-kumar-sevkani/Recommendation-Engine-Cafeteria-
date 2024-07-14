package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.*;
import org.example.utils.ClientUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;

public class ChefService {
    public static void handleGetRecommendedItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("GET_RECOMMENDED_ITEMS"),ip);

        RecommendedDTO recommendedDTO = new RecommendedDTO();
        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            recommendedDTO.setMealType(mealType);

            System.out.println("Enter Number of Items:");
            recommendedDTO.setNumberOfItems(scanner.nextInt());
            scanner.nextLine();
            ClientUtil.sendMessage(out, gson, recommendedDTO,ip);
            List<FoodItem> recommendedItems = ClientUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
            recommendedItems.forEach(System.out::println);
        } else {
            out.println("Invalid Meal Type");
            System.out.println(ClientUtil.receiveMessage(in));
        }

    }

    public static void handleRollOutFoodItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("GET_VOTED_ITEMS"),ip);

        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            ClientUtil.sendMessage(out,gson,mealType,ip);
            List<RollOutFoodItemsDTO> foodItemList = ClientUtil.receiveMessage(in, gson, new TypeToken<List<RollOutFoodItemsDTO>>() {}.getType());
            foodItemList.forEach(System.out::println);

            List<Integer> foodItemIds = new ArrayList<>();
            System.out.println("Enter the IDs of the food items to roll out:");
            String[] inputIds = scanner.nextLine().split(",");
            for (String inputId : inputIds) {
                foodItemIds.add(Integer.parseInt(inputId.trim()));
            }

            ClientUtil.sendMessage(out, gson, foodItemIds,ip);
            System.out.println(ClientUtil.receiveMessage(in));
        } else {
            out.println("Invalid Meal Type");
            System.out.println(ClientUtil.receiveMessage(in));
        }

    }

    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"),ip);

        List<FoodItem> foodItems = ClientUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        foodItems.forEach(System.out::println);
    }

    public static void handleViewNotification(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"),ip);
        String notificationJson = ClientUtil.receiveMessage(in);
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }

    public static void handleDiscardMenuItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_DISCARD_ITEMS"),ip);

        List<FoodItem> foodItems = ClientUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        foodItems.forEach(System.out::println);


        System.out.println("Choose an option:");
        System.out.println("1. Discard a Food Item");
        System.out.println("2. Send Notification for Feedback");
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" :
                ClientUtil.sendMessage(out, gson, "Discard_Food_Items",ip);
                System.out.print("Enter the FoodItemIds to discard (comma-separated): ");
                String input = in.readLine().trim();
                List<Integer> foodItemIds = parseFoodItemIds(input);
                ClientUtil.sendMessage(out, gson, foodItemIds,ip);
                System.out.println(ClientUtil.receiveMessage(in));
                break;
            case "2":
                ClientUtil.sendMessage(out, gson, "Send_Notification",ip);
                ClientUtil.sendMessage(out, gson, foodItems,ip);
                System.out.println(ClientUtil.receiveMessage(in));
                break;
            default:
                System.out.println("Invalid choice.");
        }

    }

    public static List<Integer> parseFoodItemIds(String input) {
        List<Integer> foodItemIds = new ArrayList<>();
        String[] ids = input.split(",");
        for (String id : ids) {
            try {
                foodItemIds.add(Integer.parseInt(id.trim()));
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID: " + id);
            }
        }
        return foodItemIds;
    }
}
