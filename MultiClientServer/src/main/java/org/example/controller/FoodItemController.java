package org.example.controller;

// AdminController.java

import org.example.model.FoodItem;
import org.example.service.FoodItemService;
import org.example.util.JsonUtil;


import java.io.*;
import java.sql.SQLException;
import java.util.List;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.example.util.JsonUtil.gson;

public class FoodItemController {
    private FoodItemService foodItemService;

    public FoodItemController() throws SQLException {
        this.foodItemService = new FoodItemService();
    }

    public String addFoodItem(FoodItem newFoodItem) {
        try {
            foodItemService.addFoodItem(newFoodItem);
            return "Food item added successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateFoodItem(FoodItem updatedFoodItem) {
        try {
            foodItemService.updateFoodItem(updatedFoodItem);
            return "Food item updated successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String deleteFoodItem(int idToDelete) {
        try {
            foodItemService.deleteFoodItem(idToDelete);
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
}
