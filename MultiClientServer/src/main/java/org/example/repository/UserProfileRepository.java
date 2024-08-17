package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.UserProfile;
import org.example.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRepository {
    private Connection connection;

    public UserProfileRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void addUserProfile(UserProfile userProfile) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INSERT_USER_PROFILE)) {
            stmt.setInt(1, userProfile.getUserId());
            stmt.setString(2, userProfile.getFoodType());
            stmt.setString(3, userProfile.getSpiceLevel());
            stmt.setString(4, userProfile.getOriginality());
            stmt.setBoolean(5, userProfile.isSweetTooth());

            int rowsInserted = stmt.executeUpdate();
        }
    }

    public void updateUserProfile(UserProfile userProfile) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.UPDATE_USER_PROFILE)) {
            stmt.setString(1, userProfile.getFoodType());
            stmt.setString(2, userProfile.getSpiceLevel());
            stmt.setString(3, userProfile.getOriginality());
            stmt.setBoolean(4, userProfile.isSweetTooth());
            stmt.setInt(5, userProfile.getUserId());

            int rowsUpdated = stmt.executeUpdate();
        }
    }

    public UserProfile getUserProfile(int userId) throws SQLException {
        UserProfile userProfile = new UserProfile();

        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.SELECT_USER_PROFILE)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userProfile.setFoodType(rs.getString("FoodType"));
                    userProfile.setSpiceLevel(rs.getString("SpiceLevel"));
                    userProfile.setOriginality(rs.getString("Originality"));
                    userProfile.setSweetTooth(rs.getBoolean("SweetTooth"));
                }
            }
        }
        return userProfile;
    }
}

