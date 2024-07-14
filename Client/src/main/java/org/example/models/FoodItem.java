package org.example.models;

import java.math.BigDecimal;

public class FoodItem {
    private int id;
    private int mealTypeId;
    private String name;
    private BigDecimal price;
    private boolean isAvailable;
    private boolean isDelete;

    private String foodType;
    private String spiceLevel;
    private String originality;
    private boolean sweetTooth;
    public FoodItem() {
    }

   /* public FoodItem(int id, int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete) {
        this.id = id;
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
    }*/

    public FoodItem(int id, int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete, String foodType, String spiceLevel, String originality, boolean sweetTooth) {
        this.id = id;
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
        this.foodType = foodType;
        this.spiceLevel = spiceLevel;
        this.originality = originality;
        this.sweetTooth = sweetTooth;
    }

    public FoodItem(int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete, String foodType, String spiceLevel, String originality, boolean sweetTooth) {
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
        this.foodType = foodType;
        this.spiceLevel = spiceLevel;
        this.originality = originality;
        this.sweetTooth = sweetTooth;
    }

   /* public FoodItem(int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete) {
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(int mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(String spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getOriginality() {
        return originality;
    }

    public void setOriginality(String originality) {
        this.originality = originality;
    }

    public boolean isSweetTooth() {
        return sweetTooth;
    }

    public void setSweetTooth(boolean sweetTooth) {
        this.sweetTooth = sweetTooth;
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", mealTypeId=" + mealTypeId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", isDelete=" + isDelete +
                ", foodType='" + foodType + '\'' +
                ", spiceLevel='" + spiceLevel + '\'' +
                ", originality='" + originality + '\'' +
                ", sweetTooth=" + sweetTooth +
                '}';
    }
}

