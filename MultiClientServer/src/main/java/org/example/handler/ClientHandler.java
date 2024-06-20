package org.example.handler;

import org.example.server.MultiClientServer;
import org.example.controller.AuthenticationController;
import org.example.controller.FoodItemController;
import org.example.model.MessageType;
import org.example.model.FoodItem;
import org.example.model.LoginMessage;
import org.example.model.RoleMessage;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket socket;
    private AuthenticationController authController;
    private FoodItemController foodItemController;
    private Gson gson = new Gson();

    public ClientHandler(Socket socket) throws SQLException {
        this.socket = socket;
        this.authController = new AuthenticationController();
        this.foodItemController = new FoodItemController();
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
        String loginMessageJson = in.readLine();
        LoginMessage loginMessage = gson.fromJson(loginMessageJson, LoginMessage.class);
        System.out.println("Received login info: " + loginMessage.getEmail());

        String role = authController.login(loginMessage.getEmail(), loginMessage.getName());
        RoleMessage roleMessage = new RoleMessage(role);
        String roleJson = gson.toJson(roleMessage);
        out.println(roleJson);
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

    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}
