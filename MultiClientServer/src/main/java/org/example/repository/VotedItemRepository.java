package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;
import org.example.DTO.RollOutFoodItemsDTO;
import org.example.util.SQLQueries;

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
        try (PreparedStatement checkStmt = connection.prepareStatement(SQLQueries.CHECK_VOTED_ITEM);
             PreparedStatement insertStmt = connection.prepareStatement(SQLQueries.INSERT_VOTED_ITEM)) {

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
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.UPDATE_VOTE)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

   /* public void markFoodItemsAsPrepared(List<Integer> foodItemIds) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.MARK_AS_PREPARED)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }*/

    public void markFoodItemsAsPrepared(List<Integer> foodItemIds) throws SQLException {

            try (PreparedStatement markPreparedStmt = connection.prepareStatement(SQLQueries.MARK_AS_PREPARED);
                 PreparedStatement incrementPreparedStmt = connection.prepareStatement(SQLQueries.INCREMENT_PREPARED_COUNT)) {

                // Mark food items as prepared
                for (int foodItemId : foodItemIds) {
                    markPreparedStmt.setInt(1, foodItemId);
                    markPreparedStmt.addBatch();
                }
                markPreparedStmt.executeBatch();

                // Increment the prepared count
                for (int foodItemId : foodItemIds) {
                    incrementPreparedStmt.setInt(1, foodItemId);
                    incrementPreparedStmt.addBatch();
                }
                incrementPreparedStmt.executeBatch();
            }

    }

    public List<FoodItem> getFoodItemsForVote(String mealType) throws SQLException {
        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_MEAL_TYPE_ID)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    throw new SQLException("Meal type not found");
                }
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_FOOD_ITEMS_FOR_VOTE)) {
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
                    foodItem.setFoodType(rs.getString("FoodType"));
                    foodItem.setSpiceLevel(rs.getString("SpiceLevel"));
                    foodItem.setOriginality(rs.getString("Originality"));
                    foodItem.setSweetTooth(rs.getBoolean("SweetTooth"));
                    foodItems.add(foodItem);
                }
                return foodItems;
            }
        }
    }

    public List<RollOutFoodItemsDTO> getRollOutItem(String mealType) throws SQLException {
        int mealTypeId;
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_MEAL_TYPE_ID)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mealTypeId = rs.getInt("Id");
                } else {
                    throw new SQLException("Meal type not found");
                }
            }
        }

        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_ROLL_OUT_ITEMS)) {
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

    public List<Integer> getPreparedFoodItemIds() throws SQLException {
        List<Integer> foodItemIds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_PREPARED_FOOD_ITEM_IDS)) {
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
        String query = String.format(SQLQueries.GET_FOOD_ITEMS_BY_IDS_TEMPLATE, idList);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                FoodItem foodItem = new FoodItem();
                foodItem.setId(rs.getInt("Id"));
                foodItem.setMealTypeId(rs.getInt("MealTypeId"));
                foodItem.setName(rs.getString("Name"));
                foodItem.setPrice(rs.getBigDecimal("Price"));
                foodItem.setAvailable(rs.getBoolean("IsAvailable"));
                foodItem.setDelete(rs.getBoolean("IsDelete"));
                foodItem.setFoodType(rs.getString("FoodType"));
                foodItem.setSpiceLevel(rs.getString("SpiceLevel"));
                foodItem.setOriginality(rs.getString("Originality"));
                foodItem.setSweetTooth(rs.getBoolean("SweetTooth"));
                foodItems.add(foodItem);
            }
        }
        return foodItems;
    }
}

