package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.EmployeeController;
import org.example.controller.FoodItemController;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.UserProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeHandlerImpl implements EmployeeHandler{
    private FoodItemController foodItemController;
    private EmployeeController employeeController;
    public EmployeeHandlerImpl() throws SQLException {
        this.foodItemController = new FoodItemController();
        this.employeeController = new EmployeeController();
    }

    private Gson gson = new Gson();

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    public void handleFeedback(BufferedReader in, PrintWriter out) throws IOException {
        String feedbackJson = in.readLine();
        Feedback feedback = gson.fromJson(feedbackJson, Feedback.class);

        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());

        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        if (validFoodItemIds.contains(feedback.getFoodItemId())) {
            String response = employeeController.addFeedback(feedback);
            out.println(response);
        } else {
            out.println("Invalid Food Item Id");
        }
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException {
        String notificationJson = employeeController.getNotification();
        out.println(notificationJson);
    }

    public void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException {
        String mealType = in.readLine();
        if("Invalid Meal Type".equals(mealType))
        {
            out.println("Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        } else {
            String foodItemsJson = employeeController.viewRollOutItem(mealType);
            out.println(foodItemsJson);
            String votedItemIdsJson = in.readLine();
            if("No valid IDs".equals(votedItemIdsJson))
            {
                out.println("No valid IDs entered.");
            }
            else {
                List<Integer> votedItems = gson.fromJson(votedItemIdsJson, new TypeToken<List<Integer>>() {}.getType());
                String response = employeeController.voteFoodItem(votedItems);
                out.println(response);
            }
        }

    }
    public void handleTodayMenuItems(BufferedReader in, PrintWriter out) throws IOException {
        String foodItemsJson = employeeController.viewTodayMenu();
        out.println(foodItemsJson);
    }

    public void handleUserProfile(BufferedReader in, PrintWriter out) throws IOException {

        String userProfileJson = in.readLine();
        UserProfile userProfile = gson.fromJson(userProfileJson, UserProfile.class);
        String response = employeeController.addUserProfile(userProfile);
        out.println(response);
    }
}
