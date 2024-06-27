package org.example.service;

import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.repository.FeedbackRepository;
import org.example.repository.FoodItemRepository;

import java.sql.SQLException;

public class FeedbackService {

    private FeedbackRepository feedbackRepository;

    public FeedbackService() throws SQLException {
        this.feedbackRepository = new FeedbackRepository();
    }

    public void addFeedback(Feedback feedback) throws SQLException {
        feedbackRepository.addFeedback(feedback);
    }
}
