package org.example.service;

import org.example.model.Role;
import org.example.DTO.RoleDTO;
import org.example.model.User;
import org.example.repository.AuthenticationRepository;

import java.sql.SQLException;

public class AuthenticationService {
    private AuthenticationRepository repository;

    public AuthenticationService() throws SQLException {
        this.repository = new AuthenticationRepository();
    }

    public RoleDTO authenticate(String email) throws SQLException {
        User user = repository.findUserByEmail(email);
        if (user != null) {
            Role role = repository.findRoleById(user.getRoleId());
            if (role != null) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRole(role.getType());
                roleDTO.setUserId(user.getId());
                return roleDTO;
            }
        }
        return null;
    }
}
