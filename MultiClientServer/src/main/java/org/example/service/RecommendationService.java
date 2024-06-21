package org.example.service;

import org.example.model.FoodItem;
import org.example.model.FoodItemRating;
import org.example.repository.FeedbackRepository;
import org.example.repository.FoodItemRepository;
import org.example.repository.VotedItemRepository;
import org.example.util.SentimentAnalyzer;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<FoodItem> getAllFoodItems() throws SQLException {
        List<FoodItem> allTopFoodItems = new ArrayList<>();
        List<String> mealTypes = List.of("Breakfast", "Lunch", "Dinner");
        for (String mealType : mealTypes) {
            List<FoodItem> topFoodItems = foodItemRepository.getTopFoodItems(mealType);
            allTopFoodItems.addAll(topFoodItems);
        }

        votedItemRepository.insertFoodItemsToVotedItems(allTopFoodItems);

        return allTopFoodItems;
    }

    public String getRecommendation() throws SQLException {
        List<FoodItemRating> foodItemRatings = feedbackRepository.getFoodItemRatingsForToday();

        for (FoodItemRating foodItemRating : foodItemRatings) {
            List<String> comments = Arrays.asList(foodItemRating.getComments().split(", "));

            double averageSentiment = SentimentAnalyzer.getAverageRating(comments);
            feedbackRepository.updateItemAudit(foodItemRating, averageSentiment);
        }

        return "update audit successfully";
    }



}
