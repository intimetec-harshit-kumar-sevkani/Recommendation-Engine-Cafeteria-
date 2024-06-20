package org.example.repository;

// AdminRepository.java

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;

public class FoodItemRepository {
    private Connection connection;

    public FoodItemRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void addFoodItem(FoodItem foodItem) throws SQLException {
        String sql = "INSERT INTO FoodItems (MealTypeId, Name, Price, IsAvailable, IsDelete) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, foodItem.getMealTypeId());
            stmt.setString(2, foodItem.getName());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setBoolean(4, foodItem.isAvailable());
            stmt.setBoolean(5, foodItem.isDelete());
            stmt.executeUpdate();
        }
    }

    public void updateFoodItem(FoodItem foodItem) throws SQLException {
        String sql = "UPDATE FoodItems SET MealTypeId = ?, Name = ?, Price = ?, IsAvailable = ?, IsDelete = ? WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, foodItem.getMealTypeId());
            stmt.setString(2, foodItem.getName());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setBoolean(4, foodItem.isAvailable());
            stmt.setBoolean(5, foodItem.isDelete());
            stmt.setInt(6, foodItem.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteFoodItem(int id) throws SQLException {
        String sql = "UPDATE FoodItems SET IsDelete = true WHERE Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<FoodItem> getAllFoodItems() throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM FoodItems";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FoodItem foodItem = new FoodItem();
                foodItem.setId(rs.getInt("Id"));
                foodItem.setMealTypeId(rs.getInt("MealTypeId"));
                foodItem.setName(rs.getString("Name"));
                foodItem.setPrice(rs.getBigDecimal("Price"));
                foodItem.setAvailable(rs.getBoolean("IsAvailable"));
                foodItem.setDelete(rs.getBoolean("IsDelete"));
                foodItems.add(foodItem);
            }
        }
        return foodItems;
    }
}
