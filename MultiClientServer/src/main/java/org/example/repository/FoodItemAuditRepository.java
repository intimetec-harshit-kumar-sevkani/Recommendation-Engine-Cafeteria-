package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.model.FoodItem;
import org.example.model.FoodItemRating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodItemAuditRepository {
    private Connection connection;

    public FoodItemAuditRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void insertIntoFoodAudit(int foodItemId) throws SQLException {
        String sql = "INSERT INTO fooditemaudit (FoodItemId, Rating, Vote, Sentiment, Prepared) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, foodItemId);
            stmt.setDouble(2, 3.0);
            stmt.setInt(3, 0);
            stmt.setDouble(4, 3.0);
            stmt.setInt(5, 0);
            stmt.executeUpdate();
        }
    }
}
