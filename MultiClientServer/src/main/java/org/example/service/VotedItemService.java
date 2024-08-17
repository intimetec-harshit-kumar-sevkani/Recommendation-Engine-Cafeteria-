package org.example.service;

import org.example.model.FoodItem;
import org.example.DTO.RollOutFoodItemsDTO;
import org.example.repository.VotedItemRepository;

import java.sql.SQLException;
import java.util.List;

public class VotedItemService {

    private VotedItemRepository votedItemRepository;

    public VotedItemService() throws SQLException {
        this.votedItemRepository = new VotedItemRepository();
    }

    public void updateFoodItems(List<Integer> votedItemIds) throws SQLException {
        votedItemRepository.voteFoodItems(votedItemIds);
    }

    public void rollOutFoodItems(List<Integer> votedItemIds)  throws SQLException {
        votedItemRepository.markFoodItemsAsPrepared(votedItemIds);
    }

    public List<RollOutFoodItemsDTO> viewVotedFoodItem(String mealType) throws SQLException {
        List<RollOutFoodItemsDTO> rollOutFoodItemsDTOList = votedItemRepository.getRollOutItem(mealType);
        return rollOutFoodItemsDTOList;
    }

    public List<FoodItem> viewFoodItems(String mealType) throws SQLException {
        List<FoodItem>foodItems = votedItemRepository.getFoodItemsForVote(mealType);
        return foodItems;
    }

    public List<FoodItem> getPreparedFoodItems() throws SQLException {
        List<Integer> foodItemIds = votedItemRepository.getPreparedFoodItemIds();
        return votedItemRepository.getFoodItemsByIds(foodItemIds);
    }

}
