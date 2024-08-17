package service;

import org.example.model.Feedback;
import org.example.repository.FeedbackRepository;
import org.example.service.FeedbackService;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    private FeedbackService feedbackService;
    private FeedbackRepository feedbackRepository;

    @Before
    public void setUp() throws SQLException {
        feedbackRepository = mock(FeedbackRepository.class);
        feedbackService = new FeedbackService();
        setField(feedbackService, "feedbackRepository", feedbackRepository);
    }

    @Test
    public void testAddFeedback() throws SQLException {
        Feedback feedback = new Feedback();

        feedbackService.addFeedback(feedback);

        verify(feedbackRepository, times(1)).addFeedback(feedback);
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
