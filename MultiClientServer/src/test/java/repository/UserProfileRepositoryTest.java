package repository;

import org.example.model.UserProfile;
import org.example.repository.UserProfileRepository;
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

public class UserProfileRepositoryTest {

    private UserProfileRepository userProfileRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        userProfileRepository = new UserProfileRepository();
        setField(userProfileRepository, "connection", connection);
    }

    @Test
    public void testAddUserProfile_Successful() throws SQLException {
        UserProfile userProfile = new UserProfile(1, "Vegetarian", "Medium", "Traditional", true);

        when(connection.prepareStatement(SQLQueries.INSERT_USER_PROFILE)).thenReturn(preparedStatement);

        userProfileRepository.addUserProfile(userProfile);

        verify(connection, times(1)).prepareStatement(SQLQueries.INSERT_USER_PROFILE);
        verify(preparedStatement, times(1)).setInt(1, userProfile.getUserId());
        verify(preparedStatement, times(1)).setString(2, userProfile.getFoodType());
        verify(preparedStatement, times(1)).setString(3, userProfile.getSpiceLevel());
        verify(preparedStatement, times(1)).setString(4, userProfile.getOriginality());
        verify(preparedStatement, times(1)).setBoolean(5, userProfile.isSweetTooth());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateUserProfile_Successful() throws SQLException {
        UserProfile userProfile = new UserProfile(1, "Vegan", "Hot", "Modern", false);

        when(connection.prepareStatement(SQLQueries.UPDATE_USER_PROFILE)).thenReturn(preparedStatement);

        userProfileRepository.updateUserProfile(userProfile);

        verify(connection, times(1)).prepareStatement(SQLQueries.UPDATE_USER_PROFILE);
        verify(preparedStatement, times(1)).setString(1, userProfile.getFoodType());
        verify(preparedStatement, times(1)).setString(2, userProfile.getSpiceLevel());
        verify(preparedStatement, times(1)).setString(3, userProfile.getOriginality());
        verify(preparedStatement, times(1)).setBoolean(4, userProfile.isSweetTooth());
        verify(preparedStatement, times(1)).setInt(5, userProfile.getUserId());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetUserProfile_Successful() throws SQLException {
        int userId = 1;
        when(connection.prepareStatement(SQLQueries.SELECT_USER_PROFILE)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("FoodType")).thenReturn("Vegetarian");
        when(resultSet.getString("SpiceLevel")).thenReturn("Medium");
        when(resultSet.getString("Originality")).thenReturn("Traditional");
        when(resultSet.getBoolean("SweetTooth")).thenReturn(true);

        UserProfile userProfile = userProfileRepository.getUserProfile(userId);

        verify(connection, times(1)).prepareStatement(SQLQueries.SELECT_USER_PROFILE);
        verify(preparedStatement, times(1)).setInt(1, userId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
        assertEquals("Vegetarian", userProfile.getFoodType());
        assertEquals("Medium", userProfile.getSpiceLevel());
        assertEquals("Traditional", userProfile.getOriginality());
        assertEquals(true, userProfile.isSweetTooth());
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
