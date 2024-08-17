package service;

import org.example.DTO.FeedbackDTO;
import org.example.DTO.RecommendedDTO;
import org.example.model.FoodItem;
import org.example.repository.FeedbackRepository;
import org.example.repository.FoodItemRepository;
import org.example.repository.VotedItemRepository;
import org.example.service.RecommendationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecommendationServiceTest {

    private RecommendationService recommendationService;
    private FoodItemRepository foodItemRepository;
    private VotedItemRepository votedItemRepository;
    private FeedbackRepository feedbackRepository;

    @Before
    public void setUp() throws SQLException {
        foodItemRepository = mock(FoodItemRepository.class);
        votedItemRepository = mock(VotedItemRepository.class);
        feedbackRepository = mock(FeedbackRepository.class);
        recommendationService = new RecommendationService();
        setField(recommendationService, "foodItemRepository", foodItemRepository);
        setField(recommendationService, "votedItemRepository", votedItemRepository);
        setField(recommendationService, "feedbackRepository", feedbackRepository);
    }

    @Test
    public void testGetRecommendedFoodItems() throws SQLException {
        RecommendedDTO recommendedDTO = new RecommendedDTO();
        recommendedDTO.setMealType("Breakfast");
        recommendedDTO.setNumberOfItems(5);

        List<FoodItem> foodItems = Arrays.asList(new FoodItem(1), new FoodItem(2));

        when(foodItemRepository.getRecommendedFoodItems("Breakfast", 5)).thenReturn(foodItems);

        List<FoodItem> result = recommendationService.getRecommendedFoodItems(recommendedDTO);

        verify(votedItemRepository, times(1)).addVotedItems(foodItems);
        assertEquals(foodItems, result);
    }

    @Test
    public void testUpdateRating() throws SQLException {
        int foodItemId = 1;
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setLastComment("Good");

        when(feedbackRepository.getLastFoodItemFeedback(foodItemId)).thenReturn(feedbackDTO);

        recommendationService.updateRating(foodItemId);

        verify(feedbackRepository, times(1)).updateItemAudit(any(FeedbackDTO.class), anyDouble());
    }

    @Test
    public void testGetDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2);

        when(foodItemRepository.getDiscardedFoodItems()).thenReturn(foodItemIds);
        when(foodItemRepository.getById(foodItemIds)).thenReturn(Arrays.asList(new FoodItem(1), new FoodItem(2)));

        List<FoodItem> result = recommendationService.getDiscardedFoodItems();

        assertEquals(foodItemIds.size(), result.size());
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
