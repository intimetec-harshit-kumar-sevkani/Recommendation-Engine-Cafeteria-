package org.example.models;

public class UserProfile {
    private int userId;
    private String foodType;
    private String spiceLevel;
    private String originality;
    private boolean sweetTooth;

    public UserProfile(int userId, String foodType, String spiceLevel, String originality, boolean sweetTooth) {
        this.userId = userId;
        this.foodType = foodType;
        this.spiceLevel = spiceLevel;
        this.originality = originality;
        this.sweetTooth = sweetTooth;
    }

    public int getUserId() {
        return userId;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public String getOriginality() {
        return originality;
    }

    public boolean isSweetTooth() {
        return sweetTooth;
    }
}
