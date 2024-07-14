package org.example.handlers;

import com.google.gson.Gson;
import org.example.services.ChefService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public class ChefHandler implements RoleHandler {

    public boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId , InetAddress ip) throws IOException {
        switch (selection) {
            case "1":
                ChefService.handleGetRecommendedItems(scanner, out, in, gson, ip);
                return false;
            case "2":
                ChefService.handleViewAllFoodItems(out, in, gson, ip);
                return false;
            case "3":
                ChefService.handleRollOutFoodItems(scanner, out, in, gson, ip);
                return false;
            case "4" :
                ChefService.handleViewNotification(out, in, gson, ip);
                return false;
            case "5" :
                ChefService.handleDiscardMenuItems(out, in, gson, ip);
                return false;
            case "6":
                System.out.println("Exiting...");
                return true;
            default:
                System.out.println("Invalid selection.");
                return false;
        }
    }
}
