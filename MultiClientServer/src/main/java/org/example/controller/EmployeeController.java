package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.service.FeedbackService;
import org.example.service.NotificationService;
import org.example.service.RecommendationService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    private VotedItemService votedItemService;
    private FeedbackService feedbackService;

    private RecommendationService recommendationService;

    private Gson gson = new Gson();

    private NotificationService notificationService;

    public EmployeeController() throws SQLException {
        this.votedItemService = new VotedItemService();
        this.feedbackService = new FeedbackService();
        this.notificationService = new NotificationService();
        this.recommendationService = new RecommendationService();
    }

    public String voteFoodItem(List<Integer> votedFoodItems) {
        try {
            votedItemService.updateFoodItem(votedFoodItems);
            return "Food item voted successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String addFeedback(Feedback feedback) {
        try {
            feedbackService.addFeedback(feedback);
            recommendationService.updateRating(feedback.getFoodItemId());
            return "Feedback added successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String viewRollOutItem(String mealType) {
        try {
            List<FoodItem> foodItems = votedItemService.viewFoodItem(mealType);
            String foodItemsJson = gson.toJson(foodItems);
            return foodItemsJson;
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

    }

    public String getNotification()  {
        try {
            List<Notification> notifications = notificationService.getNotification();
            String notificationJson = gson.toJson(notifications);
            return notificationJson;
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String viewTodayMenu() {
        try {
            List<FoodItem> foodItems = votedItemService.getPreparedFoodItems();
            String foodItemsJson = gson.toJson(foodItems);
            return foodItemsJson;
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
