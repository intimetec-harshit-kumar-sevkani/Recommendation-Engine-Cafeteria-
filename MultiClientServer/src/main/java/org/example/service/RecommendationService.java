package org.example.service;

import org.example.model.FoodItem;
import org.example.model.FoodItemRating;
import org.example.model.RecommendedDTO;
import org.example.repository.FeedbackRepository;
import org.example.repository.FoodItemRepository;
import org.example.repository.VotedItemRepository;
import org.example.util.SentimentAnalyzer;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class RecommendationService {
    private FoodItemRepository foodItemRepository;
    private FeedbackRepository feedbackRepository;
    private VotedItemRepository votedItemRepository;

    public RecommendationService() throws SQLException {
        this.foodItemRepository = new FoodItemRepository();
        this.votedItemRepository = new VotedItemRepository();
        this.feedbackRepository = new FeedbackRepository();
    }

    public List<FoodItem> getRecommendedFoodItems(RecommendedDTO recommendedDTO) throws SQLException {
        List<FoodItem> topFoodItems = foodItemRepository.getRecommendedFoodItems(recommendedDTO.getMealType(),recommendedDTO.getNumberOfItems());
        votedItemRepository.addVotedItems(topFoodItems);
        return topFoodItems;
    }

    public void updateRating(int foodItemId) throws SQLException {
        List<FoodItemRating> foodItemRatings = feedbackRepository.getFoodItemRatings(foodItemId);

        for (FoodItemRating foodItemRating : foodItemRatings) {
            List<String> comments = Arrays.asList(foodItemRating.getComments().split(", "));
            double averageSentiment = SentimentAnalyzer.getAverageRating(comments);
            feedbackRepository.updateItemAudit(foodItemRating, averageSentiment);
        }

    }

    // getDiscardedFoodItems

    public List<FoodItem> getDiscardedFoodItems() throws SQLException {
        List<Integer> foodItemIds = foodItemRepository.getDiscardedFoodItems();
        List<FoodItem> foodItems = foodItemRepository.getById(foodItemIds);
        return foodItems;
    }

}
