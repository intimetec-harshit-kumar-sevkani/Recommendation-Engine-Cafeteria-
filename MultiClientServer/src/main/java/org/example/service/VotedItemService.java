package org.example.service;

import org.example.model.FoodItem;
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
        List<FoodItem> topVotedFoodItems = votedItemRepository.getTopVotedFoodItems();
        List<Integer> topVotedFoodItemIds = new ArrayList<>();
        for (FoodItem foodItem : topVotedFoodItems) {
            topVotedFoodItemIds.add(foodItem.getId());
        }
        votedItemRepository.markFoodItemsAsPrepared(topVotedFoodItemIds);

    }

}
