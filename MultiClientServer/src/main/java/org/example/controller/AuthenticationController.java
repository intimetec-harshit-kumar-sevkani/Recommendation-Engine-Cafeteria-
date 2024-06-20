package org.example.controller;

import org.example.service.AuthenticationService;

import java.sql.SQLException;

public class AuthenticationController {

    private AuthenticationService service;

    public AuthenticationController() throws SQLException {
        this.service = new AuthenticationService();
    }

    public String login(String email, String name) {
        return service.authenticate(email, name);
    }
}
