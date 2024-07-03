package org.example.model;

public class MealType {
    private int id;
    private String type;
    private boolean isDelete;

    public MealType() {}

    public MealType(int id, String type, boolean isDelete) {
        this.id = id;
        this.type = type;
        this.isDelete = isDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "MealType{id=" + id + ", type='" + type + "', isDelete=" + isDelete + "}";
    }
}
