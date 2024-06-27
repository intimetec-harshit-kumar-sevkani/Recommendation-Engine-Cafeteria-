package org.example.models;

public class NotificationType {
    private int id;
    private String type;
    private int validFor;
    private boolean isDelete;

    public NotificationType() {}

    public NotificationType(int id, String type, int validFor, boolean isDelete) {
        this.id = id;
        this.type = type;
        this.validFor = validFor;
        this.isDelete = isDelete;
    }

    // Getters and Setters
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

    public int getValidFor() {
        return validFor;
    }

    public void setValidFor(int validFor) {
        this.validFor = validFor;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "NotificationType{id=" + id + ", type='" + type + "', validFor=" + validFor + ", isDelete=" + isDelete + "}";
    }
}

