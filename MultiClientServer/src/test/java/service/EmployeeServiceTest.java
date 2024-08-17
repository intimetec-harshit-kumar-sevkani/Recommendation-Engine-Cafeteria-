package service;
import org.example.model.UserProfile;
import org.example.repository.UserProfileRepository;
import org.example.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeService employeeService;
    private UserProfileRepository userProfileRepository;

    @Before
    public void setUp() throws SQLException {
        userProfileRepository = mock(UserProfileRepository.class);
        employeeService = new EmployeeService();
        setField(employeeService, "userProfileRepository", userProfileRepository);
    }

    @Test
    public void testAddUserProfile() throws SQLException {
        UserProfile userProfile = new UserProfile(1, "Vegetarian", "Medium", "Traditional", true);

        employeeService.addUserProfile(userProfile);

        verify(userProfileRepository, times(1)).addUserProfile(userProfile);
    }

    @Test
    public void testUpdateUserProfile() throws SQLException {
        UserProfile userProfile = new UserProfile(1, "Vegetarian", "Medium", "Traditional", true);

        employeeService.updateUserProfile(userProfile);

        verify(userProfileRepository, times(1)).updateUserProfile(userProfile);
    }

    @Test
    public void testGetUserProfile() throws SQLException {
        int userId = 1;
        UserProfile expectedUserProfile = new UserProfile(1, "Vegetarian", "Medium", "Traditional", true);

        when(userProfileRepository.getUserProfile(userId)).thenReturn(expectedUserProfile);

        UserProfile result = employeeService.getUserProfile(userId);

        assertEquals(expectedUserProfile, result);
        verify(userProfileRepository, times(1)).getUserProfile(userId);
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

