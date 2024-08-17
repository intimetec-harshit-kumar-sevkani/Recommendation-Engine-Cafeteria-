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

    public void updateItemAudit(FeedbackDTO feedbackDTO, double averageSentiment) throws SQLException {

        try (PreparedStatement selectStmt = connection.prepareStatement(SQLQueries.SELECT_FOOD_ITEM_AUDIT)) {
            selectStmt.setInt(1, feedbackDTO.getFoodItemId());

            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    double currentRating = rs.getDouble("Rating");
                    int currentVote = rs.getInt("Vote");
                    double currentSentiment = rs.getDouble("Sentiment");

                    double newRating = ((currentRating * currentVote) + feedbackDTO.getLastRating()) / (currentVote + 1);
                    double newSentiment = ((currentSentiment * currentVote) + averageSentiment) / (currentVote + 1);
                    int newVote = currentVote + 1;

                    try (PreparedStatement updateStmt = connection.prepareStatement(SQLQueries.UPDATE_FOOD_ITEM_AUDIT)) {
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

