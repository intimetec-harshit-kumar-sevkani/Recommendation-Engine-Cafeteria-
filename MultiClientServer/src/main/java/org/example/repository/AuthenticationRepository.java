package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.Role;
import org.example.model.User;

import java.sql.*;

public class AuthenticationRepository {

    private Connection connection;

    public AuthenticationRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public User findUserByEmailAndName(String email, String name) {
        User user = null;
        String query = "SELECT * FROM Users WHERE Email = ? AND Name = ? AND IsDelete = FALSE";

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, name);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("Id"));
                user.setRoleId(resultSet.getInt("RoleId"));
                user.setName(resultSet.getString("Name"));
                user.setEmail(resultSet.getString("Email"));
                user.setDelete(resultSet.getBoolean("IsDelete"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public Role findRoleById(int roleId) {
        Role role = null;
        String query = "SELECT * FROM Roles WHERE Id = ? AND IsDelete = FALSE";

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roleId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getInt("Id"));
                role.setType(resultSet.getString("Type"));
                role.setDelete(resultSet.getBoolean("IsDelete"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }
}
