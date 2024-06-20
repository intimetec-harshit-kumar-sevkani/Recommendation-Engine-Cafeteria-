package org.example.models;

public class User {
    private int id;
    private int roleId;
    private String name;
    private String email;
    private boolean isDelete;

    // Constructors
    public User() {}

    public User(int id, int roleId, String name, String email, boolean isDelete) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.email = email;
        this.isDelete = isDelete;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return "User{id=" + id + ", roleId=" + roleId + ", name='" + name + "', email='" + email + "', isDelete=" + isDelete + "}";
    }
}

