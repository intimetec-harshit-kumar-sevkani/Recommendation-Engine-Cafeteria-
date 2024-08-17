package repository;

import org.example.repository.NotificationRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NotificationRepositoryTest {

    private NotificationRepository notificationRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        notificationRepository = new NotificationRepository();
        setField(notificationRepository, "connection", connection);
    }

    @Test
    public void testGetNotificationTypeId_Successful() throws SQLException {
        String notificationType = "NewItem";

        when(connection.prepareStatement(SQLQueries.SELECT_NOTIFICATION_TYPE_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("Id")).thenReturn(5);

        int notificationTypeId = notificationRepository.getNotificationTypeId(notificationType);

        verify(connection, times(1)).prepareStatement(SQLQueries.SELECT_NOTIFICATION_TYPE_ID);
        verify(preparedStatement, times(1)).setString(1, notificationType);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).getInt("Id");

        assertEquals(5, notificationTypeId);
    }

    @Test
    public void testAddNotification_Successful() throws SQLException {
        int notificationTypeId = 1;
        String message = "Dinner: Pizza";

        when(connection.prepareStatement(SQLQueries.INSERT_NOTIFICATION)).thenReturn(preparedStatement);

        notificationRepository.addNotification(notificationTypeId, message);

        verify(connection, times(1)).prepareStatement(SQLQueries.INSERT_NOTIFICATION);
        verify(preparedStatement, times(1)).setInt(1, notificationTypeId);
        verify(preparedStatement, times(1)).setString(2, message);
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

