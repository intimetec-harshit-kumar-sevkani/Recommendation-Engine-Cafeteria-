package org.example.DTO;

public class RoleDTO {
    public String role;
    public int userId;

    public RoleDTO(String role, int userId) {
        this.role = role;
        this.userId = userId;
    }

    public RoleDTO() {
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

