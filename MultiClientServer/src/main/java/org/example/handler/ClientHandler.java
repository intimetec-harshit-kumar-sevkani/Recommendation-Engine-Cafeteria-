package org.example.handler;

import com.google.gson.reflect.TypeToken;
import org.example.controller.ChefController;
import org.example.controller.EmployeeController;
import org.example.model.*;
import org.example.server.MultiClientServer;
import org.example.controller.AuthenticationController;
import org.example.controller.FoodItemController;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

// previous working code
/*

public class ClientHandler implements Runnable {
    private Socket socket;
    private AuthenticationController authController;
    private FoodItemController foodItemController;

    private EmployeeController employeeController;
    private ChefController chefController;
    private Gson gson = new Gson();

    public ClientHandler(Socket socket) throws SQLException {
        this.socket = socket;
        this.authController = new AuthenticationController();
        this.foodItemController = new FoodItemController();
        this.chefController = new ChefController();
        this.employeeController = new EmployeeController();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message: " + message);
                MessageType messageType = gson.fromJson(message, MessageType.class);

                switch (messageType.type) {
                    case "LOGIN":
                        handleLogin(in, out);
                        break;
                    case "ADD_FOOD_ITEM":
                        handleAddFoodItem(in, out);
                        break;
                    case "UPDATE_FOOD_ITEM":
                        handleUpdateFoodItem(in, out);
                        break;
                    case "DELETE_FOOD_ITEM":
                        handleDeleteFoodItem(in, out);
                        break;
                    case "VIEW_ALL_FOOD_ITEMS":
                        handleViewAllFoodItems(out);
                        break;
                    case "GET_RECOMMENDED_ITEMS":
                         handleRecommendationFoodItems(in,out);
                         break;
                    case "VOTE_RECOMMENDED_ITEMS":
                        handleVotedFoodItems(in,out);
                        break;
                    case "GIVE_FEEDBACK":
                        handleFeedback(in,out);
                        break;
                    case "GET_VOTED_ITEMS":
                        handleRollOutItems(in,out);
                        break;
                    case "VIEW_NOTIFICATION":
                        handleNotifications(in,out);
                        break;
                    default:
                        System.out.println("Unknown message type received: " + messageType.type);
                }
            }

        } catch (Exception ex) {
            System.out.println("Client " + socket.getInetAddress() + " disconnected due to Exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing socket: " + ex.getMessage());
            }
            MultiClientServer.removeClient(this);
            System.out.println("Client " + socket.getInetAddress() + " disconnected");
        }
    }

    private void handleLogin(BufferedReader in, PrintWriter out) throws Exception {
       try {
           String loginMessageJson = in.readLine();
           LoginMessage loginMessage = gson.fromJson(loginMessageJson, LoginMessage.class);
           System.out.println("Received login info: " + loginMessage.getEmail());

           RoleMessage roleMessage = authController.login(loginMessage.getEmail(), loginMessage.getName());

           String roleJson = gson.toJson(roleMessage);
           out.println(roleJson);
       }
       catch (Exception ex) {

       }
    }

    private void handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String foodItemJson = in.readLine();
        FoodItem newFoodItem = gson.fromJson(foodItemJson, FoodItem.class);
        foodItemController.addFoodItem(newFoodItem);
        out.println("Food item added successfully.");
    }

    private void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String payload = in.readLine();
        FoodItem updatedFoodItem = gson.fromJson(payload, FoodItem.class);
        foodItemController.updateFoodItem(updatedFoodItem);
        out.println("Food item updated successfully.");
    }

    private void handleDeleteFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        int id = Integer.parseInt(in.readLine());
        foodItemController.deleteFoodItem(id);
        out.println("Food item deleted successfully.");
    }

    private void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }

    private void handleRecommendationFoodItems(BufferedReader in,PrintWriter out) throws IOException {
        String recommendedDTOJson = in.readLine();
        RecommendedDTO recommendedDTO= gson.fromJson(recommendedDTOJson, RecommendedDTO.class);
        String foodItemsJson = chefController.getTopFoodItems(recommendedDTO);
        out.println(foodItemsJson);
    }

    private void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        String mealType = in.readLine();
        List<FoodItem> foodItems = employeeController.viewVoteItem(mealType);
        String foodItemsJson = gson.toJson(foodItems);
        out.println(foodItemsJson);
        String votedItemIdsJson = in.readLine();
        List<Integer> votedItems = gson.fromJson(votedItemIdsJson, new TypeToken<List<Integer>>(){}.getType());
        employeeController.voteFoodItem(votedItems);
        out.println("Food Item Voted Successfully");

    }

    public void handleRollOutItems(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        String mealType = in.readLine();
        List<RollOutFoodItemsDTO> rollOutFoodItemsDTOList = chefController.getRollOutFoodItemsDTOList(mealType);
        String rollOutFoodItemJson = gson.toJson(rollOutFoodItemsDTOList);
        out.println(rollOutFoodItemJson);
        String rollItemIdsJson = in.readLine();
        List<Integer> rollOutItems = gson.fromJson(rollItemIdsJson, new TypeToken<List<Integer>>(){}.getType());
        chefController.rollOutItems(rollOutItems);
        out.println("Food Item Roll Out Successfully");
    }


    private void handleFeedback(BufferedReader in, PrintWriter out) throws IOException {
        String feedbackJson = in.readLine();
        Feedback feedback = gson.fromJson(feedbackJson, Feedback.class);
        employeeController.addFeedback(feedback);
        out.println("Feedback added Successfully");
    }

    private void handleNotifications(BufferedReader in, PrintWriter out) throws IOException, SQLException {
        List<Notification> notifications = employeeController.getNotification();
        String notificationJson = gson.toJson(notifications);
        out.println(notificationJson);
    }

    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}
*/

