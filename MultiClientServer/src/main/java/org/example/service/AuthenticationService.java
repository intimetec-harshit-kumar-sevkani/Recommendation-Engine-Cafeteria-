package org.example.service;

import org.example.model.Role;
import org.example.model.RoleMessage;
import org.example.model.User;
import org.example.repository.AuthenticationRepository;

import java.sql.SQLException;

public class AuthenticationService {
    private AuthenticationRepository repository;

    public AuthenticationService() throws SQLException {
        this.repository = new AuthenticationRepository();
    }

    public RoleMessage authenticate(String email, String name) {
        User user = repository.findUserByEmailAndName(email, name);
        if (user != null) {
            Role role = repository.findRoleById(user.getRoleId());
            if (role != null) {
                RoleMessage roleMessage = new RoleMessage();
                roleMessage.setRole(role.getType());
                roleMessage.setUserId(user.getId());
                return roleMessage;
            }
        }
        return null;
    }
}
