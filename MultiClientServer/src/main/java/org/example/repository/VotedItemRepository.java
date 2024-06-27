package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VotedItemRepository {
    private Connection connection;

    public VotedItemRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void insertFoodItemsToVotedItems(List<FoodItem> foodItems) throws SQLException {
        String sql = "INSERT INTO VotedItems (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 0, NOW(), false, false)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (FoodItem foodItem : foodItems) {
                stmt.setInt(1, foodItem.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void voteFoodItems(List<Integer> foodItemIds) throws SQLException {
        String sql = "INSERT INTO VotedItems (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 1, NOW(), false, false) " +
                "ON DUPLICATE KEY UPDATE Vote = Vote + 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<FoodItem> getTopVotedFoodItems() throws SQLException {
        String sql = "SELECT fi.*, SUM(vi.Vote) AS totalVotes " +
                "FROM VotedItems vi " +
                "JOIN FoodItems fi ON vi.FoodItemId = fi.Id " +
                "WHERE vi.IsDelete = false " +
                "GROUP BY vi.FoodItemId " +
                "ORDER BY totalVotes DESC " +
                "LIMIT 3";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<FoodItem> topFoodItems = new ArrayList<>();
            while (rs.next()) {
                FoodItem foodItem = new FoodItem();
                foodItem.setId(rs.getInt("Id"));
                foodItem.setMealTypeId(rs.getInt("MealTypeId"));
                foodItem.setName(rs.getString("Name"));
                foodItem.setPrice(rs.getBigDecimal("Price"));
                foodItem.setAvailable(rs.getBoolean("IsAvailable"));
                foodItem.setDelete(rs.getBoolean("IsDelete"));
                topFoodItems.add(foodItem);
            }
            return topFoodItems;
        }
    }

    public void markFoodItemsAsPrepared(List<Integer> foodItemIds) throws SQLException {
        String sql = "UPDATE VotedItems SET IsPrepared = true WHERE FoodItemId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }



}
