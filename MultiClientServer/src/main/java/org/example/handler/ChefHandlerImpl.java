package org.example.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.FoodItemController;
import org.example.controller.NotificationController;
import org.example.model.FoodItem;
import org.example.model.MessageType;
import org.example.model.RecommendedDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChefHandlerImpl implements ChefHandler, RoleHandler{
    private ChefController chefController;

    private FoodItemController foodItemController;

    private NotificationController notificationController;

    public ChefHandlerImpl() throws SQLException {
        this.chefController = new ChefController();
        this.foodItemController = new FoodItemController();
        this.notificationController = new NotificationController();
    }

    private Gson gson = new Gson();


    @Override
    public void handleRequest(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "GET_RECOMMENDED_ITEMS":
                handleRecommendationFoodItems(in, out);
                break;
            case "GET_VOTED_ITEMS":
                handleRollOutItems(in, out);
                break;
            case "VIEW_ALL_FOOD_ITEMS":
                handleViewAllFoodItems(out);
                break;
            case "VIEW_NOTIFICATION":
                handleNotifications(in, out);
                break;
            case "VIEW_DISCARD_ITEMS":
                handleDiscardMenuItems(in, out);
                break;
            default:
                System.out.println("--------------");
        }
    }




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

    public void handleDiscardMenuItems(BufferedReader in, PrintWriter out) throws IOException {
             String discardItemJson = chefController.ViewDiscardItem();
             out.println(discardItemJson);
             String response = in.readLine();
        if("Discard_Food_Items".equals(response))
        {
           String foodItemIdsJson = in.readLine();
            List<Integer> foodItemIds = gson.fromJson(foodItemIdsJson, new TypeToken<List<Integer>>() {
            }.getType());

            String discardResponse = chefController.DiscardItem(foodItemIds);
            out.println(discardResponse);
        }
        if("Send_Notification".equals(response))
        {
            String foodItemJson = in.readLine();
            List<FoodItem> foodItems = gson.fromJson(foodItemJson, new TypeToken<List<FoodItem>>() {
            }.getType());
            String notificationResponse =  notificationController.sendNotification(foodItems);
            out.println(notificationResponse);
        }

    }
}
