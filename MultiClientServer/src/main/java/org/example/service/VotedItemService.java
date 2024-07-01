package org.example.service;

import org.example.model.FoodItem;
import org.example.model.RollOutFoodItemsDTO;
import org.example.repository.VotedItemRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VotedItemService {

    private VotedItemRepository votedItemRepository;

    public VotedItemService() throws SQLException {
        this.votedItemRepository = new VotedItemRepository();
    }

    public void updateFoodItem(List<Integer> votedItemIds) throws SQLException {
        votedItemRepository.voteFoodItems(votedItemIds);
      /*  List<FoodItem> topVotedFoodItems = votedItemRepository.getTopVotedFoodItems();
        List<Integer> topVotedFoodItemIds = new ArrayList<>();
        for (FoodItem foodItem : topVotedFoodItems) {
            topVotedFoodItemIds.add(foodItem.getId());
        }*/
       // votedItemRepository.markFoodItemsAsPrepared(topVotedFoodItemIds);

    }

    public void rollOutFoodItems(List<Integer> votedItemIds)  throws SQLException {
        votedItemRepository.markFoodItemsAsPrepared(votedItemIds);
    }

    public List<RollOutFoodItemsDTO> viewRollOutFoodItem(String mealType) throws SQLException {
        List<RollOutFoodItemsDTO> rollOutFoodItemsDTOList = votedItemRepository.getRollOutItem(mealType);
        return rollOutFoodItemsDTOList;
    }



    public List<FoodItem> viewFoodItem(String mealType) throws SQLException {
        List<FoodItem>foodItems = votedItemRepository.getFoodItemsVotedToday(mealType);
        return foodItems;
    }

}