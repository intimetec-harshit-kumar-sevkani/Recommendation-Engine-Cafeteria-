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

        RoleMessage roleMessage = authController.login(loginMessage.getEmail(), loginMessage.getName());

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

    private void handleRecommendationFoodItems(BufferedReader in,PrintWriter out) throws IOException {
        String recommendedDTOJson = in.readLine();
        RecommendedDTO recommendedDTO= gson.fromJson(recommendedDTOJson, RecommendedDTO.class);
        String foodItemsJson = chefController.getTopFoodItems(recommendedDTO);
        out.println(foodItemsJson);
    }

    private void handleVotedFoodItems(BufferedReader in, PrintWriter out) throws IOException {
        String votedItemIdsJson = in.readLine();
        List<Integer> votedItems = gson.fromJson(votedItemIdsJson, new TypeToken<List<Integer>>(){}.getType());
        employeeController.voteFoodItem(votedItems);
        out.println("Food Item Voted Successfully");

    }

    private void handleFeedback(BufferedReader in, PrintWriter out) throws IOException {
        String feedbackJson = in.readLine();
        Feedback feedback = gson.fromJson(feedbackJson, Feedback.class);
        employeeController.addFeedback(feedback);
        out.println("Feedback added Successfully");
    }

    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}

