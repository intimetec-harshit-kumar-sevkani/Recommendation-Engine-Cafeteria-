package org.example.model;

public class RoleMessage {
    public String role;

    public int userId;

    public RoleMessage(String role, int userId) {
        this.role = role;
        this.userId = userId;
    }

    public RoleMessage() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

