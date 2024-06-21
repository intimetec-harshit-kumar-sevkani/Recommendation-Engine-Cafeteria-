package org.example.model;

public class FoodItemRating {
    private int foodItemId;
    private double averageRating;
    private String comments;

    // Constructor
    public FoodItemRating() {}

    // Getters and Setters
    public int getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(int foodItemId) {
        this.foodItemId = foodItemId;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // Optional: toString method for easy printing
    @Override
    public String toString() {
        return "FoodItemRating{" +
                "foodItemId=" + foodItemId +
                ", averageRating=" + averageRating +
                ", comments='" + comments + '\'' +
                '}';
    }
}

