package org.example.model;

import java.util.Date;

public class VotedItem {
    private int id;
    private int foodItemId;
    private int userId;
    private Date date;
    private boolean isDelete;

    // Constructors
    public VotedItem() {}

    public VotedItem(int id, int foodItemId, int userId, Date date, boolean isDelete) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.userId = userId;
        this.date = date;
        this.isDelete = isDelete;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    // toString
    @Override
    public String toString() {
        return "VotedItem{id=" + id + ", foodItemId=" + foodItemId + ", userId=" + userId + ", date=" + date + ", isDelete=" + isDelete + "}";
    }
}
