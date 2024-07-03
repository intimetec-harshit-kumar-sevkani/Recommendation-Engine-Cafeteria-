package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Feedback;
import org.example.models.FoodItem;
import org.example.models.MessageType;
import org.example.models.Notification;
import org.example.utils.MessageUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeHandler {

    public static boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException {
        switch (selection) {
            case "1":
                handleViewAllFoodItems(out, in, gson);
                return false;
            case "2":
                handleGiveFeedback(scanner, out, in, gson, userId);
                return false;
            case "3":
                handleGiveVote(scanner, out, in, gson, userId);
                return false;
            case "4":
                handleNotification(scanner, out, in, gson, userId);
                return false;
            case "5":
                handleTodayMenuItems(out, in, gson);
                return false;
            case "6":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

    private static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"));

        List<FoodItem> foodItems = MessageUtils.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        foodItems.forEach(System.out::println);
    }

    private static void handleGiveFeedback(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("GIVE_FEEDBACK"));

        Feedback feedback = new Feedback();
        System.out.println("Enter food Item Id : ");
        feedback.setFoodItemId(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Enter Rating (0-5) : ");
        feedback.setRating(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Enter Comment : ");
        String comment = scanner.nextLine();
        feedback.setComment(comment);
        feedback.setUserId(userId);
        feedback.setDelete(false);

        MessageUtils.sendMessage(out, gson, feedback);
        System.out.println(MessageUtils.receiveMessage(in));
    }

    private static void handleGiveVote(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VOTE_RECOMMENDED_ITEMS"));

        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter Meal Type : ");
        String mealType = scanner.nextLine();
        if(validMealTypes.contains(mealType)) {
            out.println(mealType);
            String voteItemJson = in.readLine();
            List<FoodItem> votedItemList = gson.fromJson(voteItemJson, new TypeToken<List<FoodItem>>() {
            }.getType());
            votedItemList.forEach(System.out::println);

            Set<Integer> validIds = votedItemList.stream().map(FoodItem::getId).collect(Collectors.toSet());


            List<Integer> votedItemIds = new ArrayList<>();
            System.out.println("Enter the IDs of the food items to vote for");
            String[] inputIds = scanner.nextLine().split(",");

            for (String inputId : inputIds) {
                int id = Integer.parseInt(inputId.trim());
                if (validIds.contains(id)) {
                    votedItemIds.add(id);
                } else {
                    System.out.println("Invalid item ID: " + id);
                }
            }

            if (votedItemIds.isEmpty()) {
                out.println("No valid IDs");
                System.out.println(MessageUtils.receiveMessage(in));
            } else {
                String votedItemIdsJson = gson.toJson(votedItemIds);
                out.println(votedItemIdsJson);
                String response = in.readLine();
                System.out.println(response);
            }
        }else {
            out.println("Invalid Meal Type");
            System.out.println(MessageUtils.receiveMessage(in));
        }

    }

    private static void handleNotification(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"));
        String notificationJson = in.readLine();
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }

    private static void handleTodayMenuItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_TODAY_MENU"));

        String foodItemJson = in.readLine();
        List<FoodItem> foodItems =  gson.fromJson(foodItemJson, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }

}

