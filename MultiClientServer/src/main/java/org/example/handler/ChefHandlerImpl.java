package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.FoodItemController;
import org.example.model.Notification;
import org.example.model.RecommendedDTO;
import org.example.model.RollOutFoodItemsDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ChefHandlerImpl implements ChefHandler{

    /*
    ("GET_RECOMMENDED_ITEMS")
("GET_VOTED_ITEMS")
("VIEW_ALL_FOOD_ITEMS")
     */

    private ChefController chefController;

    private FoodItemController foodItemController;

    public ChefHandlerImpl() throws SQLException {
        this.chefController = new ChefController();
        this.foodItemController = new FoodItemController();
    }

    private Gson gson = new Gson();
    public void handleRecommendationFoodItems(BufferedReader in,PrintWriter out) throws IOException {
        String recommendedDTOJson = in.readLine();
        RecommendedDTO recommendedDTO= gson.fromJson(recommendedDTOJson, RecommendedDTO.class);
        String foodItemsJson = chefController.getTopFoodItems(recommendedDTO);
        out.println(foodItemsJson);
    }

    public void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    public void handleRollOutItems(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        String mealType = in.readLine();
        List<RollOutFoodItemsDTO> rollOutFoodItemsDTOList = chefController.getRollOutFoodItemsDTOList(mealType);
        String rollOutFoodItemJson = gson.toJson(rollOutFoodItemsDTOList);
        out.println(rollOutFoodItemJson);
        String rollItemIdsJson = in.readLine();
        List<Integer> rollOutItems = gson.fromJson(rollItemIdsJson, new TypeToken<List<Integer>>(){}.getType());
        chefController.rollOutItems(rollOutItems);
        out.println("Food Item Roll Out Successfully");
    }
    public void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        List<Notification> notifications = chefController.getNotification();
        String notificationJson = gson.toJson(notifications);
        out.println(notificationJson);
    }
}