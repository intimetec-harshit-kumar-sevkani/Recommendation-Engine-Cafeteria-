package controller;

import com.google.gson.Gson;
import org.example.controller.FoodItemController;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class FoodItemControllerTest {

    private FoodItemController foodItemController;
    private FoodItemService foodItemService;
    private NotificationService notificationService;
    private Gson gson;

    @Before
    public void setUp() throws SQLException {
        foodItemService = mock(FoodItemService.class);
        notificationService = mock(NotificationService.class);
        gson = new Gson();
        foodItemController = new FoodItemController(foodItemService, notificationService, gson);
        setField(foodItemController,"foodItemService",foodItemService);
        setField(foodItemController,"notificationService",notificationService);
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
    public void testAddFoodItem_success() throws SQLException {
        FoodItem foodItem = new FoodItem();

        String result = foodItemController.addFoodItem(foodItem);

        verify(foodItemService, times(1)).addFoodItem(foodItem);
        assertEquals("Food item added successfully.", result);
    }

    @Test
    public void testAddFoodItem_failure() throws SQLException {
        FoodItem foodItem = new FoodItem();
        doThrow(new SQLException("Database error")).when(foodItemService).addFoodItem(foodItem);

        String result = foodItemController.addFoodItem(foodItem);

        verify(foodItemService, times(1)).addFoodItem(foodItem);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testUpdateFoodItem_success() throws SQLException {
        FoodItem foodItem = new FoodItem();

        String result = foodItemController.updateFoodItem(foodItem);

        verify(foodItemService, times(1)).updateFoodItem(foodItem);
        assertEquals("Food item updated successfully.", result);
    }

    @Test
    public void testUpdateFoodItem_failure() throws SQLException {
        FoodItem foodItem = new FoodItem();
        doThrow(new SQLException("Database error")).when(foodItemService).updateFoodItem(foodItem);

        String result = foodItemController.updateFoodItem(foodItem);

        verify(foodItemService, times(1)).updateFoodItem(foodItem);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testDeleteFoodItem_success() throws SQLException {
        int foodItemId = 1;

        String result = foodItemController.deleteFoodItem(foodItemId);

        verify(foodItemService, times(1)).deleteFoodItem(foodItemId);
        assertEquals("Food item deleted successfully.", result);
    }

    @Test
    public void testDeleteFoodItem_failure() throws SQLException {
        int foodItemId = 1;
        doThrow(new SQLException("Database error")).when(foodItemService).deleteFoodItem(foodItemId);

        String result = foodItemController.deleteFoodItem(foodItemId);

        verify(foodItemService, times(1)).deleteFoodItem(foodItemId);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testGetAllFoodItems_success() throws SQLException {
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(), new FoodItem());

        when(foodItemService.getAllFoodItems()).thenReturn(foodItems);

        String result = foodItemController.getAllFoodItems();

        verify(foodItemService, times(1)).getAllFoodItems();
        assertEquals(gson.toJson(foodItems), result);
    }

    @Test
    public void testGetAllFoodItems_failure() throws SQLException {
        doThrow(new SQLException("Database error")).when(foodItemService).getAllFoodItems();

        String result = foodItemController.getAllFoodItems();

        verify(foodItemService, times(1)).getAllFoodItems();
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testGetNotifications_success() throws SQLException {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        when(notificationService.getNotifications()).thenReturn(notifications);

        String result = foodItemController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals(gson.toJson(notifications), result);
    }

    @Test
    public void testGetNotifications_failure() throws SQLException {
        doThrow(new SQLException("Database error")).when(notificationService).getNotifications();

        String result = foodItemController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals("Error: Database error", result);
    }
}