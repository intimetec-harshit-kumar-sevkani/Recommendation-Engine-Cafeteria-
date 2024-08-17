package controller;

import org.example.DTO.RoleDTO;
import org.example.controller.AuthenticationController;
import org.example.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private AuthenticationService authenticationService;

    @Before
    public void setUp() throws SQLException {
        authenticationService = mock(AuthenticationService.class);
        authenticationController = new AuthenticationController();
        setField(authenticationController, "authenticationServiceservice", authenticationService);
    }

    @Test
    public void testLogin_success() throws SQLException {
        String email = "test@example.com";
        RoleDTO roleDTO = new RoleDTO();

        when(authenticationService.authenticate(email)).thenReturn(roleDTO);

        RoleDTO result = authenticationController.login(email);

        verify(authenticationService, times(1)).authenticate(email);
        assertEquals(roleDTO, result);
    }

    @Test(expected = SQLException.class)
    public void testLogin_failure() throws SQLException {
        String email = "test@example.com";

        when(authenticationService.authenticate(email)).thenThrow(new SQLException("Authentication failed"));

        authenticationController.login(email);
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

