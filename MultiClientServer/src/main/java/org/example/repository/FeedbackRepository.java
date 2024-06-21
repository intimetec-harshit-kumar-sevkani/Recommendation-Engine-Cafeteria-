package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItemRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    private Connection connection;

    public FeedbackRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public List<FoodItemRating> getFoodItemRatingsForToday() throws SQLException {
        String sql = "SELECT food_item_id, AVG(rating) AS average_rating, GROUP_CONCAT(comment SEPARATOR ', ') AS comments " +
                "FROM rating " +
                "WHERE rating_date = CURDATE() " +
                "GROUP BY food_item_id";
        List<FoodItemRating> foodItemRatings = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                FoodItemRating foodItemRating = new FoodItemRating();
                foodItemRating.setFoodItemId(((ResultSet) rs).getInt("food_item_id"));
                foodItemRating.setAverageRating(rs.getDouble("average_rating"));
                foodItemRating.setComments(rs.getString("comments"));
                foodItemRatings.add(foodItemRating);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while fetching food item ratings for today", e);
        }

        return foodItemRatings;
    }


    public void updateItemAudit(FoodItemRating foodItemRating, double averageSentiment) throws SQLException {
        String sql = "UPDATE item_audit SET average_rating = ?, average_sentiment = ? WHERE food_item_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, foodItemRating.getAverageRating());
            stmt.setDouble(2, averageSentiment);
            stmt.setInt(3, foodItemRating.getFoodItemId());
            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while updating item audit", e);
        }
    }


}
