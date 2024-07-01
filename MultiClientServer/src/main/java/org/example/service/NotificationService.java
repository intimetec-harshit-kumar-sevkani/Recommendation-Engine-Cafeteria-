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
}
