package org.example.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface AdminHandler {
    void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException;
    void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException;
    void handleDeleteFoodItem(BufferedReader in, PrintWriter out) throws IOException;
    void handleViewAllFoodItems(PrintWriter out) throws IOException;

    void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException;


}
