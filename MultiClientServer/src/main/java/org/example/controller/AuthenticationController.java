package org.example.controller;

import org.example.DTO.RoleDTO;
import org.example.service.AuthenticationService;
import java.sql.SQLException;

public class AuthenticationController {

    private AuthenticationService authenticationServiceservice;

    public AuthenticationController() throws SQLException {
        this.authenticationServiceservice = new AuthenticationService();
    }

    public RoleDTO login(String email) throws SQLException {
        return authenticationServiceservice.authenticate(email);
    }
}
