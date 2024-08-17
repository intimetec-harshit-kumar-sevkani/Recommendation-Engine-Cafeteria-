package org.example.model;

public class User {
    private int id;
    private int roleId;
    private String name;
    private String email;
    private boolean isDelete;


    public User() {
    }

    public User(int id, int roleId, String name, String email, boolean isDelete) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.email = email;
        this.isDelete = isDelete;
    }


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
}