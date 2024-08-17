package controller;

import com.google.gson.Gson;
import org.example.controller.EmployeeController;
import org.example.model.Feedback;
import org.example.model.Notification;
import org.example.model.UserProfile;
import org.example.service.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    private EmployeeController employeeController;
    private VotedItemService votedItemService;
    private FeedbackService feedbackService;

    private NotificationService notificationService;

    private RecommendationService recommendationService;

    private EmployeeService employeeService;

    @Before
    public void setUp() throws SQLException {
        employeeService = mock(EmployeeService.class);
        feedbackService = mock(FeedbackService.class);
        recommendationService = mock(RecommendationService.class);
        votedItemService = mock(VotedItemService.class);
        notificationService = mock(NotificationService.class);
        employeeController = new EmployeeController();
        setField(employeeController, "employeeService", employeeService);
        setField(employeeController, "feedbackService", feedbackService);
        setField(employeeController, "recommendationService", recommendationService);
        setField(employeeController, "votedItemService", votedItemService);
        setField(employeeController, "notificationService",notificationService);
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

    @Test
    public void testVoteForFoodItems_success() throws SQLException {
        List<Integer> votedFoodItems = Arrays.asList(1, 2, 3);

        String result = employeeController.voteForFoodItems(votedFoodItems);

        verify(votedItemService, times(1)).updateFoodItems(votedFoodItems);
        assertEquals("Food item voted successfully.", result);
    }

    @Test
    public void testVoteForFoodItems_failure() throws SQLException {
        List<Integer> votedFoodItems = Arrays.asList(1, 2, 3);
        doThrow(new SQLException("Database error")).when(votedItemService).updateFoodItems(votedFoodItems);

        String result = employeeController.voteForFoodItems(votedFoodItems);

        verify(votedItemService, times(1)).updateFoodItems(votedFoodItems);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testAddFeedback_success() throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFoodItemId(1);

        String result = employeeController.addFeedback(feedback);

        verify(feedbackService, times(1)).addFeedback(feedback);
        verify(recommendationService, times(1)).updateRating(feedback.getFoodItemId());
        assertEquals("Feedback added successfully.", result);
    }

    @Test
    public void testAddFeedback_failure() throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFoodItemId(1);
        doThrow(new SQLException("Database error")).when(feedbackService).addFeedback(feedback);

        String result = employeeController.addFeedback(feedback);

        verify(feedbackService, times(1)).addFeedback(feedback);
        verify(recommendationService, never()).updateRating(anyInt());
        assertEquals("Error: Database error", result);
    }


    @Test
    public void testViewRollOutItems_failure() throws SQLException {
        String mealType = "Lunch";
        int userId = 1;
        doThrow(new SQLException("Database error")).when(votedItemService).viewFoodItems(mealType);

        String result = employeeController.viewRollOutItems(mealType, userId);

        verify(votedItemService, times(1)).viewFoodItems(mealType);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testGetNotifications_success() throws SQLException {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        when(notificationService.getNotifications()).thenReturn(notifications);

        String result = employeeController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals(new Gson().toJson(notifications), result);
    }

    @Test
    public void testGetNotifications_failure() throws SQLException {
        doThrow(new SQLException("Database error")).when(notificationService).getNotifications();

        String result = employeeController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testViewTodayMenu_failure() throws SQLException {
        int userId = 1;
        doThrow(new SQLException("Database error")).when(votedItemService).getPreparedFoodItems();

        String result = employeeController.viewTodayMenu(userId);

        verify(votedItemService, times(1)).getPreparedFoodItems();
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testAddUserProfile_success() throws SQLException {
        UserProfile userProfile = new UserProfile();

        String result = employeeController.addUserProfile(userProfile);

        verify(employeeService, times(1)).addUserProfile(userProfile);
        assertEquals("Profile Added SuccessFully", result);
    }

    @Test
    public void testAddUserProfile_failure() throws SQLException {
        UserProfile userProfile = new UserProfile();
        doThrow(new SQLException("Database error")).when(employeeService).addUserProfile(userProfile);

        String result = employeeController.addUserProfile(userProfile);

        verify(employeeService, times(1)).addUserProfile(userProfile);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testUpdateUserProfile_success() throws SQLException {
        UserProfile userProfile = new UserProfile();

        String result = employeeController.updateUserProfile(userProfile);

        verify(employeeService, times(1)).updateUserProfile(userProfile);
        assertEquals("Profile Updated SuccessFully", result);
    }

    @Test
    public void testUpdateUserProfile_failure() throws SQLException {
        UserProfile userProfile = new UserProfile();
        doThrow(new SQLException("Database error")).when(employeeService).updateUserProfile(userProfile);

        String result = employeeController.updateUserProfile(userProfile);

        verify(employeeService, times(1)).updateUserProfile(userProfile);
        assertEquals("Error: Database error", result);
    }

}
