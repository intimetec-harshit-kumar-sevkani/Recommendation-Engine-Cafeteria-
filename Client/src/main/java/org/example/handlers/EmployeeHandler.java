package org.example.handlers;

import com.google.gson.Gson;
import org.example.models.FoodItem;
import org.example.models.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeHandler {

    public static boolean handleEmployeeSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        switch (selection) {
            case "1":
                MessageType voteFoodItem = new MessageType("VOTE_RECOMMENDED_ITEMS");
                String voteFoodItemJson = gson.toJson(voteFoodItem);
                out.println(voteFoodItemJson);
                voteFoodItem(scanner, out, in, gson);
                return false;
            case "2":
                System.out.println("View Food Items...");
                return false;
            case "3":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }
    private static void voteFoodItem(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson) throws IOException {
        List<Integer> votedItemIds = new ArrayList<>();
        System.out.println("Enter the IDs of the food items to vote for");
        String[] inputIds = scanner.nextLine().split(",");
        for (String inputId : inputIds) {
            votedItemIds.add(Integer.parseInt(inputId.trim()));
        }
        String votedItemIdsJson = gson.toJson(votedItemIds);
        out.println(votedItemIdsJson);

        String response = in.readLine();
        System.out.println(response);

    }
}
