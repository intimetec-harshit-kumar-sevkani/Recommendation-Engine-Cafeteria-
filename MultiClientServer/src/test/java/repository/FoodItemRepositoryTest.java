package repository;

import org.example.model.FoodItem;
import org.example.repository.FoodItemRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FoodItemRepositoryTest {

    private FoodItemRepository foodItemRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        foodItemRepository = new FoodItemRepository();
        setField(foodItemRepository, "connection", connection);
    }

    @Test
    public void testDeleteFoodItem_Successful() throws SQLException {
        int foodItemId = 1;

        when(connection.prepareStatement(SQLQueries.DELETE_FOOD_ITEM)).thenReturn(preparedStatement);

        foodItemRepository.deleteFoodItem(foodItemId);

        verify(connection, times(1)).prepareStatement(SQLQueries.DELETE_FOOD_ITEM);
        verify(preparedStatement, times(1)).setInt(1, foodItemId);
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testAddFoodItem_Successful() throws SQLException {
        FoodItem foodItem = new FoodItem(1, "Pizza", new BigDecimal("9.99"), true, false, "Main Course", "Mild", "Italian", true);

        when(connection.prepareStatement(SQLQueries.INSERT_FOOD_ITEM, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(10);

        int generatedId = foodItemRepository.addFoodItem(foodItem);

        verify(connection, times(1)).prepareStatement(SQLQueries.INSERT_FOOD_ITEM, Statement.RETURN_GENERATED_KEYS);
        verify(preparedStatement, times(1)).setInt(1, foodItem.getMealTypeId());
        verify(preparedStatement, times(1)).setString(2, foodItem.getName());
        verify(preparedStatement, times(1)).setBigDecimal(3, foodItem.getPrice());
        verify(preparedStatement, times(1)).setBoolean(4, foodItem.isAvailable());
        verify(preparedStatement, times(1)).setBoolean(5, foodItem.isDelete());
        verify(preparedStatement, times(1)).setString(6, foodItem.getFoodType());
        verify(preparedStatement, times(1)).setString(7, foodItem.getSpiceLevel());
        verify(preparedStatement, times(1)).setString(8, foodItem.getOriginality());
        verify(preparedStatement, times(1)).setBoolean(9, foodItem.isSweetTooth());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt(1);

        assertEquals(10, generatedId);
    }

    @Test
    public void testUpdateFoodItem_Successful() throws SQLException {
        FoodItem foodItem = new FoodItem(1, "Pizza", new BigDecimal("9.99"), true, false, "Main Course", "Mild", "Italian", true);
        foodItem.setId(1);

        when(connection.prepareStatement(SQLQueries.UPDATE_FOOD_ITEM)).thenReturn(preparedStatement);

        foodItemRepository.updateFoodItem(foodItem);

        verify(connection, times(1)).prepareStatement(SQLQueries.UPDATE_FOOD_ITEM);
        verify(preparedStatement, times(1)).setInt(1, foodItem.getMealTypeId());
        verify(preparedStatement, times(1)).setString(2, foodItem.getName());
        verify(preparedStatement, times(1)).setBigDecimal(3, foodItem.getPrice());
        verify(preparedStatement, times(1)).setBoolean(4, foodItem.isAvailable());
        verify(preparedStatement, times(1)).setBoolean(5, foodItem.isDelete());
        verify(preparedStatement, times(1)).setString(6, foodItem.getFoodType());
        verify(preparedStatement, times(1)).setString(7, foodItem.getSpiceLevel());
        verify(preparedStatement, times(1)).setString(8, foodItem.getOriginality());
        verify(preparedStatement, times(1)).setBoolean(9, foodItem.isSweetTooth());
        verify(preparedStatement, times(1)).setInt(10, foodItem.getId());
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

