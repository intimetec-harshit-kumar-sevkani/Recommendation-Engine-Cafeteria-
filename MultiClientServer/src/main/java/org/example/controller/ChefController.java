package org.example.controller;

import org.example.model.FoodItem;
import org.example.service.FoodItemService;
import org.example.service.RecommendationService;

import java.sql.SQLException;
import java.util.List;

import static org.example.util.JsonUtil.gson;

public class ChefController {
    private RecommendationService recommendationService;

    public ChefController() throws SQLException {
        this.recommendationService = new RecommendationService();
    }

    public String getTopFoodItems() {
        try {
            List<FoodItem> foodItems = recommendationService.getAllFoodItems();
            return gson.toJson(foodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }


}
