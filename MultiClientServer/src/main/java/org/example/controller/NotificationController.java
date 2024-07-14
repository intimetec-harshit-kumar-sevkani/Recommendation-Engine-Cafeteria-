package org.example.controller;

import org.example.model.FoodItem;
import org.example.service.NotificationService;

import java.sql.SQLException;
import java.util.List;

public class NotificationController {

    private NotificationService notificationService;


    public NotificationController() throws SQLException {
        this.notificationService = new NotificationService();
    }

    public String sendNotification(List<FoodItem> foodItems)  {
        try {
            notificationService.sendNotificationForFeedback(foodItems);
            return "Notification Send Successfully";
        }
        catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}