public class ClientHandler implements Runnable {
    private Socket socket;
    private AuthenticationController authController;
    private Gson gson = new Gson();

    private AdminHandler adminHandler;
    private ChefHandler chefHandler;
    private EmployeeHandler employeeHandler;

    public ClientHandler(Socket socket) throws SQLException {
        this.socket = socket;
        this.authController = new AuthenticationController();
        // Initialize role handlers
        this.adminHandler = new AdminHandlerImpl();
        this.chefHandler = new ChefHandlerImpl();
        this.employeeHandler = new EmployeeHandlerImpl();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String message;
            String role = null;
            while ((message = in.readLine()) != null) {
                System.out.println("Received message: " + message);
                MessageType messageType = gson.fromJson(message, MessageType.class);

                if (/*role == null && */messageType.type.equals("LOGIN")) {
                    role = handleLogin(in, out);
                }

                if (role != null) {
                    switch (role) {
                        case "Admin":
                            handleAdminRequests(messageType, in, out);
                            break;
                        case "Chef":
                            handleChefRequests(messageType, in, out);
                            break;
                        case "Employee":
                            handleEmployeeRequests(messageType, in, out);
                            break;
                        default:
                            System.out.println("Unknown role: " + role);
                    }
                } else {
                    System.out.println("Role not set yet. Awaiting login.");
                }
            }
        } catch (Exception ex) {
            System.out.println("Client " + socket.getInetAddress() + " disconnected due to Exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing socket: " + ex.getMessage());
            }
            MultiClientServer.removeClient(this);
            System.out.println("Client " + socket.getInetAddress() + " disconnected");
        }
    }

    private String handleLogin(BufferedReader in, PrintWriter out) throws Exception {
        try {
            String loginMessageJson = in.readLine();
            LoginMessage loginMessage = gson.fromJson(loginMessageJson, LoginMessage.class);
            System.out.println("Received login info: " + loginMessage.getEmail());

            RoleMessage roleMessage = authController.login(loginMessage.getEmail(), loginMessage.getName());

            String roleJson = gson.toJson(roleMessage);
            out.println(roleJson);

            return roleMessage.getRole();
        } catch (Exception ex) {
            return null;
        }
    }

    private void handleAdminRequests(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "ADD_FOOD_ITEM":
                adminHandler.handleAddFoodItem(in, out);
                break;
            case "UPDATE_FOOD_ITEM":
                adminHandler.handleUpdateFoodItem(in, out);
                break;
            case "DELETE_FOOD_ITEM":
                adminHandler.handleDeleteFoodItem(in, out);
                break;
            case "VIEW_ALL_FOOD_ITEMS":
                adminHandler.handleViewAllFoodItems(out);
                break;
            case "VIEW_NOTIFICATION" :
                adminHandler.handleNotifications(in,out);
                break;
            default:
                System.out.println("Unknown message type for Admin: " + messageType.type);
        }
    }

    private void handleChefRequests(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "GET_RECOMMENDED_ITEMS":
                chefHandler.handleRecommendationFoodItems(in, out);
                break;
            case "GET_VOTED_ITEMS":
                chefHandler.handleRollOutItems(in, out);
                break;
            case "VIEW_ALL_FOOD_ITEMS":
                chefHandler.handleViewAllFoodItems(out);
                break;
            case "VIEW_NOTIFICATION":
                chefHandler.handleNotifications(in,out);
                break;
            default:
                System.out.println("Unknown message type for Chef: " + messageType.type);
        }
    }

    private void handleEmployeeRequests(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException {
        switch (messageType.type) {
            case "VIEW_ALL_FOOD_ITEMS":
                employeeHandler.handleViewAllFoodItems(out);
                break;
            case "GIVE_FEEDBACK":
                employeeHandler.handleFeedback(in, out);
                break;
            case "VOTE_RECOMMENDED_ITEMS":
                employeeHandler.handleVotedFoodItems(in, out);
                break;
            case "VIEW_NOTIFICATION":
                employeeHandler.handleNotifications(in,out);
                break;
            case "VIEW_TODAY_MENU":
                employeeHandler.handleTodayMenuItems(in,out);
                break;
            default:
                System.out.println("Unknown message type for Employee: " + messageType.type);
        }
    }
    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}
