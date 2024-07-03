package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;
import org.example.model.RollOutFoodItemsDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class VotedItemRepository {
    private Connection connection;

    public VotedItemRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }
    public void addVotedItems(List<FoodItem> foodItems) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM VotedItems WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";
        String insertSql = "INSERT INTO VotedItems (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 0, NOW(), false, false)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql);
             PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {

            for (FoodItem foodItem : foodItems) {
                checkStmt.setInt(1, foodItem.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        insertStmt.setInt(1, foodItem.getId());
                        insertStmt.addBatch();
                    }
                }
            }
            insertStmt.executeBatch();
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
    public List<FoodItem> getFoodItemsForVote(String mealType) throws SQLException {
        String getMealTypeIdSql = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";

        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(getMealTypeIdSql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    throw new SQLException("Meal type not found");
                }
            }
        }

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
        String getMealTypeIdSql = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";

        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(getMealTypeIdSql)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    throw new SQLException("Meal type not found");
                }
            }
        }

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


    public List<Integer> getPreparedFoodItemIds() throws SQLException  {
        List<Integer> foodItemIds = new ArrayList<>();
        try (
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT FoodItemId FROM voteditems " +
                                "WHERE IsPrepared = 1 " +
                                "AND DATE(Date) = ?")) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            java.util.Date yesterday = calendar.getTime();
            java.sql.Date sqlYesterday = new java.sql.Date(yesterday.getTime());

            stmt.setDate(1, sqlYesterday);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                foodItemIds.add(rs.getInt("FoodItemId"));
            }
        }
        return foodItemIds;
    }


    public List<FoodItem> getFoodItemsByIds(List<Integer> foodItemIds) throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        if (foodItemIds.isEmpty()) {
            return foodItems;
        }

        String idList = foodItemIds.toString().replace("[", "").replace("]", "");
        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT * FROM fooditems WHERE Id IN (" + idList + ") AND IsDelete = 0")) {

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




