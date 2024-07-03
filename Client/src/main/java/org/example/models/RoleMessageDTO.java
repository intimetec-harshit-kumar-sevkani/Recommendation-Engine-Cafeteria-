package org.example.models;

public class RoleMessageDTO {
    public String role;

    public int userId;

    public RoleMessageDTO(String role, int userId) {
        this.role = role;
        this.userId = userId;
    }

    public RoleMessageDTO() {
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
