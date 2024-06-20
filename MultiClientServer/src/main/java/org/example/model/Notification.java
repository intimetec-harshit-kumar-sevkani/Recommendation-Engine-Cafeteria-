package org.example.model;

public class Notification {
    private int id;
    private int notificationTypeId;
    private String message;
    private boolean isDelete;

    // Constructors
    public Notification() {}

    public Notification(int id, int notificationTypeId, String message, boolean isDelete) {
        this.id = id;
        this.notificationTypeId = notificationTypeId;
        this.message = message;
        this.isDelete = isDelete;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    // toString
    @Override
    public String toString() {
        return "Notification{id=" + id + ", notificationTypeId=" + notificationTypeId + ", message='" + message + "', isDelete=" + isDelete + "}";
    }
}
