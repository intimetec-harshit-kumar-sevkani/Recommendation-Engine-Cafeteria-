package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Feedback;
import org.example.DTO.FeedbackDTO;
import org.example.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackRepository {

    private Connection connection;

    public FeedbackRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

  /*  public List<FoodItemRating> getFoodItemRatings(int foodItemId) throws SQLException {
        List<FoodItemRating> foodItemRatings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_FOOD_ITEM_RATINGS)) {
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
    }*/

    public FeedbackDTO getLastFoodItemFeedback(int foodItemId) throws SQLException {
        FeedbackDTO feedbackDTO = null;
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.GET_LAST_FOOD_ITEM_FEEDBACK)) {
            stmt.setInt(1, foodItemId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    feedbackDTO = new FeedbackDTO();
                    feedbackDTO.setFoodItemId(rs.getInt("FoodItemId"));
                    feedbackDTO.setLastRating(rs.getDouble("last_rating"));
                    feedbackDTO.setLastComment(rs.getString("last_comment"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while fetching the last feedback for the food item", e);
        }

        return feedbackDTO;
    }


   /* public void updateItemAudit(FoodItemRating foodItemRating, double averageSentiment) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.UPDATE_ITEM_AUDIT)) {
            stmt.setDouble(1, foodItemRating.getAverageRating());
            stmt.setDouble(2, averageSentiment);
            stmt.setInt(3, foodItemRating.getFoodItemId());
            int rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while updating item audit", e);
        }
    }*/

    public void updateItemAudit(FeedbackDTO feedbackDTO, double averageSentiment) throws SQLException {
        String selectQuery = "SELECT Rating, Vote, Sentiment FROM food_item_audit WHERE FoodItemId = ?";
        String updateQuery = "UPDATE food_item_audit SET Rating = ?, Sentiment = ?, Vote = ? WHERE FoodItemId = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
            selectStmt.setInt(1, feedbackDTO.getFoodItemId());

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    double currentRating = rs.getDouble("Rating");
                    int currentVote = rs.getInt("Vote");
                    double currentSentiment = rs.getDouble("Sentiment");

                    double newRating = ((currentRating * currentVote) + feedbackDTO.getLastRating()) / (currentVote + 1);
                    double newSentiment = ((currentSentiment * currentVote) + averageSentiment) / (currentVote + 1);
                    int newVote = currentVote + 1;

                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setDouble(1, newRating);
                        updateStmt.setDouble(2, newSentiment);
                        updateStmt.setInt(3, newVote);
                        updateStmt.setInt(4, feedbackDTO.getFoodItemId());
                        int rowsAffected = updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while updating item audit", e);
        }
    }


    public void addFeedback(Feedback feedback) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.ADD_FEEDBACK)) {
            stmt.setInt(1, feedback.getFoodItemId());
            stmt.setInt(2, feedback.getUserId());
            stmt.setDouble(3, feedback.getRating());
            stmt.setString(4, feedback.getComment());
            stmt.setBoolean(5, feedback.isDelete());
            stmt.executeUpdate();
        }
    }
}

