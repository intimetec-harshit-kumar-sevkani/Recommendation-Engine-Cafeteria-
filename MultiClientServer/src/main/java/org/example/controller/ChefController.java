package org.example.controller;

import org.example.model.FoodItem;
import org.example.model.RecommendedDTO;
import org.example.model.RollOutFoodItemsDTO;
import org.example.service.NotificationService;
import org.example.service.RecommendationService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;

import static org.example.util.JsonUtil.gson;

public class ChefController {
    private RecommendationService recommendationService;

    private VotedItemService votedItemService;

    private NotificationService notificationService;

    public ChefController() throws SQLException {
        this.recommendationService = new RecommendationService();
        this.votedItemService = new VotedItemService();
        this.notificationService = new NotificationService();
    }

    public String getTopFoodItems(RecommendedDTO recommendedDTO) {
        try {
            List<FoodItem> foodItems = recommendationService.getAllFoodItems(recommendedDTO);
            return gson.toJson(foodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<RollOutFoodItemsDTO> getRollOutFoodItemsDTOList(String mealType) throws SQLException {
        List<RollOutFoodItemsDTO> foodItems = votedItemService.viewRollOutFoodItem(mealType);
        return foodItems;
    }

    public String rollOutItems(List<Integer> votedFoodItems) {
        try {
            votedItemService.rollOutFoodItems(votedFoodItems);
            notificationService.sendNotification(votedFoodItems,"RollOut Menu");
            return "Food item Roll Out successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
