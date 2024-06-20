package org.example.service;

// AdminService.java

import java.sql.SQLException;
import java.util.List;
import org.example.model.FoodItem;
import org.example.repository.FoodItemRepository;

public class FoodItemService {
    private FoodItemRepository foodItemRepository;

    public FoodItemService() throws SQLException {
        this.foodItemRepository = new FoodItemRepository();
    }

    public void addFoodItem(FoodItem foodItem) throws SQLException {
        foodItemRepository.addFoodItem(foodItem);
    }

    public void updateFoodItem(FoodItem foodItem) throws SQLException {
        foodItemRepository.updateFoodItem(foodItem);
    }

    public void deleteFoodItem(int id) throws SQLException {
        foodItemRepository.deleteFoodItem(id);
    }

    public List<FoodItem> getAllFoodItems() throws SQLException {
        return foodItemRepository.getAllFoodItems();
    }
}
