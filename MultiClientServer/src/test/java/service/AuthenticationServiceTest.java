package service;

import org.example.DTO.RoleDTO;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.AuthenticationRepository;
import org.example.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private AuthenticationRepository repository;

    @Before
    public void setUp() throws SQLException {
        repository = mock(AuthenticationRepository.class);
        authenticationService = new AuthenticationService();
        setField(authenticationService, "repository", repository);
    }

    @Test
    public void testAuthenticate_success() throws SQLException {
        String email = "test@example.com";
        User user = new User(1, 2, "Test User", email, false); // Updated constructor parameters
        Role role = new Role(2, "Admin", false); // Updated constructor parameters

        when(repository.findUserByEmail(email)).thenReturn(user);
        when(repository.findRoleById(2)).thenReturn(role);

        RoleDTO result = authenticationService.authenticate(email);

        assertEquals("Admin", result.getRole());
        assertEquals(1, result.getUserId());
    }

    @Test
    public void testAuthenticate_userNotFound() throws SQLException {
        String email = "test@example.com";

        when(repository.findUserByEmail(email)).thenReturn(null);

        RoleDTO result = authenticationService.authenticate(email);

        assertNull(result);
    }

    @Test
    public void testAuthenticate_roleNotFound() throws SQLException {
        String email = "test@example.com";
        User user = new User(1, 2, "Test User", email, false);

        when(repository.findUserByEmail(email)).thenReturn(user);
        when(repository.findRoleById(2)).thenReturn(null);

        RoleDTO result = authenticationService.authenticate(email);

        assertNull(result);
    }

    @Test(expected = SQLException.class)
    public void testAuthenticate_sqlException() throws SQLException {
        String email = "test@example.com";

        when(repository.findUserByEmail(email)).thenThrow(new SQLException("Database error"));

        authenticationService.authenticate(email);
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