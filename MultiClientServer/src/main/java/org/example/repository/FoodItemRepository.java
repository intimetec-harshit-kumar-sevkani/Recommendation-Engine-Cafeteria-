package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;

import java.sql.*;
import java.time.LocalDate;
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

    public List<FoodItem> getRecommendedFoodItems(String mealType, int numberOfItems) throws SQLException {
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

    // getDiscardedFoodItems

   /* public List<Integer> getDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = new ArrayList<>();
        String selectSql = "SELECT FoodItemId FROM FoodItemAudit WHERE Rating <= 2";
        String insertSql = "INSERT INTO discarditem (foodItemId, date) VALUES (?, ?)";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement insertStmt = connection.prepareStatement(insertSql);
             ResultSet rs = selectStmt.executeQuery()) {

            while (rs.next()) {
                int foodItemId = rs.getInt("FoodItemId");
                foodItemIds.add(foodItemId);

                insertStmt.setInt(1, foodItemId);
                insertStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
        }

        return foodItemIds;
    }*/

    public List<FoodItem> getById(List<Integer> foodItemIds) throws SQLException {
        if (foodItemIds == null || foodItemIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<FoodItem> foodItems = new ArrayList<>();
        String sql = "SELECT * FROM foodItems WHERE Id IN (" +
                String.join(",", foodItemIds.stream().map(id -> "?").toArray(String[]::new)) +
                ")";

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

                    foodItems.add(foodItem);
                }
            }
        }

        return foodItems;

    }


    public List<Integer> getDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = new ArrayList<>();

        // Check for discarded items within the past month
        String checkSql = "SELECT foodItemId FROM discarditem WHERE date >= ?";
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setDate(1, java.sql.Date.valueOf(oneMonthAgo));

            try (ResultSet rs = checkStmt.executeQuery()) {
                while (rs.next()) {
                    foodItemIds.add(rs.getInt("foodItemId"));
                }
            }
        }

        // If no discarded items found within the past month, fetch and insert new ones
        if (foodItemIds.isEmpty()) {
            insertDiscardedItems(foodItemIds);
        }

        return foodItemIds;
    }

    private void insertDiscardedItems(List<Integer> foodItemIds) throws SQLException {
        String insertSql = "INSERT INTO discarditem (foodItemId, date) VALUES (?, ?)";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            for (int foodItemId : foodItemIds) {
                insertStmt.setInt(1, foodItemId);
                insertStmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
        }
    }


    public void discardFoodItems(List<Integer> foodItemIds) {
        String deleteSql = "DELETE FROM foodItems WHERE Id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
            for (int foodItemId : foodItemIds) {
                deleteStmt.setInt(1, foodItemId);
                deleteStmt.addBatch();
            }
            deleteStmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
