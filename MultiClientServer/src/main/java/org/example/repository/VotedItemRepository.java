package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;
import org.example.model.RollOutFoodItemsDTO;

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
        String sql = "UPDATE VotedItems " +
                "SET Vote = Vote + 1 " +
                "WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
     /*   String updateSql = "UPDATE VotedItems " +
                "SET Vote = Vote + 1 " +
                "WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";*/
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

    /*public List<FoodItem> getFoodItemsVotedToday(String mealType) throws SQLException {
        // Step 1: Get the mealTypeId for the given mealType
        String getMealTypeIdSql = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";

        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(getMealTypeIdSql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    // Handle case where the meal type does not exist
                    throw new SQLException("Meal type not found");
                }
            }
        }

        // Step 2: Get the FoodItems voted today based on the mealTypeId
        String getFoodItemsSql = "SELECT fi.* " +
                "FROM fooditems fi " +
                "JOIN voteditems vi ON fi.Id = vi.FoodItemId " +
                "WHERE vi.Date = CURDATE() AND fi.MealTypeId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getFoodItemsSql)) {
            stmt.setInt(1, mealTypeId);

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
    }*/

    public List<FoodItem> getFoodItemsVotedToday(String mealType) throws SQLException {
        // Step 1: Get the mealTypeId for the given mealType
        String getMealTypeIdSql = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";

        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(getMealTypeIdSql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    // Handle case where the meal type does not exist
                    throw new SQLException("Meal type not found");
                }
            }
        }

        // Step 2: Get the FoodItems voted today based on the mealTypeId
        String getFoodItemsSql = "SELECT fi.* " +
                "FROM fooditems fi " +
                "JOIN voteditems vi ON fi.Id = vi.FoodItemId " +
                "WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getFoodItemsSql)) {
            stmt.setInt(1, mealTypeId);

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

    public List<RollOutFoodItemsDTO> getRollOutItem(String mealType) throws SQLException {
        // Step 1: Get the mealTypeId for the given mealType
        String getMealTypeIdSql = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";

        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(getMealTypeIdSql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    // Handle case where the meal type does not exist
                    throw new SQLException("Meal type not found");
                }
            }
        }

        // Step 2: Get the FoodItems voted today based on the mealTypeId
        String getFoodItemsSql = "SELECT fi.Id, fi.MealTypeId, fi.Name, fi.Price, fi.IsAvailable, fi.IsDelete, vi.Vote " +
                "FROM fooditems fi " +
                "JOIN voteditems vi ON fi.Id = vi.FoodItemId " +
                "WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(getFoodItemsSql)) {
            stmt.setInt(1, mealTypeId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<RollOutFoodItemsDTO> foodItems = new ArrayList<>();
                while (rs.next()) {
                    RollOutFoodItemsDTO foodItem = new RollOutFoodItemsDTO();
                    foodItem.setId(rs.getInt("Id"));
                    foodItem.setMealTypeId(rs.getInt("MealTypeId"));
                    foodItem.setName(rs.getString("Name"));
                    foodItem.setPrice(rs.getBigDecimal("Price"));
                    foodItem.setVote(rs.getInt("Vote"));
                    foodItem.setAvailable(rs.getBoolean("IsAvailable"));
                    foodItem.setDelete(rs.getBoolean("IsDelete"));
                    foodItems.add(foodItem);
                }
                return foodItems;
            }
        }
    }


}
