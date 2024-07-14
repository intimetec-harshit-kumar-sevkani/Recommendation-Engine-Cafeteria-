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
import java.util.stream.Collectors;

public class EmployeeService {
    public static void handleViewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_ALL_FOOD_ITEMS"), ip);

        List<FoodItem> foodItems = ClientUtil.receiveMessage(in, gson, new TypeToken<List<FoodItem>>() {}.getType());
        foodItems.forEach(System.out::println);
    }

    public static void handleGiveFeedback(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("GIVE_FEEDBACK"),ip);

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

        ClientUtil.sendMessage(out, gson, feedback,ip);
        System.out.println(ClientUtil.receiveMessage(in));
    }

    public static void handleGiveVote(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VOTE_RECOMMENDED_ITEMS"),ip);

        Set<String> validMealTypes = new HashSet<>(Arrays.asList("Breakfast", "Lunch", "Dinner"));

        System.out.println("Enter Meal Type : ");
        String mealType = scanner.nextLine();
        if(validMealTypes.contains(mealType)) {
            ClientUtil.sendMessage(out,gson,mealType,ip);
            ClientUtil.sendMessage(out,gson,userId,ip);
            String voteItemJson = ClientUtil.receiveMessage(in);
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
                ClientUtil.sendMessage(out,gson,"No valid IDs",ip);
                System.out.println(ClientUtil.receiveMessage(in));
            } else {
                String votedItemIdsJson = gson.toJson(votedItemIds);
                out.println(votedItemIdsJson);
                String response = ClientUtil.receiveMessage(in);
                System.out.println(response);
            }
        }else {
            ClientUtil.sendMessage(out,gson,"Invalid Meal Type",ip);
            System.out.println(ClientUtil.receiveMessage(in));
        }

    }

    public static void handleNotification(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_NOTIFICATION"), ip);
        String notificationJson = ClientUtil.receiveMessage(in);
        List<Notification> notifications = gson.fromJson(notificationJson, new TypeToken<List<Notification>>(){}.getType());
        notifications.forEach(System.out::println);
    }

    public static void handleTodayMenuItems(PrintWriter out, BufferedReader in, Gson gson , int userId, InetAddress ip) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("VIEW_TODAY_MENU"),ip);
        ClientUtil.sendMessage(out,gson,userId,ip);
        String foodItemJson = ClientUtil.receiveMessage(in);
        List<FoodItem> foodItems =  gson.fromJson(foodItemJson, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }

    public static void handleUserProfile(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , int userId, InetAddress ip ) throws IOException {
        ClientUtil.sendMessage(out, gson, new MessageType("CREATE_USER_PROFILE"),ip);

        System.out.print("Enter Food Type: ");
        String foodType = scanner.nextLine().trim();

        System.out.print("Enter Spice Level: ");
        String spiceLevel = scanner.nextLine().trim();

        System.out.print("Enter Originality: ");
        String originality = scanner.nextLine().trim();

        System.out.print("Do you have a sweet tooth? (true/false): ");
        boolean sweetTooth = Boolean.parseBoolean(scanner.nextLine().trim());

        UserProfile userProfile = new UserProfile(userId, foodType, spiceLevel, originality, sweetTooth);
        ClientUtil.sendMessage(out, gson, userProfile,ip);

        System.out.println(ClientUtil.receiveMessage(in));
    }

}
