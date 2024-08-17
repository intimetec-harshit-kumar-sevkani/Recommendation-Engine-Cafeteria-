package service;

import org.example.model.FoodItem;
import org.example.repository.FoodItemAuditRepository;
import org.example.repository.FoodItemRepository;
import org.example.service.FoodItemService;
import org.example.service.NotificationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class FoodItemServiceTest {

    private FoodItemService foodItemService;
    private FoodItemRepository foodItemRepository;
    private NotificationService notificationService;
    private FoodItemAuditRepository foodItemAuditRepository;

    @Before
    public void setUp() throws SQLException {
        foodItemRepository = mock(FoodItemRepository.class);
        notificationService = mock(NotificationService.class);
        foodItemAuditRepository = mock(FoodItemAuditRepository.class);
        foodItemService = new FoodItemService();
        setField(foodItemService, "foodItemRepository", foodItemRepository);
        setField(foodItemService, "notificationService", notificationService);
        setField(foodItemService, "foodItemAuditRepository", foodItemAuditRepository);
    }

    @Test
    public void testAddFoodItem() throws SQLException {
        FoodItem foodItem = new FoodItem();
        when(foodItemRepository.addFoodItem(foodItem)).thenReturn(1);

        foodItemService.addFoodItem(foodItem);

        verify(foodItemRepository, times(1)).addFoodItem(foodItem);
        verify(notificationService, times(1)).sendNotification("FoodItem Update", "Food Item Added", 1);
        verify(foodItemAuditRepository, times(1)).addFoodAudit(1);
    }

    @Test
    public void testUpdateFoodItem() throws SQLException {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(1);

        foodItemService.updateFoodItem(foodItem);

        verify(foodItemRepository, times(1)).updateFoodItem(foodItem);
        verify(notificationService, times(1)).sendNotification("FoodItem Update", "Food Item Updated", 1);
    }

    @Test
    public void testDeleteFoodItem() throws SQLException {
        int id = 1;

        foodItemService.deleteFoodItem(id);

        verify(foodItemRepository, times(1)).deleteFoodItem(id);
        verify(notificationService, times(1)).sendNotification("FoodItem Update", "Food Item Deleted", id);
    }

    @Test
    public void testGetAllFoodItems() throws SQLException {
        foodItemService.getAllFoodItems();

        verify(foodItemRepository, times(1)).getAllFoodItems();
    }

    @Test
    public void testDiscardFoodItem() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2, 3);

        foodItemService.discardFoodItem(foodItemIds);

        verify(foodItemRepository, times(1)).discardFoodItems(foodItemIds);
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
