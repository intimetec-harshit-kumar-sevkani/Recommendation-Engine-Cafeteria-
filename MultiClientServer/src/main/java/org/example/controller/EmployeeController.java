package org.example.controller;

import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.Notification;
import org.example.model.VotedItem;
import org.example.service.FeedbackService;
import org.example.service.NotificationService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    private VotedItemService votedItemService;
    private FeedbackService feedbackService;

    private NotificationService notificationService;

    public EmployeeController() throws SQLException {
        this.votedItemService = new VotedItemService();
        this.feedbackService = new FeedbackService();
        this.notificationService = new NotificationService();
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
            return "Feedback added successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }

    public List<FoodItem> viewVoteItem(String mealType) throws SQLException {
            List<FoodItem> foodItems = votedItemService.viewFoodItem(mealType);
            return foodItems;
    }

    public List<Notification> getNotification() throws SQLException {
        return notificationService.getNotification();
    }
}
