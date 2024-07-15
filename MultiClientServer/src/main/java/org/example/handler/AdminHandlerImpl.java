package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.FoodItemController;
import org.example.controller.NotificationController;
import org.example.model.FoodItem;
import org.example.DTO.MessageType;
import org.example.util.MessageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminHandlerImpl implements AdminHandler, RoleHandler{

    private FoodItemController foodItemController;
    private NotificationController notificationController;
    private ChefController chefController;
    private final MessageProcessor messageProcessor;

    public AdminHandlerImpl() throws SQLException {
        this.foodItemController = new FoodItemController();
        this.chefController = new ChefController();
        this.notificationController = new NotificationController();
        this.messageProcessor = new MessageProcessor(gson);
    }

    private Gson gson = new Gson();

    @Override
    public void handleRequest(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "ADD_FOOD_ITEM":
                handleAddFoodItem(in, out);
                break;
            case "UPDATE_FOOD_ITEM":
                handleUpdateFoodItem(in, out);
                break;
            case "DELETE_FOOD_ITEM":
                handleDeleteFoodItem(in, out);
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

    public void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<FoodItem> wrapper = messageProcessor.processMessage(in, FoodItem.class);
        FoodItem foodItem = wrapper.getMessage();
        String response = foodItemController.addFoodItem(foodItem);
        messageProcessor.sendMessage(out, response);
    }

    public void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<FoodItem> wrapper = messageProcessor.processMessage(in, FoodItem.class);
        FoodItem updatedFoodItem = wrapper.getMessage();

      //  String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemController.getAllFoodItems(), new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        String response;
        if (validFoodItemIds.contains(updatedFoodItem.getId())) {
            response = foodItemController.updateFoodItem(updatedFoodItem);
        } else {
            response = "Invalid Food Item Id.";
        }
        messageProcessor.sendMessage(out, response);
    }

    public void handleDeleteFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        MessageProcessor.MessageWrapper<Integer> wrapper = messageProcessor.processMessage(in, Integer.class);
        int id = wrapper.getMessage();

       // String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemController.getAllFoodItems(), new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        String response;
        if (validFoodItemIds.contains(id)) {
            response = foodItemController.deleteFoodItem(id);
        } else {
            response = "Invalid Food Item Id.";
        }
        messageProcessor.sendMessage(out, response);
    }

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItems = foodItemController.getAllFoodItems();
        out.println(foodItems);
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException {
        String notifications = foodItemController.getNotifications();
        messageProcessor.sendMessage(out, notifications);
    }

    public void handleDiscardMenuItems(BufferedReader in, PrintWriter out) throws IOException {
       // String discardItemJson = chefController.viewDiscardedItems();
        messageProcessor.sendMessage(out, chefController.viewDiscardedItems());

        MessageProcessor.MessageWrapper<String> responseWrapper = messageProcessor.processMessage(in, String.class);
        String response = responseWrapper.getMessage();

        if ("Discard_Food_Items".equals(response)) {
            MessageProcessor.MessageWrapper<List<Integer>> foodItemIdsWrapper = messageProcessor.processMessage(in, new TypeToken<List<Integer>>() {}.getType());
            List<Integer> foodItemIds = foodItemIdsWrapper.getMessage();

           // String discardResponse = chefController.discardItems(foodItemIds);
            messageProcessor.sendMessage(out, chefController.discardItems(foodItemIds));
        }
        if ("Send_Notification".equals(response)) {
            MessageProcessor.MessageWrapper<List<FoodItem>> foodItemsWrapper = messageProcessor.processMessage(in, new TypeToken<List<FoodItem>>() {}.getType());
            List<FoodItem> foodItems = foodItemsWrapper.getMessage();

           // String notificationResponse = notificationController.sendNotifications(foodItems);
            messageProcessor.sendMessage(out, notificationController.sendNotifications(foodItems));
        }
    }

}
