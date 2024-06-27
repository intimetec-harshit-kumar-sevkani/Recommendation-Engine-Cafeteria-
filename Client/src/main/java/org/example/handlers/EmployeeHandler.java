package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Feedback;
import org.example.models.FoodItem;
import org.example.models.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
    private static void voteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
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
        //LocalDate localDate = LocalDate.now();
       // Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //feedback.setDate(localDate);
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
