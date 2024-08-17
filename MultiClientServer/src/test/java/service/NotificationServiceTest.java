package service;

import org.example.model.FoodItem;
import org.example.repository.NotificationRepository;
import org.example.service.NotificationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private NotificationRepository notificationRepository;

    @Before
    public void setUp() throws SQLException {
        notificationRepository = mock(NotificationRepository.class);
        notificationService = new NotificationService();
        setField(notificationService, "notificationRepository", notificationRepository);
    }

    @Test
    public void testSendNotificationForFeedback() throws SQLException {
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(1), new FoodItem(2));

        when(notificationRepository.getNotificationTypeId("FoodItem Update")).thenReturn(1);

        notificationService.sendNotificationForFeedback(foodItems);

        String expectedMessage = "Provide Feedback for Discard Items: 1, 2";
        verify(notificationRepository, times(1)).addNotification(1, expectedMessage);
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

