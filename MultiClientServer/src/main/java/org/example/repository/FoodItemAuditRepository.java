package org.example.repository;

import org.example.Config.SQLDataSourceConfig;
import org.example.util.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FoodItemAuditRepository {
    private Connection connection;

    public FoodItemAuditRepository() throws SQLException {
        this.connection = SQLDataSourceConfig.getConnection();
    }

    public void addFoodAudit(int foodItemId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SQLQueries.INSERT_FOOD_AUDIT)) {
            stmt.setInt(1, foodItemId);
            stmt.setDouble(2, 3.0);
            stmt.setInt(3, 0);
            stmt.setDouble(4, 3.0);
            stmt.setInt(5, 0);
            stmt.executeUpdate();
        }
    }
}
