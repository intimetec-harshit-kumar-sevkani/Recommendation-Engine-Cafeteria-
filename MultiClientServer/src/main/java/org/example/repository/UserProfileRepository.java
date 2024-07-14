package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.UserProfile;
import org.example.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*

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

    public UserProfile getUserProfile(int userId) throws SQLException {
        String sql = "SELECT FoodType, SpiceLevel, Originality, SweetTooth FROM UserProfile WHERE UserId = ?";
        UserProfile userProfile = new UserProfile();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRepository {
    private Connection connection;

    public UserProfileRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void insertUserProfile(UserProfile userProfile) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INSERT_USER_PROFILE)) {
            stmt.setInt(1, userProfile.getUserId());
            stmt.setString(2, userProfile.getFoodType());
            stmt.setString(3, userProfile.getSpiceLevel());
            stmt.setString(4, userProfile.getOriginality());
            stmt.setBoolean(5, userProfile.isSweetTooth());

            int rowsInserted = stmt.executeUpdate();
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

