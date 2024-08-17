package service;

import org.example.DTO.RollOutFoodItemsDTO;
import org.example.model.FoodItem;
import org.example.repository.VotedItemRepository;
import org.example.service.VotedItemService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VotedItemServiceTest {

    private VotedItemService votedItemService;
    private VotedItemRepository votedItemRepository;

    @Before
    public void setUp() throws SQLException {
        votedItemRepository = mock(VotedItemRepository.class);
        votedItemService = new VotedItemService();
        setField(votedItemService, "votedItemRepository", votedItemRepository);
    }

    @Test
    public void testUpdateFoodItems() throws SQLException {
        List<Integer> votedItemIds = Arrays.asList(1, 2, 3);

        votedItemService.updateFoodItems(votedItemIds);

        verify(votedItemRepository, times(1)).voteFoodItems(votedItemIds);
    }

    @Test
    public void testRollOutFoodItems() throws SQLException {
        List<Integer> votedItemIds = Arrays.asList(4, 5, 6);

        votedItemService.rollOutFoodItems(votedItemIds);

        verify(votedItemRepository, times(1)).markFoodItemsAsPrepared(votedItemIds);
    }

    @Test
    public void testViewVotedFoodItem() throws SQLException {
        String mealType = "Lunch";
        List<RollOutFoodItemsDTO> rollOutFoodItemsDTOList = Arrays.asList(new RollOutFoodItemsDTO(), new RollOutFoodItemsDTO());
        when(votedItemRepository.getRollOutItem(mealType)).thenReturn(rollOutFoodItemsDTOList);

        List<RollOutFoodItemsDTO> result = votedItemService.viewVotedFoodItem(mealType);

        verify(votedItemRepository, times(1)).getRollOutItem(mealType);
        assertEquals(rollOutFoodItemsDTOList, result);
    }

    @Test
    public void testViewFoodItems() throws SQLException {
        String mealType = "Dinner";
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(7), new FoodItem(8));
        when(votedItemRepository.getFoodItemsForVote(mealType)).thenReturn(foodItems);

        List<FoodItem> result = votedItemService.viewFoodItems(mealType);

        verify(votedItemRepository, times(1)).getFoodItemsForVote(mealType);
        assertEquals(foodItems, result);
    }

    @Test
    public void testGetPreparedFoodItems() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(9, 10);
        List<FoodItem> foodItems = Arrays.asList(new FoodItem(9), new FoodItem(10));
        when(votedItemRepository.getPreparedFoodItemIds()).thenReturn(foodItemIds);
        when(votedItemRepository.getFoodItemsByIds(foodItemIds)).thenReturn(foodItems);

        List<FoodItem> result = votedItemService.getPreparedFoodItems();

        verify(votedItemRepository, times(1)).getPreparedFoodItemIds();
        verify(votedItemRepository, times(1)).getFoodItemsByIds(foodItemIds);
        assertEquals(foodItems, result);
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
