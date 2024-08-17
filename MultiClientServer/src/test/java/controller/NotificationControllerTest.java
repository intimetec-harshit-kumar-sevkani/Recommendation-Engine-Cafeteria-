package controller;

import org.example.controller.NotificationController;
import org.example.model.FoodItem;
import org.example.service.NotificationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    private NotificationController notificationController;
    private NotificationService notificationService;

    @Before
    public void setUp() throws SQLException {
        notificationService = mock(NotificationService.class);
        notificationController = new NotificationController();
        setField(notificationController, "notificationService", notificationService);
    }

    @Test
    public void testSendNotifications_success() throws SQLException {
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(), new FoodItem());

        String result = notificationController.sendNotifications(foodItems);

        verify(notificationService, times(1)).sendNotificationForFeedback(foodItems);
        assertEquals("Notification Send Successfully", result);
    }

    @Test
    public void testSendNotifications_failure() throws SQLException {
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(), new FoodItem());
        doThrow(new SQLException("Database error")).when(notificationService).sendNotificationForFeedback(foodItems);

        String result = notificationController.sendNotifications(foodItems);

        verify(notificationService, times(1)).sendNotificationForFeedback(foodItems);
        assertEquals("Error: Database error", result);
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
