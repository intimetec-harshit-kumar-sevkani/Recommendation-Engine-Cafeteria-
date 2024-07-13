package org.example.model;

import java.time.LocalDate;

public class DiscardItem {
    private int id;
    private int foodItemId;
    private LocalDate date;

    public DiscardItem() {
    }

    public DiscardItem(int id, int foodItemId, LocalDate date) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
