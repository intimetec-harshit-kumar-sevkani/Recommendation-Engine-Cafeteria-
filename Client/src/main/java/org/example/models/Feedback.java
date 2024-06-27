package org.example.models;

import java.time.LocalDate;
import java.util.Date;

public class Feedback {
    private int id;
    private int foodItemId;
    private int userId;
    private double rating;
    private String comment;
    private Date date;
    private boolean isDelete;

    public Feedback() {}

    public Feedback(int id, int foodItemId, int userId, double rating, String comment, Date date, boolean isDelete) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.isDelete = isDelete;
    }

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        return "Feedback{id=" + id + ", foodItemId=" + foodItemId + ", userId=" + userId + ", rating=" + rating + ", comment='" + comment + "', date=" + date + ", isDelete=" + isDelete + "}";
    }
}

