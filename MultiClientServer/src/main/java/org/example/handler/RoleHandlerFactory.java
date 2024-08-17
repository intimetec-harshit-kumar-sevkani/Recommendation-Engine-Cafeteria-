package org.example.handler;

import java.sql.SQLException;

public class RoleHandlerFactory {
    public static RoleHandler getHandler(String role) throws SQLException {
        switch (role) {
            case "Admin":
                return new AdminHandlerImpl();
            case "Chef":
                return new ChefHandlerImpl();
            case "Employee":
                return new EmployeeHandlerImpl();
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}

