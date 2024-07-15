package org.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Enums.FoodType;
import org.example.Enums.Originality;
import org.example.Enums.SpiceLevel;
import org.example.models.*;
import org.example.utils.InputHandler;
import org.example.utils.MessageUtil;
import org.example.utils.EntityTablePrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {
    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"), ip);
        List<FoodItem> foodItems = MessageUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        EntityTablePrinter.printEntitiesAsTable(foodItems);
    }

    public static void handleGiveFeedback(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("GIVE_FEEDBACK"),ip);

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

        MessageUtil.sendMessage(out, gson, feedback,ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

    public static void handleGiveVote(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("VOTE_RECOMMENDED_ITEMS"),ip);

        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter Meal Type : ");
        String mealType = scanner.nextLine();
        if(validMealTypes.contains(mealType)) {
            MessageUtil.sendMessage(out,gson,mealType,ip);
            MessageUtil.sendMessage(out,gson,userId,ip);
            String voteItemJson = MessageUtil.receiveMessage(in);
            List<FoodItem> votedItemList = gson.fromJson(voteItemJson, new TypeToken<List<FoodItem>>() {
            }.getType());
           // votedItemList.forEach(System.out::println);
            EntityTablePrinter.printEntitiesAsTable(votedItemList);
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
                MessageUtil.sendMessage(out,gson,"No valid IDs",ip);
                System.out.println(MessageUtil.receiveMessage(in));
            } else {
                String votedItemIdsJson = gson.toJson(votedItemIds);
                out.println(votedItemIdsJson);
                String response = MessageUtil.receiveMessage(in);
                System.out.println(response);
            }
        }else {
            MessageUtil.sendMessage(out,gson,"Invalid Meal Type",ip);
            System.out.println(MessageUtil.receiveMessage(in));
        }

    }

    public static void handleNotification(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"), ip);
        String notificationJson = MessageUtil.receiveMessage(in);
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        EntityTablePrinter.printEntitiesAsTable(notifications);
    }

    public static void handleTodayMenuItems(PrintWriter out, BufferedReader in, Gson gson , int userId, InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("VIEW_TODAY_MENU"),ip);
        MessageUtil.sendMessage(out,gson,userId,ip);
        String foodItemJson = MessageUtil.receiveMessage(in);
        List<FoodItem> foodItems =  gson.fromJson(foodItemJson, new TypeToken<List<FoodItem>>(){}.getType());
        EntityTablePrinter.printEntitiesAsTable(foodItems);
    }

    public static void handleAddUserProfile(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , int userId, InetAddress ip ) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("CREATE_USER_PROFILE"),ip);
        FoodType foodType = InputHandler.getEnumInput(scanner, FoodType.class, "Enter Food Type: (press 1. Vegetarian 2. Non-Vegetarian): ");
        SpiceLevel spiceLevel = InputHandler.getEnumInput(scanner, SpiceLevel.class, "Enter Spice Level: (press 1. High 2. Medium 3. Low): ");
        Originality originality = InputHandler.getEnumInput(scanner, Originality.class, "Enter Originality: (press 1. North-Indian 2. South-Indian): ");
        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());
        UserProfile userProfile = new UserProfile(userId, foodType.toString(), spiceLevel.toString(), originality.toString(), sweetTooth);
        MessageUtil.sendMessage(out, gson, userProfile,ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

    public static void handleUpdateUserProfile(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , int userId, InetAddress ip ) throws IOException {
        MessageUtil.sendMessage(out, gson, new MessageType("UPDATE_USER_PROFILE"),ip);
        FoodType foodType = InputHandler.getEnumInput(scanner, FoodType.class, "Enter Food Type: (press 1. Vegetarian 2. Non-Vegetarian): ");
        SpiceLevel spiceLevel = InputHandler.getEnumInput(scanner, SpiceLevel.class, "Enter Spice Level: (press 1. High 2. Medium 3. Low): ");
        Originality originality = InputHandler.getEnumInput(scanner, Originality.class, "Enter Originality: (press 1. North-Indian 2. South-Indian): ");
        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());
        UserProfile userProfile = new UserProfile(userId, foodType.toString(), spiceLevel.toString(), originality.toString(), sweetTooth);
        MessageUtil.sendMessage(out, gson, userProfile,ip);
        System.out.println(MessageUtil.receiveMessage(in));
    }

}
