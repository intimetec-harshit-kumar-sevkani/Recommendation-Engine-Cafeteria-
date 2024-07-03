package org.example.controller;

import org.example.model.RoleMessageDTO;
import org.example.service.AuthenticationService;
import java.sql.SQLException;

public class AuthenticationController {

    private AuthenticationService service;

    public AuthenticationController() throws SQLException {
        this.service = new AuthenticationService();
    }

    public RoleMessageDTO login(String email, String name) throws SQLException {
        return service.authenticate(email, name);
    }
}
