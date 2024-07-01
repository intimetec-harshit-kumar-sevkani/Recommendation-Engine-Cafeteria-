package org.example.service;

import org.example.model.Notification;
import org.example.repository.NotificationRepository;

import java.sql.SQLException;
import java.util.List;

public class NotificationService {

    private NotificationRepository notificationRepository;

    public NotificationService() throws SQLException {
        this.notificationRepository = new NotificationRepository();
    }

    public void sendNotification(List<Integer> foodItemIds, String notificationType) throws SQLException {
        notificationRepository.sendNotification(foodItemIds,notificationType);
    }

    public List<Notification> getNotification() throws SQLException {
        return notificationRepository.getValidNotifications();
    }

    public void sendNotification(String notificationType, String task, int foodItemId) throws SQLException {
        int notificationTypeId = notificationRepository.getNotificationTypeId(notificationType);
        if (notificationTypeId != -1) {
            String message = task + ": " + foodItemId;
            notificationRepository.addNotification(notificationTypeId, message);
        } else {
            System.out.println("Invalid notification type: " + notificationType);
        }
    }

}
