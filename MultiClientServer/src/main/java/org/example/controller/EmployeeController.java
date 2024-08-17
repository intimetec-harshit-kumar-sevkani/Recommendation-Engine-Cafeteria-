package org.example.controller;

import com.google.gson.Gson;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.model.UserProfile;
import org.example.service.*;
import org.example.util.SortFoodItems;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    private VotedItemService votedItemService;
    private FeedbackService feedbackService;

    private RecommendationService recommendationService;

    private EmployeeService employeeService;

    private Gson gson = new Gson();

    private NotificationService notificationService;

    public EmployeeController() throws SQLException {
        this.votedItemService = new VotedItemService();
        this.feedbackService = new FeedbackService();
        this.notificationService = new NotificationService();
        this.recommendationService = new RecommendationService();
        this.employeeService = new EmployeeService();
    }

    public String voteForFoodItems(List<Integer> votedFoodItems) {
        try {
            votedItemService.updateFoodItems(votedFoodItems);
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

    public String viewRollOutItems(String mealType, int userId) {
        try {
            List<FoodItem> foodItems = votedItemService.viewFoodItems(mealType);
            UserProfile userProfile = employeeService.getUserProfile(userId);
            List<FoodItem> sortFoodItems = SortFoodItems.sortFoodItems(foodItems,userProfile);
            return gson.toJson(sortFoodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }

    }

    public String getNotifications()  {
        try {
            List<Notification> notifications = notificationService.getNotifications();
            return gson.toJson(notifications);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String viewTodayMenu(int userId) {
        try {
            List<FoodItem> foodItems = votedItemService.getPreparedFoodItems();
            UserProfile userProfile = employeeService.getUserProfile(userId);
            List<FoodItem> sortFoodItems = SortFoodItems.sortFoodItems(foodItems,userProfile);
            return gson.toJson(sortFoodItems);
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String addUserProfile(UserProfile userProfile) {
        try {
             employeeService.addUserProfile(userProfile);
            return "Profile Added SuccessFully";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String updateUserProfile(UserProfile userProfile) {
        try {
            employeeService.updateUserProfile(userProfile);
            return "Profile Updated SuccessFully";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

}
