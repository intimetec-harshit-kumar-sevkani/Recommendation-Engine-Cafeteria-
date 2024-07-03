package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.FoodItemController;
import org.example.model.FoodItem;
import org.example.model.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminHandlerImpl implements AdminHandler{

    private FoodItemController foodItemController;

    public AdminHandlerImpl() throws SQLException {
        this.foodItemController = new FoodItemController();
    }

    private Gson gson = new Gson();
    public void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String foodItemJson = in.readLine();
        FoodItem newFoodItem = gson.fromJson(foodItemJson, FoodItem.class);
        foodItemController.addFoodItem(newFoodItem);
        out.println("Food item added successfully.");
    }

    public void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String payload = in.readLine();
        FoodItem updatedFoodItem = gson.fromJson(payload, FoodItem.class);

        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        if (validFoodItemIds.contains(updatedFoodItem.getId())) {
            foodItemController.updateFoodItem(updatedFoodItem);
            out.println("Food item updated successfully.");
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
            foodItemController.deleteFoodItem(id);
            out.println("Food item deleted successfully.");
        } else {
            out.println("Invalid Food Item Id.");
        }
    }


    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        List<Notification> notifications = foodItemController.getNotification();
        String notificationJson = gson.toJson(notifications);
        out.println(notificationJson);
    }
}
