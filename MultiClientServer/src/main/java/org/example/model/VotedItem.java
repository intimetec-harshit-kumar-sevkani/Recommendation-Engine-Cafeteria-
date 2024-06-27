package org.example.model;

import java.util.Date;

public class VotedItem {
    private int id;
    private int foodItemId;
    private int vote;
    private Date date;
    private boolean isPrepared;
    private boolean isDelete;

    public VotedItem() {}

    public VotedItem(int id, int foodItemId, int vote, Date date, boolean isPrepared, boolean isDelete) {
        this.id = id;
        this.foodItemId = foodItemId;
        this.vote = vote;
        this.date = date;
        this.isPrepared = isPrepared;
        this.isDelete = isDelete;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
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


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "VotedItem{" +
                "id=" + id +
                ", foodItemId=" + foodItemId +
                ", vote=" + vote +
                ", date=" + date +
                ", isPrepared=" + isPrepared +
                ", isDelete=" + isDelete +
                '}';
    }


}
