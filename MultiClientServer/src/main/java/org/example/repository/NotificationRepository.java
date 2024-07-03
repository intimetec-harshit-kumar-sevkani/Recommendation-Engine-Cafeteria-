package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class NotificationRepository {

    private Connection connection;

    public NotificationRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void sendNotification(List<Integer> foodItemIds, String notificationType) throws SQLException {
        int notificationTypeId = getNotificationTypeId(notificationType);
        if (notificationTypeId == -1) {
            throw new IllegalArgumentException("Invalid notification type");
        }

        for (Integer foodItemId : foodItemIds) {
            String sql = "SELECT Name, MealTypeId FROM fooditems WHERE Id = ? AND IsDelete = 0";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, foodItemId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String foodItemName = rs.getString("Name");
                        int mealTypeId = rs.getInt("MealTypeId");
                        String mealType = getMealType(mealTypeId);
                        String message = mealType + ": " + foodItemName;
                        //insertNotification(notificationTypeId, message);
                        addNotification(notificationTypeId, message);
                    }
                }
            }
        }
    }

    public int getNotificationTypeId(String notificationType) throws SQLException {
        String sql = "SELECT Id FROM NotificationTypes WHERE Type = ? AND IsDelete = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, notificationType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }
        }
        return -1;
    }

    private String getMealType(int mealTypeId) throws SQLException {
        String sql = "SELECT Type FROM MealTypes WHERE Id = ? AND IsDelete = 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, mealTypeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Type");
                }
            }
        }
        return null;
    }

    public List<Notification> getValidNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.Id, n.NotificationTypeId, n.Message, n.IsDelete, n.Date, nt.ValidFor " +
                "FROM notifications n " +
                "JOIN NotificationTypes nt ON n.NotificationTypeId = nt.Id " +
                "WHERE n.IsDelete = 0 AND nt.IsDelete = 0";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int notificationId = rs.getInt("Id");
                int notificationTypeId = rs.getInt("NotificationTypeId");
                String message = rs.getString("Message");
                boolean isDelete = rs.getBoolean("IsDelete");
                java.sql.Timestamp date = rs.getTimestamp("Date");
                int validFor = rs.getInt("ValidFor");

                java.sql.Timestamp validUntil = new java.sql.Timestamp(date.getTime() + (validFor * 24L * 60L * 60L * 1000L));
                java.sql.Timestamp currentDate = new java.sql.Timestamp(System.currentTimeMillis());

                if (validUntil.after(currentDate) || validUntil.equals(currentDate)) {
                    Notification notification = new Notification(notificationId, notificationTypeId, message, isDelete,date);
                    notifications.add(notification);
                }
            }
        }

        return notifications;
    }

    public void addNotification(int notificationTypeId, String message) throws SQLException {
        String sql = "INSERT INTO Notifications (NotificationTypeId, Message, IsDelete, Date) VALUES (?, ?, 0, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationTypeId);
            stmt.setString(2, message);
            stmt.executeUpdate();
        }
    }

}
