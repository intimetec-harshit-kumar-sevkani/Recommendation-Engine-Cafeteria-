package repository;

import org.example.repository.FoodItemAuditRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class FoodItemAuditRepositoryTest {

    private FoodItemAuditRepository foodItemAuditRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);

        foodItemAuditRepository = new FoodItemAuditRepository();
        setField(foodItemAuditRepository, "connection", connection);
    }

    @Test
    public void testAddFoodAudit_Successful() throws SQLException {
        int foodItemId = 1;

        when(connection.prepareStatement(SQLQueries.INSERT_FOOD_AUDIT)).thenReturn(preparedStatement);

        foodItemAuditRepository.addFoodAudit(foodItemId);

        verify(connection, times(1)).prepareStatement(SQLQueries.INSERT_FOOD_AUDIT);
        verify(preparedStatement, times(1)).setInt(1, foodItemId);
        verify(preparedStatement, times(1)).setDouble(2, 3.0);
        verify(preparedStatement, times(1)).setInt(3, 1);
        verify(preparedStatement, times(1)).setDouble(4, 3.0);
        verify(preparedStatement, times(1)).setInt(5, 0);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

