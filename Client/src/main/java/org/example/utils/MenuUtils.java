package org.example.utils;

public class MenuUtils {

    public static String getMenuForRole(String role) {
        switch (role) {
            case "Admin":
                return "Admin Menu:\n1. Add Food Item\n2. Update Food Item\n3. Delete Food Item\n4. View All Food Items\n5. Exit";
            case "Chef":
                return "Chef Menu:\n1. Get Recommended Items\n2. View All Food Items\n3. Exit\n4. Roll Out Food Items";
            case "Employee":
                return "Employee Menu:\n1. View All Food Items\n2. Give Feedback\n3. Give Vote\n4. Exit\n5. View Notification";
            default:
                return "Invalid role.";
        }
    }
}
