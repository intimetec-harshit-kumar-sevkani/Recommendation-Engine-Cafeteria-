package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProfileRepository {
    private Connection connection;

    public UserProfileRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void insertUserProfile(UserProfile userProfile) throws SQLException {
        String sql = "INSERT INTO UserProfile (UserId, FoodType, SpiceLevel, Originality, SweetTooth) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userProfile.getUserId());
            stmt.setString(2, userProfile.getFoodType());
            stmt.setString(3, userProfile.getSpiceLevel());
            stmt.setString(4, userProfile.getOriginality());
            stmt.setBoolean(5, userProfile.isSweetTooth());

            int rowsInserted = stmt.executeUpdate();
        }
    }
}
