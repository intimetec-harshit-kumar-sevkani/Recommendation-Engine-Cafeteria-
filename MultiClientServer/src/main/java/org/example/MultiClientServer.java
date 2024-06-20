package org.example;
import org.example.controller.AuthenticationController;
import org.example.controller.FoodItemController;
import org.example.model.FoodItem;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

import static org.example.util.JsonUtil.gson;


public class MultiClientServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    public MultiClientServer() {

    }

    public static void main(String[] args) {
        MultiClientServer server = new MultiClientServer();
        server.start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected: " + socket.getInetAddress());
                    ClientHandler clientHandler = new ClientHandler(socket);
                    clientHandlers.add(clientHandler);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Removed client: " + clientHandler.getClientInfo());
    }
}




class ClientHandler implements Runnable {
    private Socket socket;

    private AuthenticationController authController;
    private FoodItemController foodItemController;

    public ClientHandler(Socket socket) throws SQLException {
        this.socket = socket;
        this.authController = new AuthenticationController();
        this.foodItemController =new FoodItemController();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Receive login info

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


    public String handleAddFoodItem(BufferedReader in, PrintWriter out) throws IOException {
            String foodItemJson = in.readLine();
            FoodItem newFoodItem = gson.fromJson(foodItemJson, FoodItem.class);
            foodItemController.addFoodItem(newFoodItem);
            return "Food item added successfully.";

    }
    private void handleUpdateFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String payload = in.readLine();
        FoodItem updatedFoodItem = gson.fromJson(payload, FoodItem.class);
        foodItemController.updateFoodItem(updatedFoodItem);
        out.println("Food item updated successfully.");
    }

    private void handleDeleteFoodItem(BufferedReader in, PrintWriter out) throws IOException {
        String payload = in.readLine();
        FoodItem foodItemToDelete = gson.fromJson(payload, FoodItem.class);
        foodItemController.deleteFoodItem(foodItemToDelete.getId());
        out.println("Food item deleted successfully.");
    }

    private void handleViewAllFoodItems(PrintWriter out) throws IOException {
        String foodItemsJson = foodItemController.getAllFoodItems();
        out.println(foodItemsJson);
    }


    private void handleLogin(BufferedReader in, PrintWriter out) throws Exception {
        String loginMessageJson = in.readLine();
        LoginMessage loginMessage = gson.fromJson(loginMessageJson, LoginMessage.class);
        System.out.println("Received login info: " + loginMessage.getEmail());

        String role = authController.login(loginMessage.getEmail(), loginMessage.getName());
        RoleMessage roleMessage = new RoleMessage(role);
        String roleJson = gson.toJson(roleMessage);
        out.println(roleJson);

      /*  if (!"Unknown".equals(role)) {
            boolean exit = false;
            while (!exit) {
                String menuMessageJson = in.readLine();
                MenuSelectionMessage menuSelectionMessage = gson.fromJson(menuMessageJson, MenuSelectionMessage.class);
                System.out.println("Received menu selection: " + menuSelectionMessage.selection);

                String response = handleMenuSelection(role, menuSelectionMessage.selection);
                ResponseMessage responseMessage = new ResponseMessage(response);
                String responseJson = gson.toJson(responseMessage);
                out.println(responseJson);

                exit = "5".equals(menuSelectionMessage.selection); // Exit on selection "5"
            }
        }*/
    }


    private static class MessageType {
        private String type;

        public MessageType(String type) {
            this.type = type;
        }
    }

    private static class LoginMessage {
        private String email;
        private String name;

        public LoginMessage(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }
    }

    private static class RoleMessage {
        private String role;

        public RoleMessage(String role) {
            this.role = role;
        }
    }


    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}














