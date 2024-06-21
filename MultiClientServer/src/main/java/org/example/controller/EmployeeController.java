package org.example.controller;

import org.example.model.FoodItem;
import org.example.service.FoodItemService;
import org.example.service.VotedItemService;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {

    private VotedItemService votedItemService;

    public EmployeeController() throws SQLException {
        this.votedItemService = new VotedItemService();
    }

    public String voteFoodItem(List<Integer> votedFoodItems) {
        try {
            votedItemService.updateFoodItem(votedFoodItems);
            return "Food item voted successfully.";
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
    }
}
