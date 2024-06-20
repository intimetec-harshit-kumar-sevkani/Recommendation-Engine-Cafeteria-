package org.example.models;

import java.math.BigDecimal;

public class FoodItem {
    private int id;
    private int mealTypeId;
    private String name;
    private BigDecimal price;
    private boolean isAvailable;
    private boolean isDelete;

    // Constructors, getters, and setters


    public FoodItem() {
    }

    public FoodItem(int id, int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete) {
        this.id = id;
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
    }

    public FoodItem(int mealTypeId, String name, BigDecimal price, boolean isAvailable, boolean isDelete) {
        this.mealTypeId = mealTypeId;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isDelete = isDelete;
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

    @Override
    public String toString() {
        return "FoodItem{" +
                "id=" + id +
                ", mealTypeId=" + mealTypeId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isAvailable=" + isAvailable +
                ", isDelete=" + isDelete +
                '}';
    }
}

