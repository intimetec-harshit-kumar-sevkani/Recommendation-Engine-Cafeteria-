package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.FoodItemRating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    private Connection connection;

    public FeedbackRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public List<FoodItemRating> getFoodItemRatingsForToday() throws SQLException {
        String sql = "SELECT FoodItemId, AVG(Rating) AS average_rating, GROUP_CONCAT(Comment SEPARATOR ', ') AS comments " +
                "FROM Feedbacks " +
                "WHERE Date = CURDATE() " +
                "GROUP BY FoodItemId";
        List<FoodItemRating> foodItemRatings = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                FoodItemRating foodItemRating = new FoodItemRating();
                foodItemRating.setFoodItemId(((ResultSet) rs).getInt("FoodItemId"));
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
        String sql = "UPDATE FoodItemAudit SET Rating = ?, Sentiment = ? WHERE FoodItemId = ?";

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

/*

public class FeedbackRepository extends GenericRepository<FoodItemRating> {

    public FeedbackRepository() throws SQLException {
        super();
    }

    public List<FoodItemRating> getFoodItemRatingsForToday() throws SQLException {
        String query = "SELECT FoodItemId, AVG(Rating) AS average_rating, GROUP_CONCAT(Comment SEPARATOR ', ') AS comments " +
                "FROM Feedbacks WHERE Date = CURDATE() GROUP BY FoodItemId";
        return findAll(query, this::mapFoodItemRating);
    }

    public void updateItemAudit(FoodItemRating foodItemRating, double averageSentiment) throws SQLException {
        String query = "UPDATE FoodItemAudit SET Rating = ?, Sentiment = ? WHERE FoodItemId = ?";
        update(query, foodItemRating.getAverageRating(), averageSentiment, foodItemRating.getFoodItemId());
    }

    public void addFeedback(Feedback feedback) throws SQLException {
        String query = "INSERT INTO Feedbacks (FoodItemId, UserId, Rating, Comment , Date ,IsDelete) VALUES (?, ?, ?, ?, CURDATE(), ?)";
        insert(query, feedback.getFoodItemId(), feedback.getUserId(), feedback.getRating(), feedback.getComment(), feedback.isDelete());
    }

    private FoodItemRating mapFoodItemRating(ResultSet rs) throws SQLException {
        FoodItemRating foodItemRating = new FoodItemRating();
        foodItemRating.setFoodItemId(rs.getInt("FoodItemId"));
        foodItemRating.setAverageRating(rs.getDouble("average_rating"));
        foodItemRating.setComments(rs.getString("comments"));
        return foodItemRating;
    }
}

*/
