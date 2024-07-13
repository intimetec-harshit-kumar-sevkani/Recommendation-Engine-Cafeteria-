package org.example.model;


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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public void setOriginality(String originality) {
        this.originality = originality;
    }

    public void setSweetTooth(boolean sweetTooth) {
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
