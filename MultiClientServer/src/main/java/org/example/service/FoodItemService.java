package org.example.service;

import java.sql.SQLException;
import java.util.List;
import org.example.model.FoodItem;
import org.example.repository.FoodItemAuditRepository;
import org.example.repository.FoodItemRepository;

public class FoodItemService {
    private FoodItemRepository foodItemRepository;
    private NotificationService notificationService;

    private FoodItemAuditRepository foodItemAuditRepository;

    public FoodItemService() throws SQLException {
        this.foodItemRepository = new FoodItemRepository();
        this.notificationService = new NotificationService();
        this.foodItemAuditRepository = new FoodItemAuditRepository();
    }

    public void addFoodItem(FoodItem foodItem) throws SQLException {
        int foodItemId = foodItemRepository.addFoodItem(foodItem);
        notificationService.sendNotification("FoodItem Update", "Food Item Added", foodItemId);
        foodItemAuditRepository.addFoodAudit(foodItemId);
    }

    public void updateFoodItem(FoodItem foodItem) throws SQLException {
        foodItemRepository.updateFoodItem(foodItem);
        notificationService.sendNotification("FoodItem Update", "Food Item Updated", foodItem.getId());

    }

    public void deleteFoodItem(int id) throws SQLException {
        foodItemRepository.deleteFoodItem(id);
        notificationService.sendNotification("FoodItem Update", "Food Item Deleted", id);

    }

    public List<FoodItem> getAllFoodItems() throws SQLException {
        return foodItemRepository.getAllFoodItems();
    }


    public void discardFoodItem(List<Integer> foodItemIds) throws SQLException {
        foodItemRepository.discardFoodItems(foodItemIds);
    }

}
