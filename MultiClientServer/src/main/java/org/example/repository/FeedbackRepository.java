package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Feedback;
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
    public List<FoodItemRating> getFoodItemRatings(int foodItemId) throws SQLException {
        String sql = "SELECT FoodItemId, AVG(Rating) AS average_rating, GROUP_CONCAT(Comment SEPARATOR ', ') AS comments " +
                "FROM feedbacks " +
                "WHERE Date = CURDATE() AND FoodItemId = ? " +
                "GROUP BY FoodItemId";
        List<FoodItemRating> foodItemRatings = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, foodItemId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FoodItemRating foodItemRating = new FoodItemRating();
                    foodItemRating.setFoodItemId(rs.getInt("FoodItemId"));
                    foodItemRating.setAverageRating(rs.getDouble("average_rating"));
                    foodItemRating.setComments(rs.getString("comments"));
                    foodItemRatings.add(foodItemRating);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while fetching food item ratings for today", e);
        }

        return foodItemRatings;
    }



    public void updateItemAudit(FoodItemRating foodItemRating, double averageSentiment) throws SQLException {
        String sql = "UPDATE fooditemaudit SET Rating = ?, Sentiment = ? WHERE FoodItemId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, foodItemRating.getAverageRating());
            stmt.setDouble(2, averageSentiment);
            stmt.setInt(3, foodItemRating.getFoodItemId());
            int rowsAffected = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while updating item audit", e);
        }
    }
    public void addFeedback(Feedback feedback) throws SQLException {
        String sql = "INSERT INTO Feedbacks (FoodItemId, UserId, Rating, Comment , Date ,IsDelete) VALUES (?, ?, ?, ?, CURDATE(), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, feedback.getFoodItemId());
            stmt.setInt(2, feedback.getUserId());
            stmt.setDouble(3, feedback.getRating());
            stmt.setString(4, feedback.getComment());
            stmt.setBoolean(5, feedback.isDelete());
            stmt.executeUpdate();
        }
    }

}
