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

   /* public void insertFoodItemsToVotedItems(List<FoodItem> foodItems) throws SQLException {
        String sql = "INSERT INTO VotedItems (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 0, NOW(), false, false)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (FoodItem foodItem : foodItems) {
                stmt.setInt(1, foodItem.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }*/

    public void insertFoodItemsToVotedItems(List<FoodItem> foodItems) throws SQLException {
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
    public List<FoodItem> getFoodItemsVotedToday(String mealType) throws SQLException {
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


    public List<Integer> getPreparedFoodItemIdsFromYesterday() {
        List<Integer> foodItemIds = new ArrayList<>();
        try (
                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT FoodItemId FROM voteditems " +
                                "WHERE IsPrepared = 1 " +
                                "AND DATE(Date) = ?")) {

            // Calculate yesterday's date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            java.util.Date yesterday = calendar.getTime();

            // Convert yesterday's date to SQL date
            java.sql.Date sqlYesterday = new java.sql.Date(yesterday.getTime());

            stmt.setDate(1, sqlYesterday);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                foodItemIds.add(rs.getInt("FoodItemId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItemIds;
    }


    public List<FoodItem> getFoodItemsByIds(List<Integer> foodItemIds) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodItems;
    }


}



/*

public class VotedItemRepository extends GenericRepository<FoodItem> {

    public VotedItemRepository() throws SQLException {
        super();
    }

    public void insertFoodItemsToVotedItems(List<FoodItem> foodItems) throws SQLException {
        String query = "INSERT INTO VotedItems (FoodItemId, Vote, Date, IsPrepared, IsDelete) VALUES (?, 0, NOW(), false, false)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (FoodItem foodItem : foodItems) {
                stmt.setInt(1, foodItem.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void voteFoodItems(List<Integer> foodItemIds) throws SQLException {
        String query = "UPDATE VotedItems SET Vote = Vote + 1 WHERE FoodItemId = ? AND DATE(Date) = CURDATE()";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<FoodItem> getTopVotedFoodItems() throws SQLException {
        String query = "SELECT fi.*, SUM(vi.Vote) AS totalVotes " +
                "FROM VotedItems vi " +
                "JOIN FoodItems fi ON vi.FoodItemId = fi.Id " +
                "WHERE vi.IsDelete = false " +
                "GROUP BY vi.FoodItemId " +
                "ORDER BY totalVotes DESC LIMIT 3";
        return findAll(query, this::mapFoodItem);
    }

    public void markFoodItemsAsPrepared(List<Integer> foodItemIds) throws SQLException {
        String query = "UPDATE VotedItems SET IsPrepared = true WHERE FoodItemId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int foodItemId : foodItemIds) {
                stmt.setInt(1, foodItemId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<FoodItem> getFoodItemsVotedToday(String mealType) throws SQLException {
        // Step 1: Get the mealTypeId for the given mealType
        int mealTypeId = getMealTypeId(mealType);

        // Step 2: Get the FoodItems voted today based on the mealTypeId
        String query = "SELECT fi.* FROM fooditems fi " +
                "JOIN voteditems vi ON fi.Id = vi.FoodItemId " +
                "WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";
        return findAll(query, this::mapFoodItem, mealTypeId);
    }

    public List<RollOutFoodItemsDTO> getRollOutItem(String mealType) throws SQLException {
        // Step 1: Get the mealTypeId for the given mealType
        int mealTypeId = getMealTypeId(mealType);

        // Step 2: Get the FoodItems voted today based on the mealTypeId
        String query = "SELECT fi.Id, fi.MealTypeId, fi.Name, fi.Price, fi.IsAvailable, fi.IsDelete, vi.Vote " +
                "FROM fooditems fi " +
                "JOIN voteditems vi ON fi.Id = vi.FoodItemId " +
                "WHERE DATE(vi.Date) = CURDATE() AND fi.MealTypeId = ?";
        return findAll(query, this::mapRollOutFoodItem, mealTypeId);
    }

    private int getMealTypeId(String mealType) throws SQLException {
        String query = "SELECT Id FROM mealtypes WHERE Type = ? AND IsDelete = 0";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mealType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                } else {
                    throw new SQLException("Meal type not found");
                }
            }
        }
    }

    private FoodItem mapFoodItem(ResultSet rs) throws SQLException {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(rs.getInt("Id"));
        foodItem.setMealTypeId(rs.getInt("MealTypeId"));
        foodItem.setName(rs.getString("Name"));
        foodItem.setPrice(rs.getBigDecimal("Price"));
        foodItem.setAvailable(rs.getBoolean("IsAvailable"));
        foodItem.setDelete(rs.getBoolean("IsDelete"));
        return foodItem;
    }

    private RollOutFoodItemsDTO mapRollOutFoodItem(ResultSet rs) throws SQLException {
        RollOutFoodItemsDTO foodItem = new RollOutFoodItemsDTO();
        foodItem.setId(rs.getInt("Id"));
        foodItem.setMealTypeId(rs.getInt("MealTypeId"));
        foodItem.setName(rs.getString("Name"));
        foodItem.setPrice(rs.getBigDecimal("Price"));
        foodItem.setVote(rs.getInt("Vote"));
        foodItem.setAvailable(rs.getBoolean("IsAvailable"));
        foodItem.setDelete(rs.getBoolean("IsDelete"));
        return foodItem;
    }
}

*/



