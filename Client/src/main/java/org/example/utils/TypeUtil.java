package org.example.utils;

public class TypeUtil {
    public static String getMealTypeName(int mealTypeId) {
        switch (mealTypeId) {
            case 1:
                return "Breakfast";
            case 2:
                return "Lunch";
            case 3:
                return "Dinner";
            default:
                return "Unknown";
        }
    }

    public static String getNotificationTypeName(int notificationTypeId) {
        switch (notificationTypeId) {
            case 1:
                return "Food_Item_Updates";
            case 2:
                return "Roll_Out_Menu";
            default:
                return "Unknown";
        }
    }
}
