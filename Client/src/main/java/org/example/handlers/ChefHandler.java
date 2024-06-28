package org.example.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.FoodItem;
import org.example.models.MessageType;
import org.example.models.RecommendedDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class ChefHandler {

    public static boolean handleChefSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        switch (selection) {
            case "1":
                MessageType getRecommendedItem = new MessageType("GET_RECOMMENDED_ITEMS");
                String getRecommendedItemJson = gson.toJson(getRecommendedItem);
                out.println(getRecommendedItemJson);
                getRecommendedItem(out, in,scanner, gson);
                return false;
            case "2":
                MessageType viewAllFoodItems = new MessageType("VIEW_ALL_FOOD_ITEMS");
                String viewAllFoodItemsJson = gson.toJson(viewAllFoodItems);
                out.println(viewAllFoodItemsJson);
                viewAllFoodItems(out, in, gson);
                return false;
            case "3":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

    private static void getRecommendedItem(PrintWriter out, BufferedReader in, Scanner scanner, Gson gson) throws IOException {
        RecommendedDTO recommendedDTO = new RecommendedDTO();
        System.out.println("Enter the Meal Type : ");
        recommendedDTO.setMealType(scanner.nextLine());
        System.out.println("Enter Number of Items : ");
        recommendedDTO.setNumberOfItems(scanner.nextInt());
        String recommendedDTOJson = gson.toJson(recommendedDTO);
        out.println(recommendedDTOJson);
        String response = in.readLine();
        List<FoodItem> foodItems = gson.fromJson(response, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }

    private static void viewAllFoodItems(PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        String response = in.readLine();
        List<FoodItem> foodItems = gson.fromJson(response, new TypeToken<List<FoodItem>>(){}.getType());
        foodItems.forEach(System.out::println);
    }

}
