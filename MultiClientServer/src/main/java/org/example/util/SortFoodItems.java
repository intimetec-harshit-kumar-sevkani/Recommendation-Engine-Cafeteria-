package org.example.util;

import org.example.model.FoodItem;
import org.example.model.UserProfile;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class SortFoodItems {

    public static List<FoodItem> sortFoodItems(List<FoodItem> foodItems, UserProfile userProfile) throws SQLException {
        Comparator<FoodItem> comparator = Comparator
                .comparing((FoodItem fi) -> !fi.getFoodType().equals(userProfile.getFoodType()))
                .thenComparing((FoodItem fi) -> !fi.getSpiceLevel().equals(userProfile.getSpiceLevel()))
                .thenComparing((FoodItem fi) -> !fi.getOriginality().equals(userProfile.getOriginality()))
                .thenComparing((FoodItem fi) -> fi.isSweetTooth() != userProfile.isSweetTooth());

       foodItems.sort(comparator);
        return foodItems;
    }

}
