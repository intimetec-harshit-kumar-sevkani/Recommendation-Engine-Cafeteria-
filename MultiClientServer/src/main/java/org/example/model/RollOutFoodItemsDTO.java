package org.example.model;

import java.math.BigDecimal;

public class RollOutFoodItemsDTO {
    private int id;
    private int mealTypeId;
    private String name;
    private BigDecimal price;

    private int vote;
    private boolean isAvailable;
    private boolean isDelete;

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
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
        return "RollOutFoodItemsDTO{" +
                "id=" + id +
                ", mealTypeId=" + mealTypeId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", vote=" + vote +
                ", isAvailable=" + isAvailable +
                ", isDelete=" + isDelete +
                '}';
    }

}
