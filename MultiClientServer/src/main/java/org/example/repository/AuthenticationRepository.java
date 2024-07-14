package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Role;
import org.example.model.User;
import org.example.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationRepository {

    private Connection connection;

    public AuthenticationRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public User findUserByEmailAndName(String email, String name) throws SQLException {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_USER_BY_EMAIL_AND_NAME)) {
            statement.setString(1, email);
            statement.setString(2, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getInt("Id"));
                    user.setRoleId(resultSet.getInt("RoleId"));
                    user.setName(resultSet.getString("Name"));
                    user.setEmail(resultSet.getString("Email"));
                    user.setDelete(resultSet.getBoolean("IsDelete"));
                }
            }
        }

        return user;
    }

    public Role findRoleById(int roleId) throws SQLException {
        Role role = null;
        try (PreparedStatement statement = connection.prepareStatement(SQLQueries.FIND_ROLE_BY_ID)) {
            statement.setInt(1, roleId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    role = new Role();
                    role.setId(resultSet.getInt("Id"));
                    role.setType(resultSet.getString("Type"));
                    role.setDelete(resultSet.getBoolean("IsDelete"));
                }
            }
        }
        return role;
    }
}

