package org.example.handlers;

public class RoleHandlerFactory {
    public static RoleHandler getRoleHandler(String role) {
        switch (role) {
            case "Admin":
                return new AdminHandler();
            case "Chef":
                return new ChefHandler();
            case "Employee":
                return new EmployeeHandler();
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}

