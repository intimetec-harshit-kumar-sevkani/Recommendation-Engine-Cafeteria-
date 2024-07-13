package org.example.service;

import org.example.model.FoodItem;
import org.example.model.UserProfile;
import org.example.repository.FeedbackRepository;
import org.example.repository.UserProfileRepository;

import java.sql.SQLException;

public class EmployeeService {

    private UserProfileRepository userProfileRepository;

    public EmployeeService() throws SQLException {
        this.userProfileRepository = new UserProfileRepository();
    }

    public void addUserProfile(UserProfile userProfile) throws SQLException {
        userProfileRepository.insertUserProfile(userProfile);
    }
}
