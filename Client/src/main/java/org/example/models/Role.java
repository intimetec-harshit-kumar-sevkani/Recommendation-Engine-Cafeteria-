package org.example.models;

public class Role {
    private int id;
    private String type;
    private boolean isDelete;

    // Constructors
    public Role() {}

    public Role(int id, String type, boolean isDelete) {
        this.id = id;
        this.type = type;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    // toString
    @Override
    public String toString() {
        return "Role{id=" + id + ", type='" + type + "', isDelete=" + isDelete + "}";
    }
}

