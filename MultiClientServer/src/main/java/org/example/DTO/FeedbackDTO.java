package org.example.DTO;

public class FeedbackDTO {
    private int foodItemId;
    private double lastRating;
    private String lastComment;

    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getLastRating() {
        return lastRating;
    }

    public void setLastRating(double lastRating) {
        this.lastRating = lastRating;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }
}
