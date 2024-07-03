package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.*;
import org.example.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ChefHandler {

    public static boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        switch (selection) {
            case "1":
                handleGetRecommendedItems(scanner, out, in, gson);
                return false;
            case "2":
                handleViewAllFoodItems(out, in, gson);
                return false;
            case "3":
                handleRollOutFoodItems(scanner, out, in, gson);
                return false;
            case "4" :
                handleViewNotification(out, in, gson);
                return false;
            case "5":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

    private static void handleGetRecommendedItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("GET_RECOMMENDED_ITEMS"));

        RecommendedDTO recommendedDTO = new RecommendedDTO();
        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            recommendedDTO.setMealType(mealType);

            System.out.println("Enter Number of Items:");
            recommendedDTO.setNumberOfItems(scanner.nextInt());
            scanner.nextLine();
            MessageUtils.sendMessage(out, gson, recommendedDTO);
            List<FoodItem> recommendedItems = MessageUtils.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
            recommendedItems.forEach(System.out::println);
        } else {
            out.println("Invalid Meal Type");
            System.out.println(MessageUtils.receiveMessage(in));
           }

    }

    private static void handleRollOutFoodItems(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("GET_VOTED_ITEMS"));

        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter the Meal Type:");
        String mealType = scanner.nextLine();
        if (validMealTypes.contains(mealType)) {
            out.println(mealType);

            List<RollOutFoodItemsDTO> foodItemList = MessageUtils.receiveMessage(in, gson, new TypeToken<List<RollOutFoodItemsDTO>>() {}.getType());
            foodItemList.forEach(System.out::println);

            List<Integer> foodItemIds = new ArrayList<>();
            System.out.println("Enter the IDs of the food items to roll out:");
            String[] inputIds = scanner.nextLine().split(",");
            for (String inputId : inputIds) {
                foodItemIds.add(Integer.parseInt(inputId.trim()));
            }

            MessageUtils.sendMessage(out, gson, foodItemIds);
            System.out.println(MessageUtils.receiveMessage(in));
        } else {
            out.println("Invalid Meal Type");
            System.out.println(MessageUtils.receiveMessage(in));
          }

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
