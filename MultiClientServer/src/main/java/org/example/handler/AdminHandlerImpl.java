package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.FoodItemController;
import org.example.model.FoodItem;
import org.example.model.MessageType;
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
    private final MessageProcessor messageProcessor;

    public AdminHandlerImpl() throws SQLException {
        this.foodItemController = new FoodItemController();
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
            default:
                System.out.println("--------------");
        }
    }


   /* public void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String foodItemJson = in.readLine();

        FoodItem foodItem = gson.fromJson(foodItemJson, FoodItem.class);
        String response = foodItemController.addFoodItem(foodItem);
        out.println(response);
    }*/

    public void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        //String foodItemJson = in.readLine();
        MessageProcessor.MessageWrapper<FoodItem> wrapper = messageProcessor.processMessage(in, FoodItem.class);
        FoodItem foodItem = wrapper.getMessage();
        String response = foodItemController.addFoodItem(foodItem);
        out.println(response);
    }

    public void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String payload = in.readLine();
        FoodItem updatedFoodItem = gson.fromJson(payload, FoodItem.class);

        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        if (validFoodItemIds.contains(updatedFoodItem.getId())) {
            String response = foodItemController.updateFoodItem(updatedFoodItem);
            out.println(response);
        } else {
            out.println("Invalid Food Item Id.");
        }
    }

    public void handleDeleteFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        int id = Integer.parseInt(in.readLine());

        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        if (validFoodItemIds.contains(id)) {
            String response = foodItemController.deleteFoodItem(id);
            out.println(response);
        } else {
            out.println("Invalid Food Item Id.");
        }
    }


    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException {
        String notificationJson = foodItemController.getNotification();
        out.println(notificationJson);
    }
}
