package controller;

import com.google.gson.Gson;
import org.example.DTO.RecommendedDTO;
import org.example.DTO.RollOutFoodItemsDTO;
import org.example.controller.ChefController;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;
import org.example.service.RecommendationService;
import org.example.service.VotedItemService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ChefControllerTest {

    private ChefController chefController;
    private RecommendationService recommendationService;
    private FoodItemService foodItemService;
    private VotedItemService votedItemService;
    private NotificationService notificationService;
    private Gson gson;

    @Before
    public void setUp() throws SQLException {
        recommendationService = mock(RecommendationService.class);
        foodItemService = mock(FoodItemService.class);
        votedItemService = mock(VotedItemService.class);
        notificationService = mock(NotificationService.class);
        gson = new Gson();
        chefController = new ChefController();
        setField(chefController, "recommendationService", recommendationService);
        setField(chefController, "foodItemService", foodItemService);
        setField(chefController, "votedItemService", votedItemService);
        setField(chefController, "notificationService", notificationService);
        setField(chefController, "gson", gson);
    }

    @Test
    public void testGetRecommendedFoodItems_success() throws SQLException {
        RecommendedDTO recommendedDTO = new RecommendedDTO();
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(), new FoodItem());

        when(recommendationService.getRecommendedFoodItems(recommendedDTO)).thenReturn(foodItems);

        String result = chefController.getRecommendedFoodItems(recommendedDTO);

        verify(recommendationService, times(1)).getRecommendedFoodItems(recommendedDTO);
        assertEquals(gson.toJson(foodItems), result);
    }

    @Test
    public void testGetRecommendedFoodItems_failure() throws SQLException {
        RecommendedDTO recommendedDTO = new RecommendedDTO();
        doThrow(new SQLException("Database error")).when(recommendationService).getRecommendedFoodItems(recommendedDTO);

        String result = chefController.getRecommendedFoodItems(recommendedDTO);

        verify(recommendationService, times(1)).getRecommendedFoodItems(recommendedDTO);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testGetVotedItems_success() throws SQLException {
        String mealType = "Lunch";
        List<RollOutFoodItemsDTO> foodItems = Arrays.asList(new RollOutFoodItemsDTO(), new RollOutFoodItemsDTO());

        when(votedItemService.viewVotedFoodItem(mealType)).thenReturn(foodItems);

        String result = chefController.getVotedItems(mealType);

        verify(votedItemService, times(1)).viewVotedFoodItem(mealType);
        assertEquals(gson.toJson(foodItems), result);
    }

    @Test
    public void testGetVotedItems_failure() throws SQLException {
        String mealType = "Lunch";
        doThrow(new SQLException("Database error")).when(votedItemService).viewVotedFoodItem(mealType);

        String result = chefController.getVotedItems(mealType);

        verify(votedItemService, times(1)).viewVotedFoodItem(mealType);
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testRollOutFoodItems_success() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2);

        String result = chefController.rollOutFoodItems(foodItemIds);

        verify(votedItemService, times(1)).rollOutFoodItems(foodItemIds);
        verify(notificationService, times(1)).sendNotification(foodItemIds, "RollOut Menu");
        assertEquals("Food item Roll Out successfully.", result);
    }

    @Test
    public void testRollOutFoodItems_failure() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2);
        doThrow(new SQLException("Database error")).when(votedItemService).rollOutFoodItems(foodItemIds);

        String result = chefController.rollOutFoodItems(foodItemIds);

        verify(votedItemService, times(1)).rollOutFoodItems(foodItemIds);
        verify(notificationService, never()).sendNotification(anyList(), anyString());
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testGetNotifications_success() throws SQLException {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());

        when(notificationService.getNotifications()).thenReturn(notifications);

        String result = chefController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals(gson.toJson(notifications), result);
    }

    @Test
    public void testGetNotifications_failure() throws SQLException {
        doThrow(new SQLException("Database error")).when(notificationService).getNotifications();

        String result = chefController.getNotifications();

        verify(notificationService, times(1)).getNotifications();
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testViewDiscardedItems_success() throws SQLException {
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(), new FoodItem());

        when(recommendationService.getDiscardedFoodItems()).thenReturn(foodItems);

        String result = chefController.viewDiscardedItems();

        verify(recommendationService, times(1)).getDiscardedFoodItems();
        assertEquals(gson.toJson(foodItems), result);
    }

    @Test
    public void testViewDiscardedItems_failure() throws SQLException {
        doThrow(new SQLException("Database error")).when(recommendationService).getDiscardedFoodItems();

        String result = chefController.viewDiscardedItems();

        verify(recommendationService, times(1)).getDiscardedFoodItems();
        assertEquals("Error: Database error", result);
    }

    @Test
    public void testDiscardItems_success() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2);

        String result = chefController.discardItems(foodItemIds);

        verify(foodItemService, times(1)).discardFoodItem(foodItemIds);
        assertEquals("Food items discarded successfully", result);
    }

    @Test
    public void testDiscardItems_failure() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2);
        doThrow(new SQLException("Database error")).when(foodItemService).discardFoodItem(foodItemIds);

        String result = chefController.discardItems(foodItemIds);

        verify(foodItemService, times(1)).discardFoodItem(foodItemIds);
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
