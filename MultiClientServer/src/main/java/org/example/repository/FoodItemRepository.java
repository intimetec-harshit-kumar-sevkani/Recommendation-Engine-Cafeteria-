package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;
import org.example.util.SQLQueries;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodItemRepository {
    private Connection connection;

    public FoodItemRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void deleteFoodItem(int id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.DELETE_FOOD_ITEM)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int addFoodItem(FoodItem foodItem) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INSERT_FOOD_ITEM, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, foodItem.getMealTypeId());
            stmt.setString(2, foodItem.getName());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setBoolean(4, foodItem.isAvailable());
            stmt.setBoolean(5, foodItem.isDelete());
            stmt.setString(6, foodItem.getFoodType());
            stmt.setString(7, foodItem.getSpiceLevel());
            stmt.setString(8, foodItem.getOriginality());
            stmt.setBoolean(9, foodItem.isSweetTooth());
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
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.UPDATE_FOOD_ITEM)) {
            stmt.setInt(1, foodItem.getMealTypeId());
            stmt.setString(2, foodItem.getName());
            stmt.setBigDecimal(3, foodItem.getPrice());
            stmt.setBoolean(4, foodItem.isAvailable());
            stmt.setBoolean(5, foodItem.isDelete());
            stmt.setString(6, foodItem.getFoodType());
            stmt.setString(7, foodItem.getSpiceLevel());
            stmt.setString(8, foodItem.getOriginality());
            stmt.setBoolean(9, foodItem.isSweetTooth());
            stmt.setInt(10, foodItem.getId());
            stmt.executeUpdate();
        }
    }

    public List<FoodItem> getAllFoodItems() throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SQLQueries.SELECT_ALL_FOOD_ITEMS)) {
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

    public List<FoodItem> getRecommendedFoodItems(String mealType, int numberOfItems) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.SELECT_RECOMMENDED_FOOD_ITEMS)) {
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

    public List<FoodItem> getById(List<Integer> foodItemIds) throws SQLException {
        if (foodItemIds == null || foodItemIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<FoodItem> foodItems = new ArrayList<>();
        String sql = String.format(SQLQueries.SELECT_FOOD_ITEMS_BY_IDS, String.join(",", foodItemIds.stream().map(id -> "?").toArray(String[]::new)));

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < foodItemIds.size(); i++) {
                stmt.setInt(i + 1, foodItemIds.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
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
        }

        return foodItems;
    }

    public List<Integer> getDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = new ArrayList<>();
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        try (PreparedStatement checkStmt = connection.prepareStatement(SQLQueries.SELECT_DISCARDED_FOOD_ITEMS)) {
            checkStmt.setDate(1, java.sql.Date.valueOf(oneMonthAgo));

            try (ResultSet rs = checkStmt.executeQuery()) {
                while (rs.next()) {
                    foodItemIds.add(rs.getInt("foodItemId"));
                }
            }
        }

        if (foodItemIds.isEmpty()) {
            foodItemIds = getNewDiscardedFoodItems();
            insertDiscardedItems(foodItemIds);
        }

        return foodItemIds;
    }

    private void insertDiscardedItems(List<Integer> foodItemIds) throws SQLException {
        try (PreparedStatement insertStmt = connection.prepareStatement(SQLQueries.INSERT_DISCARDED_ITEMS)) {
            for (int foodItemId : foodItemIds) {
                insertStmt.setInt(1, foodItemId);
                insertStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }
    }

    public void discardFoodItems(List<Integer> foodItemIds) {
        try (PreparedStatement deleteStmt = connection.prepareStatement(SQLQueries.DELETE_FOOD_ITEMS)) {
            for (int foodItemId : foodItemIds) {
                deleteStmt.setInt(1, foodItemId);
                deleteStmt.addBatch();
            }
            deleteStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getNewDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SQLQueries.FETCH_DISCARDED_ITEMS);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                foodItemIds.add(rs.getInt("FoodItemId"));
            }
        }
        return foodItemIds;
    }
}

