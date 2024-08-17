package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.EmployeeController;
import org.example.controller.FoodItemController;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.DTO.MessageType;
import org.example.model.UserProfile;
import org.example.util.MessageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeHandlerImpl implements EmployeeHandler, RoleHandler {
    private final FoodItemController foodItemController;
    private final EmployeeController employeeController;
    private final MessageProcessor messageProcessor;

    public EmployeeHandlerImpl() throws SQLException {
        this.foodItemController = new FoodItemController();
        this.employeeController = new EmployeeController();
        this.messageProcessor = new MessageProcessor(new Gson());
    }

    public EmployeeHandlerImpl(FoodItemController foodItemController, EmployeeController employeeController, MessageProcessor messageProcessor) throws SQLException {
        this.foodItemController = new FoodItemController();
        this.employeeController = new EmployeeController();
        this.messageProcessor = new MessageProcessor(new Gson());
    }

    @Override
    public void handleRequest(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "VIEW_ALL_FOOD_ITEMS":
                handleViewAllFoodItems(out);
                break;
            case "GIVE_FEEDBACK":
                handleFeedback(in, out);
                break;
            case "VOTE_RECOMMENDED_ITEMS":
                handleVotedFoodItems(in, out);
                break;
            case "VIEW_NOTIFICATION":
                handleNotifications(in, out);
                break;
            case "VIEW_TODAY_MENU":
                handleTodayMenuItems(in, out);
                break;
            case "CREATE_USER_PROFILE":
                handleAddUserProfile(in, out);
                break;
            case "UPDATE_USER_PROFILE" :
                handleUpdateUserProfile(in, out);
                break;
            default:
                System.out.println("Unknown message type: " + messageType.type);
        }
    }

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItems = foodItemController.getAllFoodItems();
        messageProcessor.sendMessage(out, foodItems);
    }

    public void handleFeedback(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<Feedback> feedbackWrapper = messageProcessor.processMessage(in, Feedback.class);
        Feedback feedback = feedbackWrapper.getMessage();
        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = new Gson().fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());
        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());
        if (validFoodItemIds.contains(feedback.getFoodItemId())) {
            String response = employeeController.addFeedback(feedback);
            messageProcessor.sendMessage(out, response);
        } else {
            messageProcessor.sendMessage(out, "Invalid Food Item Id");
        }
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException {
        String notifications = employeeController.getNotifications();
        messageProcessor.sendMessage(out, notifications);
    }

    public void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<String> mealTypeWrapper = messageProcessor.processMessage(in, String.class);
        String mealType = mealTypeWrapper.getMessage();
        if ("Invalid Meal Type".equals(mealType)) {
            messageProcessor.sendMessage(out, "Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        } else {
            MessageProcessor.MessageWrapper<Integer> userIdWrapper = messageProcessor.processMessage(in, Integer.class);
            int userId = userIdWrapper.getMessage();
            String foodItems = employeeController.viewRollOutItems(mealType, userId);
            messageProcessor.sendMessage(out, foodItems);
            String votedItemIds = in.readLine();
            if ("No valid IDs".equals(votedItemIds)) {
                messageProcessor.sendMessage(out, "No valid IDs entered.");
            } else {
                List<Integer> votedItems = new Gson().fromJson(votedItemIds, new TypeToken<List<Integer>>() {}.getType());
                String response = employeeController.voteForFoodItems(votedItems);
                messageProcessor.sendMessage(out, response);
            }
        }
    }

    public void handleTodayMenuItems(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<Integer> userIdWrapper = messageProcessor.processMessage(in, Integer.class);
        int userId = userIdWrapper.getMessage();
        String foodItems = employeeController.viewTodayMenu(userId);
        messageProcessor.sendMessage(out, foodItems);
    }

    public void handleAddUserProfile(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<UserProfile> userProfileWrapper = messageProcessor.processMessage(in, UserProfile.class);
        UserProfile userProfile = userProfileWrapper.getMessage();
        String response = employeeController.addUserProfile(userProfile);
        messageProcessor.sendMessage(out, response);
    }

    public void handleUpdateUserProfile(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<UserProfile> userProfileWrapper = messageProcessor.processMessage(in, UserProfile.class);
        UserProfile userProfile = userProfileWrapper.getMessage();
        String response = employeeController.updateUserProfile(userProfile);
        messageProcessor.sendMessage(out, response);
    }
}

