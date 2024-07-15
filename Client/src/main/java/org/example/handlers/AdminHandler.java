package org.example.handlers;

import com.google.gson.Gson;
import org.example.services.AdminService;
import org.example.services.ChefService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public class AdminHandler implements RoleHandler{

    public boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId , InetAddress ip) throws IOException {
        switch (selection) {
            case "1":
                AdminService.handleAddFoodItem(scanner, out, in, gson , ip);
                return false;
            case "2":
                AdminService.handleUpdateFoodItem(scanner, out, in, gson,ip);
                return false;
            case "3":
                AdminService.handleDeleteFoodItem(scanner, out, in, gson,ip);
                return false;
            case "4":
                AdminService.handleViewAllFoodItems(out, in, gson,ip);
                return false;
            case "5":
                AdminService.handleViewNotification(out, in , gson,ip);
                return false;
            case "6" :
                AdminService.handleDiscardMenuItems(out, in, gson, ip);
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
