package org.example.model;

public class LoginMessage {
    private String email;
    private String name;

    public LoginMessage(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}

