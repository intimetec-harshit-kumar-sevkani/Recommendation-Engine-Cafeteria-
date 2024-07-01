package org.example.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface ChefHandler {
    void handleRecommendationFoodItems(BufferedReader in, PrintWriter out) throws IOException;
    void handleRollOutItems(BufferedReader in, PrintWriter out) throws IOException, SQLException;
    void handleViewAllFoodItems(PrintWriter out) throws IOException;

    void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException;


}
