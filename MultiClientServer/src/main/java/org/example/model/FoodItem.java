package org.example.model;

import java.math.BigDecimal;
import java.util.Objects;

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

    public FoodItem(int id) {
        this.id = id;
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return id == foodItem.id &&
                isAvailable == foodItem.isAvailable &&
                isDelete == foodItem.isDelete &&
                sweetTooth == foodItem.sweetTooth &&
                Objects.equals(name, foodItem.name) &&
                Objects.equals(price, foodItem.price) &&
                Objects.equals(foodType, foodItem.foodType) &&
                Objects.equals(spiceLevel, foodItem.spiceLevel) &&
                Objects.equals(originality, foodItem.originality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, isAvailable, isDelete, foodType, spiceLevel, originality, sweetTooth);
    }



}
