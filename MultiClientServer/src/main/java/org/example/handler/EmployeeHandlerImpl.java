package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.EmployeeController;
import org.example.controller.FoodItemController;
import org.example.model.Feedback;
import org.example.model.FoodItem;
import org.example.model.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeHandlerImpl implements EmployeeHandler{

    /*
    ("VIEW_ALL_FOOD_ITEMS") handleViewAllFoodItems
("GIVE_FEEDBACK")  handleFeedback
("VOTE_RECOMMENDED_ITEMS") handleVotedFoodItems
("VIEW_NOTIFICATION") handleNotifications
     */

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
      /*  String feedbackJson = in.readLine();
        Feedback feedback = gson.fromJson(feedbackJson, Feedback.class);
        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>(){}.getType());
        employeeController.addFeedback(feedback);
        out.println("Feedback added Successfully");*/
        String feedbackJson = in.readLine();
        Feedback feedback = gson.fromJson(feedbackJson, Feedback.class);

        // Get all food items
        String foodItemsJson = foodItemController.getAllFoodItems();
        List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {}.getType());

        // Collect valid food item IDs
        Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

        // Validate feedback.foodItemId
        if (validFoodItemIds.contains(feedback.getFoodItemId())) {
            employeeController.addFeedback(feedback);
            out.println("Feedback added Successfully");
        } else {
            out.println("Invalid Food Item Id");
        }
    }

    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        List<Notification> notifications = employeeController.getNotification();
        String notificationJson = gson.toJson(notifications);
        out.println(notificationJson);
    }

    public void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        String mealType = in.readLine();
        List<FoodItem> foodItems = employeeController.viewVoteItem(mealType);
        String foodItemsJson = gson.toJson(foodItems);
        out.println(foodItemsJson);
        String votedItemIdsJson = in.readLine();
        List<Integer> votedItems = gson.fromJson(votedItemIdsJson, new TypeToken<List<Integer>>(){}.getType());
        employeeController.voteFoodItem(votedItems);
        out.println("Food Item Voted Successfully");

    }


    public void handleTodayMenuItems(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        List<FoodItem> foodItems = employeeController.viewTodayMenu();
        String foodItemsJson = gson.toJson(foodItems);
        out.println(foodItemsJson);

    }

}
