package org.example.service;

import org.example.model.Role;
import org.example.model.User;
import org.example.repository.AuthenticationRepository;

import java.sql.SQLException;

public class AuthenticationService {
    private AuthenticationRepository repository;

    public AuthenticationService() throws SQLException {
        this.repository = new AuthenticationRepository();
    }

    public String authenticate(String email, String name) {
        User user = repository.findUserByEmailAndName(email, name);
        if (user != null) {
            Role role = repository.findRoleById(user.getRoleId());
            if (role != null) {
                return role.getType();
            }
        }
        return "Unknown";
    }
}
