package repository;

import org.example.DTO.FeedbackDTO;
import org.example.model.Feedback;
import org.example.repository.FeedbackRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FeedbackRepositoryTest {

    private FeedbackRepository feedbackRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        feedbackRepository = new FeedbackRepository();
        setField(feedbackRepository, "connection", connection);
    }

    @Test
    public void testGetLastFoodItemFeedback_Found() throws SQLException {
        int foodItemId = 1;
        when(connection.prepareStatement(SQLQueries.GET_LAST_FOOD_ITEM_FEEDBACK)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("FoodItemId")).thenReturn(foodItemId);
        when(resultSet.getDouble("last_rating")).thenReturn(4.5);
        when(resultSet.getString("last_comment")).thenReturn("Great taste!");

        FeedbackDTO feedbackDTO = feedbackRepository.getLastFoodItemFeedback(foodItemId);

        assertNotNull(feedbackDTO);
        assertEquals(foodItemId, feedbackDTO.getFoodItemId());
        assertEquals(4.5, feedbackDTO.getLastRating(), 0.0);
        assertEquals("Great taste!", feedbackDTO.getLastComment());

        verify(connection, times(1)).prepareStatement(SQLQueries.GET_LAST_FOOD_ITEM_FEEDBACK);
        verify(preparedStatement, times(1)).setInt(1, foodItemId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
    }

    @Test
    public void testGetLastFoodItemFeedback_NotFound() throws SQLException {
        int foodItemId = 1;
        when(connection.prepareStatement(SQLQueries.GET_LAST_FOOD_ITEM_FEEDBACK)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        FeedbackDTO feedbackDTO = feedbackRepository.getLastFoodItemFeedback(foodItemId);

        assertNull(feedbackDTO);

        verify(connection, times(1)).prepareStatement(SQLQueries.GET_LAST_FOOD_ITEM_FEEDBACK);
        verify(preparedStatement, times(1)).setInt(1, foodItemId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
    }

    @Test
    public void testUpdateItemAudit_UpdateSuccessful() throws SQLException {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFoodItemId(1);
        feedbackDTO.setLastRating(4.5);

        double averageSentiment = 3.5;

        when(connection.prepareStatement(SQLQueries.SELECT_FOOD_ITEM_AUDIT)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getDouble("Rating")).thenReturn(4.0);
        when(resultSet.getInt("Vote")).thenReturn(2);
        when(resultSet.getDouble("Sentiment")).thenReturn(3.0);

        PreparedStatement updateStmt = mock(PreparedStatement.class);
        when(connection.prepareStatement(SQLQueries.UPDATE_FOOD_ITEM_AUDIT)).thenReturn(updateStmt);
        when(updateStmt.executeUpdate()).thenReturn(1);

        feedbackRepository.updateItemAudit(feedbackDTO, averageSentiment);

        verify(connection, times(1)).prepareStatement(SQLQueries.SELECT_FOOD_ITEM_AUDIT);
        verify(connection, times(1)).prepareStatement(SQLQueries.UPDATE_FOOD_ITEM_AUDIT);
        verify(preparedStatement, times(1)).setInt(1, feedbackDTO.getFoodItemId());
        verify(updateStmt, times(1)).setDouble(eq(1), anyDouble());
        verify(updateStmt, times(1)).setDouble(eq(2), anyDouble());
        verify(updateStmt, times(1)).setInt(eq(3), anyInt());
        verify(updateStmt, times(1)).setInt(eq(4), eq(feedbackDTO.getFoodItemId()));
        verify(updateStmt, times(1)).executeUpdate();
    }

    @Test
    public void testAddFeedback_Successful() throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFoodItemId(1);
        feedback.setUserId(1);
        feedback.setRating(5.0);
        feedback.setComment("Delicious!");
        feedback.setDelete(false);

        when(connection.prepareStatement(SQLQueries.ADD_FEEDBACK)).thenReturn(preparedStatement);

        feedbackRepository.addFeedback(feedback);

        verify(connection, times(1)).prepareStatement(SQLQueries.ADD_FEEDBACK);
        verify(preparedStatement, times(1)).setInt(1, feedback.getFoodItemId());
        verify(preparedStatement, times(1)).setInt(2, feedback.getUserId());
        verify(preparedStatement, times(1)).setDouble(3, feedback.getRating());
        verify(preparedStatement, times(1)).setString(4, feedback.getComment());
        verify(preparedStatement, times(1)).setBoolean(5, feedback.isDelete());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

