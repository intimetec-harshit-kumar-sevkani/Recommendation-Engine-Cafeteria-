package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.FoodItemController;
import org.example.model.FoodItem;
import org.example.model.RecommendedDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChefHandlerImpl implements ChefHandler{
    private ChefController chefController;

    private FoodItemController foodItemController;

    public ChefHandlerImpl() throws SQLException {
        this.chefController = new ChefController();
        this.foodItemController = new FoodItemController();
    }

    private Gson gson = new Gson();
    public void handleRecommendationFoodItems(BufferedReader in,PrintWriter out) throws IOException {
        String recommendedDTOJson = in.readLine();
        if("Invalid Meal Type".equals(recommendedDTOJson))
        {
            out.println("Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        }
        else {
            RecommendedDTO recommendedDTO = gson.fromJson(recommendedDTOJson, RecommendedDTO.class);
            String foodItemsJson = chefController.getRecommendedFoodItems(recommendedDTO);
            out.println(foodItemsJson);
        }
    }

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    public void handleRollOutItems(BufferedReader in, PrintWriter out) throws IOException {
          String mealType = in.readLine();
        if("Invalid Meal Type".equals(mealType))
        {
            out.println("Invalid Meal Type. Valid Meal Types are: Breakfast, Lunch, and Dinner.");
        }
        else {
            String response = chefController.getVotedItems(mealType);
            out.println(response);
            String rollItemIdsJson = in.readLine();

            List<Integer> rollOutItems = gson.fromJson(rollItemIdsJson, new TypeToken<List<Integer>>() {
            }.getType());

            String foodItemsJson = foodItemController.getAllFoodItems();
            List<FoodItem> foodItems = gson.fromJson(foodItemsJson, new TypeToken<List<FoodItem>>() {
            }.getType());

            Set<Integer> validFoodItemIds = foodItems.stream().map(FoodItem::getId).collect(Collectors.toSet());

            boolean isValid = rollOutItems.stream().allMatch(validFoodItemIds::contains);

            if (isValid) {
                String rollOutResponse = chefController.rollOutFoodItems(rollOutItems);
                out.println(rollOutResponse);
            } else {
                out.println("Invalid Food Item Id(s) in the list.");
            }
        }
    }
    public void handleNotifications(BufferedReader in, PrintWriter out) {
        String notificationJson = chefController.getNotification();
        out.println(notificationJson);
    }
}
