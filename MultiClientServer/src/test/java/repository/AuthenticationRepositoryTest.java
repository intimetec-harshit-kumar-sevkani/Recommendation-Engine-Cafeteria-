package repository;

import org.example.model.Role;
import org.example.model.User;
import org.example.repository.AuthenticationRepository;
import org.example.util.SQLQueries;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AuthenticationRepositoryTest {

    private AuthenticationRepository authenticationRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        authenticationRepository = new AuthenticationRepository();
        setField(authenticationRepository, "connection", connection);
    }

    @Test
    public void testFindUserByEmail_UserExists() throws SQLException {
        String email = "test@example.com";
        when(connection.prepareStatement(SQLQueries.FIND_USER_BY_EMAIL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("Id")).thenReturn(1);
        when(resultSet.getInt("RoleId")).thenReturn(2);
        when(resultSet.getString("Name")).thenReturn("Test User");
        when(resultSet.getString("Email")).thenReturn(email);
        when(resultSet.getBoolean("IsDelete")).thenReturn(false);

        User user = authenticationRepository.findUserByEmail(email);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals(2, user.getRoleId());
        assertEquals("Test User", user.getName());
        assertEquals(email, user.getEmail());
        assertFalse(user.isDelete());

        verify(connection, times(1)).prepareStatement(SQLQueries.FIND_USER_BY_EMAIL);
        verify(preparedStatement, times(1)).setString(1, email);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
    }

    @Test
    public void testFindUserByEmail_UserDoesNotExist() throws SQLException {
        String email = "nonexistent@example.com";
        when(connection.prepareStatement(SQLQueries.FIND_USER_BY_EMAIL)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = authenticationRepository.findUserByEmail(email);

        assertNull(user);

        verify(connection, times(1)).prepareStatement(SQLQueries.FIND_USER_BY_EMAIL);
        verify(preparedStatement, times(1)).setString(1, email);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
    }

    @Test
    public void testFindRoleById_RoleExists() throws SQLException {
        int roleId = 2;
        when(connection.prepareStatement(SQLQueries.FIND_ROLE_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getInt("Id")).thenReturn(roleId);
        when(resultSet.getString("Type")).thenReturn("Admin");
        when(resultSet.getBoolean("IsDelete")).thenReturn(false);

        Role role = authenticationRepository.findRoleById(roleId);

        assertNotNull(role);
        assertEquals(roleId, role.getId());
        assertEquals("Admin", role.getType());
        assertFalse(role.isDelete());

        verify(connection, times(1)).prepareStatement(SQLQueries.FIND_ROLE_BY_ID);
        verify(preparedStatement, times(1)).setInt(1, roleId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
    }

    @Test
    public void testFindRoleById_RoleDoesNotExist() throws SQLException {
        int roleId = 99;
        when(connection.prepareStatement(SQLQueries.FIND_ROLE_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Role role = authenticationRepository.findRoleById(roleId);

        assertNull(role);

        verify(connection, times(1)).prepareStatement(SQLQueries.FIND_ROLE_BY_ID);
        verify(preparedStatement, times(1)).setInt(1, roleId);
        verify(preparedStatement, times(1)).executeQuery();
        verify(resultSet, times(1)).next();
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

