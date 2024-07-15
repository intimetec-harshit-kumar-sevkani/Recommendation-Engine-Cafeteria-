package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.FoodItemController;
import org.example.controller.NotificationController;
import org.example.model.FoodItem;
import org.example.DTO.MessageType;
import org.example.DTO.RecommendedDTO;
import org.example.util.MessageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChefHandlerImpl implements ChefHandler, RoleHandler{
    private ChefController chefController;

    private FoodItemController foodItemController;

    private NotificationController notificationController;

    private final MessageProcessor messageProcessor;

    public ChefHandlerImpl() throws SQLException {
        this.chefController = new ChefController();
        this.foodItemController = new FoodItemController();
        this.notificationController = new NotificationController();
        this.messageProcessor = new MessageProcessor(new Gson());
    }

    private Gson gson = new Gson();


    @Override
    public void handleRequest(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "GET_RECOMMENDED_ITEMS":
                handleRecommendationFoodItems(in, out);
                break;
            case "GET_VOTED_ITEMS":
                handleRollOutItems(in, out);
                break;
            case "VIEW_ALL_FOOD_ITEMS":
                handleViewAllFoodItems(out);
                break;
            case "VIEW_NOTIFICATION":
                handleNotifications(in, out);
                break;
            case "VIEW_DISCARD_ITEMS":
                handleDiscardMenuItems(in, out);
                break;
            default:
                System.out.println("--------------");
        }
    }
    public void handleRecommendationFoodItems(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<RecommendedDTO> wrapper = messageProcessor.processMessage(in, RecommendedDTO.class);
        RecommendedDTO recommendedDTO = wrapper.getMessage();
        if ("Invalid Meal Type".equals(recommendedDTO)) {
            messageProcessor.sendMessage(out, "Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        } else {
            String foodItemsJson = chefController.getRecommendedFoodItems(recommendedDTO);
            messageProcessor.sendMessage(out, foodItemsJson);
        }
    }

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        messageProcessor.sendMessage(out, foodItemsJson);
    }

    public void handleRollOutItems(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<String> wrapper = messageProcessor.processMessage(in, String.class);
        String mealType = wrapper.getMessage();
        if ("Invalid Meal Type".equals(mealType)) {
            messageProcessor.sendMessage(out, "Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        } else {
            String response = chefController.getVotedItems(mealType);
            messageProcessor.sendMessage(out, response);
            MessageProcessor.MessageWrapper<List<Integer>> rollOutWrapper = messageProcessor.processMessage(in, new TypeToken<List<Integer>>() {}.getType());
            List<Integer> rollOutItems = rollOutWrapper.getMessage();
            String foodItemsJson = foodItemController.getAllFoodItems();
            List<FoodItem> foodItems = new Gson().fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());
            Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());
            boolean isValid = rollOutItems.stream().allMatch(validFoodItemIds::contains);
            if (isValid) {
                String rollOutResponse = chefController.rollOutFoodItems(rollOutItems);
                messageProcessor.sendMessage(out, rollOutResponse);
            } else {
                messageProcessor.sendMessage(out, "Invalid Food Item Id(s) in the list.");
            }
        }
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) {
        String notificationJson = chefController.getNotifications();
        messageProcessor.sendMessage(out, notificationJson);
    }

    public void handleDiscardMenuItems(BufferedReader in, PrintWriter out) throws IOException {
        messageProcessor.sendMessage(out, chefController.viewDiscardedItems());
        MessageProcessor.MessageWrapper<String> responseWrapper = messageProcessor.processMessage(in, String.class);
        String response = responseWrapper.getMessage();

        if ("Discard_Food_Items".equals(response)) {
            MessageProcessor.MessageWrapper<List<Integer>> foodItemIdsWrapper = messageProcessor.processMessage(in, new TypeToken<List<Integer>>() {}.getType());
            List<Integer> foodItemIds = foodItemIdsWrapper.getMessage();
            messageProcessor.sendMessage(out, chefController.discardItems(foodItemIds));
        }
        if ("Send_Notification".equals(response)) {
            MessageProcessor.MessageWrapper<List<FoodItem>> foodItemsWrapper = messageProcessor.processMessage(in, new TypeToken<List<FoodItem>>() {}.getType());
            List<FoodItem> foodItems = foodItemsWrapper.getMessage();
            messageProcessor.sendMessage(out, notificationController.sendNotifications(foodItems));
        }
    }
}
