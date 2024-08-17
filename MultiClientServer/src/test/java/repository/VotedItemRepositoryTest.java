package repository;

import org.example.repository.VotedItemRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class VotedItemRepositoryTest {

    private VotedItemRepository votedItemRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        votedItemRepository = new VotedItemRepository();
        setField(votedItemRepository, "connection", connection);
    }


    @Test
    public void testVoteFoodItems_Successful() throws SQLException {
        List<Integer> foodItemIds = Arrays.asList(1, 2, 3);

        when(connection.prepareStatement(SQLQueries.UPDATE_VOTE)).thenReturn(preparedStatement);

        votedItemRepository.voteFoodItems(foodItemIds);

        verify(connection, times(1)).prepareStatement(SQLQueries.UPDATE_VOTE);
        verify(preparedStatement, times(foodItemIds.size())).setInt(eq(1), anyInt());
        verify(preparedStatement, times(1)).executeBatch();
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

