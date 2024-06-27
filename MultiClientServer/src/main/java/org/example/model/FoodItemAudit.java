package org.example.model;

public class FoodItemAudit {
    private int id;
    private int foodItemId;
    private double rating;
    private int vote;
    private double sentiment;
    private int prepared;

    public FoodItemAudit() {}

    public FoodItemAudit(int id, int foodItemId, double rating, int vote, double sentiment, int prepared) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.rating = rating;
        this.vote = vote;
        this.sentiment = sentiment;
        this.prepared = prepared;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public double getSentiment() {
        return sentiment;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public int getPrepared() {
        return prepared;
    }

    public void setPrepared(int prepared) {
        this.prepared = prepared;
    }

    @Override
    public String toString() {
        return "FoodItemAudit{id=" + id + ", foodItemId=" + foodItemId + ", rating=" + rating + ", vote=" + vote + ", sentiment=" + sentiment + ", prepared=" + prepared + "}";
    }
}
