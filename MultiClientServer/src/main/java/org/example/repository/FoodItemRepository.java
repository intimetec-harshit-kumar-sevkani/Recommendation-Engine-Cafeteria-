package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemRepository {
    private Connection connection;

    public FoodItemRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

   public int addFoodItem(FoodItem foodItem) throws SQLException {
       String sql = "INSERT INTO FoodItems (MealTypeId, Name, Price, IsAvailable, IsDelete) VALUES (?, ?, ?, ?, ?)";
       try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           stmt.setInt(1, foodItem.getMealTypeId());
           stmt.setString(2, foodItem.getName());
           stmt.setBigDecimal(3, foodItem.getPrice());
           stmt.setBoolean(4, foodItem.isAvailable());
           stmt.setBoolean(5, foodItem.isDelete());
           stmt.executeUpdate();

           try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                   return generatedKeys.getInt(1);
               } else {
                   throw new SQLException("Creating food item failed, no ID obtained.");
               }
           }
       }
   }

    public void updateFoodItem(FoodItem foodItem) throws SQLException {
        String sql = "UPDATE fooditems SET MealTypeId = ?, Name = ?, Price = ?, IsAvailable = ?, IsDelete = ? WHERE Id = ?";
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
        String sql = "DELETE FROM fooditems WHERE Id = ?";
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

    public List<FoodItem> getTopFoodItems(String mealType, int numberOfItems) throws SQLException {
        String sql = "SELECT fi.* " +
                "FROM FoodItemAudit fia " +
                "JOIN FoodItems fi ON fia.FoodItemId = fi.Id " +
                "JOIN MealTypes mt ON fi.MealTypeId = mt.Id " +
                "WHERE mt.Type = ? " +
                "ORDER BY (fia.Rating + fia.Sentiment) / 2 DESC " +
                "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mealType);
            stmt.setInt(2, numberOfItems);
            try (ResultSet rs = stmt.executeQuery()) {
                List<FoodItem> foodItems = new ArrayList<>();
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
                return foodItems;
            }
        }
    }


}
