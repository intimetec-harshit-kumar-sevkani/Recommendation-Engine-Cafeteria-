package org.example.controller;

import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.model.RecommendedDTO;
import org.example.model.RollOutFoodItemsDTO;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;
import org.example.service.RecommendationService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;

import static org.example.util.JsonUtil.gson;

public class ChefController {
    private RecommendationService recommendationService;

    private FoodItemService foodItemService;

    private VotedItemService votedItemService;

    private NotificationService notificationService;

    public ChefController() throws SQLException {
        this.recommendationService = new RecommendationService();
        this.votedItemService = new VotedItemService();
        this.notificationService = new NotificationService();
        this.foodItemService = new FoodItemService();
    }

    public String getRecommendedFoodItems(RecommendedDTO recommendedDTO) {
        try {
            List<FoodItem> foodItems = recommendationService.getRecommendedFoodItems(recommendedDTO);
            return gson.toJson(foodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getVotedItems(String mealType)  {
        try {
            List<RollOutFoodItemsDTO> foodItems = votedItemService.viewVotedFoodItem(mealType);
            String foodItemJson = gson.toJson(foodItems);
            return foodItemJson;
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String rollOutFoodItems(List<Integer> FoodItems) {
        try {
            votedItemService.rollOutFoodItems(FoodItems);
            notificationService.sendNotification(FoodItems,"RollOut Menu");
            return "Food item Roll Out successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
    public String getNotification() {
        try {
            List<Notification> notifications = notificationService.getNotification();
            String notificationJson = gson.toJson(notifications);
            return notificationJson;
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String ViewDiscardItem() {
        try {
            List<FoodItem> foodItems = recommendationService.getDiscardedFoodItems();
            String discardItemJson = gson.toJson(foodItems);
            return discardItemJson;
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String DiscardItem(List<Integer> foodItemIds) {
        try {
            foodItemService.discardFoodItem(foodItemIds);
           return "Food items discarded successfully";
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
