package org.example.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface EmployeeHandler {
    void handleViewAllFoodItems(PrintWriter out) throws IOException;
    void handleFeedback(BufferedReader in, PrintWriter out) throws IOException;
    void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException, SQLException;
    void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException;

    void handleTodayMenuItems(BufferedReader in, PrintWriter out) throws IOException, SQLException;

}

