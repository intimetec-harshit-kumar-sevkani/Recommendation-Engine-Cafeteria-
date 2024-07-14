package org.example.handlers;

import com.google.gson.Gson;
import org.example.services.EmployeeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public class EmployeeHandler implements RoleHandler {

    public boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId, InetAddress ip) throws IOException {
        switch (selection) {
            case "1":
                EmployeeService.handleViewAllFoodItems(out, in, gson, ip);
                return false;
            case "2":
                EmployeeService.handleGiveFeedback(scanner, out, in, gson, userId, ip);
                return false;
            case "3":
                EmployeeService.handleGiveVote(scanner, out, in, gson, userId, ip);
                return false;
            case "4":
                EmployeeService.handleNotification(scanner, out, in, gson, userId, ip);
                return false;
            case "5":
                EmployeeService.handleTodayMenuItems(out, in, gson,userId, ip);
                return false;
            case "6" :
                EmployeeService.handleUserProfile(scanner,out, in, gson, userId, ip);
                return false;
            case "7":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }

}

