package org.example.handler;

import com.google.gson.Gson;
import org.example.controller.AuthenticationController;
import org.example.model.LoginMessage;
import org.example.model.MessageType;
import org.example.model.RoleMessageDTO;
import org.example.server.MultiClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

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

                if (messageType.type.equals("LOGIN")) {
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

            RoleMessageDTO roleMessageDTO = authController.login(loginMessage.getEmail(), loginMessage.getName());

            String roleJson = gson.toJson(roleMessageDTO);
            out.println(roleJson);

            return roleMessageDTO.getRole();
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
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
                System.out.println("--------------");
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
            case "VIEW_DISCARD_ITEMS":
                chefHandler.handleDiscardMenuItems(in,out);
            default:
                System.out.println("--------------");
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
            case "CREATE_USER_PROFILE" :
                employeeHandler.handleUserProfile(in,out);
                break;
            default:
                System.out.println("--------------");
        }
    }
    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}
