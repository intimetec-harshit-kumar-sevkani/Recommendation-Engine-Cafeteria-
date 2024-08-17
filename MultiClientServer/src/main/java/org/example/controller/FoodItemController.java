package org.example.controller;


import com.google.gson.Gson;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;

import java.sql.SQLException;
import java.util.List;

public class FoodItemController {
    private FoodItemService foodItemService;
    private NotificationService notificationService;

    private Gson gson = new Gson();

    public FoodItemController() throws SQLException {
        this.foodItemService = new FoodItemService();
        this.notificationService = new NotificationService();
    }

    public FoodItemController(FoodItemService foodItemService, NotificationService notificationService, Gson gson) throws SQLException{
        this.foodItemService = new FoodItemService();
        this.notificationService = new NotificationService();
        this.gson = new Gson();
    }

    public String addFoodItem(FoodItem foodItem) {
        try {
            foodItemService.addFoodItem(foodItem);
            return "Food item added successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateFoodItem(FoodItem foodItem) {
        try {
            foodItemService.updateFoodItem(foodItem);
            return "Food item updated successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String deleteFoodItem(int foodItemId) {
        try {
            foodItemService.deleteFoodItem(foodItemId);
            return "Food item deleted successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getAllFoodItems() {
        try {
            List<FoodItem> foodItems = foodItemService.getAllFoodItems();
            return gson.toJson(foodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
    public String getNotifications()  {
        try {
            List<Notification> notifications = notificationService.getNotifications();
            return gson.toJson(notifications);
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
