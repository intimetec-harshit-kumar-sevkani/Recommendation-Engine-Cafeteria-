package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*

public class EmployeeHandler {

    public static boolean handleEmployeeSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws Exception {
        switch (selection) {
            case "1":
                MessageType voteFoodItem = new MessageType("VOTE_RECOMMENDED_ITEMS");
                String voteFoodItemJson = gson.toJson(voteFoodItem);
                out.println(voteFoodItemJson);
                voteFoodItem(scanner, out, in, gson);
                return false;
            case "2":
                MessageType viewAllFoodItems = new MessageType("VIEW_ALL_FOOD_ITEMS");
                String viewAllFoodItemsJson = gson.toJson(viewAllFoodItems);
                out.println(viewAllFoodItemsJson);
                viewAllFoodItems(out, in, gson);
                return false;
            case "3":
                System.out.println("Exiting...");
                return true;
            case "4":
                MessageType giveFeedback = new MessageType("GIVE_FEEDBACK");
                String giveFeedbackJson = gson.toJson(giveFeedback);
                out.println(giveFeedbackJson);
                giveFeedback(scanner,out,in,gson,userId);
                return false;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }
    public static void voteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        System.out.println("Enter Meal Type : ");
        String mealType = scanner.nextLine();
        out.println(mealType);
        String voteItemJson = in.readLine();
        List<FoodItem> votedItemList = gson.fromJson(voteItemJson, new TypeToken<List<FoodItem>>(){}.getType());
        votedItemList.forEach(System.out::println);
        List<Integer> votedItemIds = new ArrayList<>();
        System.out.println("Enter the IDs of the food items to vote for");
        String[] inputIds = scanner.nextLine().split(",");
        for (String inputId : inputIds) {
            votedItemIds.add(Integer.parseInt(inputId.trim()));
        }
        String votedItemIdsJson = gson.toJson(votedItemIds);
        out.println(votedItemIdsJson);
        String response = in.readLine();
        System.out.println(response);
    }

    public static void giveFeedback(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , int userId) throws Exception {
        Feedback feedback = new Feedback();
        System.out.println("Enter food Item Id : ");
        feedback.setFoodItemId(scanner.nextInt());
        System.out.println("Enter Rating (0-5) : ");
        feedback.setRating(scanner.nextDouble());
        System.out.println("Enter Comment : ");
        String comment = scanner.next();
        feedback.setComment(comment);
        feedback.setUserId(userId);
        feedback.setDelete(false);
        String feedbackJson = gson.toJson(feedback);
        out.println(feedbackJson);
        String response = in.readLine();
        System.out.println(response);
    }

    private static void viewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        String response = in.readLine();
        List<FoodItem> foodItems = gson.fromJson(response, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }
}
*/


import com.google.gson.Gson;
import org.example.utils.MessageUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

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
                System.out.println("Exiting...");
                return true;
            case "5":
                handleNotification(scanner, out, in, gson, userId);
                return false;
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
        scanner.nextLine(); // consume the newline
        System.out.println("Enter Rating (0-5) : ");
        feedback.setRating(scanner.nextDouble());
        scanner.nextLine(); // consume the newline
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
        System.out.println("Enter Meal Type : ");
        String mealType = scanner.nextLine();
        out.println(mealType);
        String voteItemJson = in.readLine();
        List<FoodItem> votedItemList = gson.fromJson(voteItemJson, new TypeToken<List<FoodItem>>(){}.getType());
        votedItemList.forEach(System.out::println);
        List<Integer> votedItemIds = new ArrayList<>();
        System.out.println("Enter the IDs of the food items to vote for");
        String[] inputIds = scanner.nextLine().split(",");
        for (String inputId : inputIds) {
            votedItemIds.add(Integer.parseInt(inputId.trim()));
        }
        String votedItemIdsJson = gson.toJson(votedItemIds);
        out.println(votedItemIdsJson);
        String response = in.readLine();
        System.out.println(response);

    }

    private static void handleNotification(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException {
        MessageUtils.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"));
        String notificationJson = in.readLine();
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }
}

