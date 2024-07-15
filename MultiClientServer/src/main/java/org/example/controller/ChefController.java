package org.example.controller;

import com.google.gson.Gson;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.DTO.RecommendedDTO;
import org.example.DTO.RollOutFoodItemsDTO;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;
import org.example.service.RecommendationService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;


public class ChefController {
    private RecommendationService recommendationService;
    private FoodItemService foodItemService;
    private VotedItemService votedItemService;
    private NotificationService notificationService;

    private Gson gson = new Gson();
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
            return gson.toJson(foodItems);
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String rollOutFoodItems(List<Integer> foodItemIds) {
        try {
            votedItemService.rollOutFoodItems(foodItemIds);
            notificationService.sendNotification(foodItemIds,"RollOut Menu");
            return "Food item Roll Out successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
    public String getNotifications() {
        try {
            List<Notification> notifications = notificationService.getNotifications();
            return gson.toJson(notifications);
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String viewDiscardedItems() {
        try {
            List<FoodItem> foodItems = recommendationService.getDiscardedFoodItems();
            return gson.toJson(foodItems);
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String discardItems(List<Integer> foodItemIds) {
        try {
            foodItemService.discardFoodItem(foodItemIds);
           return "Food items discarded successfully";
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
