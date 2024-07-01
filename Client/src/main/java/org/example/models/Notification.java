package org.example.models;

import java.util.Date;

public class Notification {
    private int id;
    private int notificationTypeId;
    private String message;
    private boolean isDelete;

    private Date date;

    public Notification() {}

    public Notification(int id, int notificationTypeId, String message, boolean isDelete, Date date) {
        this.id = id;
        this.notificationTypeId = notificationTypeId;
        this.message = message;
        this.isDelete = isDelete;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", notificationTypeId=" + notificationTypeId +
                ", message='" + message + '\'' +
                ", isDelete=" + isDelete +
                ", date=" + date +
                '}';
    }
}
