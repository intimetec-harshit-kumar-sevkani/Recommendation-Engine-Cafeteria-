package org.example;
import com.google.gson.Gson;

import java.io.*;
import java.util.Scanner;
import java.net.Socket;

import org.example.handlers.AdminHandler;
import org.example.handlers.ChefHandler;
import org.example.handlers.EmployeeHandler;
import org.example.models.LoginMessage;
import org.example.models.MessageType;
import org.example.models.RoleMessage;

/*

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            Gson gson = new Gson();

            MessageType loginType = new MessageType("LOGIN");
            String loginTypeJson = gson.toJson(loginType);
            out.println(loginTypeJson);

            System.out.println("Enter your email:");
            String email = scanner.nextLine();
            System.out.println("Enter your name:");
            String name = scanner.nextLine();

            LoginMessage loginMessage = new LoginMessage(email, name);
            String loginJson = gson.toJson(loginMessage);
            out.println(loginJson);

            RoleMessage roleMessage = gson.fromJson(in.readLine(), RoleMessage.class);
            String role = roleMessage.getRole();
            int userId = roleMessage.getUserId();
            System.out.println("Role: " + role);
            System.out.println("UserId : " + roleMessage.getUserId());
            boolean exit = false;
            while (!exit) {

                System.out.println(getMenuForRole(role));

                String selection = scanner.nextLine();

                switch (role) {
                    case "Admin":
                        exit = AdminHandler.handleAdminSelection(selection, scanner, out, in, gson);
                        break;
                    case "Chef":
                        exit = ChefHandler.handleChefSelection(selection, scanner, out, in, gson);
                        break;
                    case "Employee":
                        exit = EmployeeHandler.handleEmployeeSelection(selection, scanner, out, in, gson,userId);
                        break;
                    default:
                        System.out.println("Unknown role.");
                        exit = true;
                }
            }

        } catch (Exception ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String getMenuForRole(String role) {
        switch (role) {
            case "Admin":
                return "Admin Menu:\n1. Add Food Item\n2. Update Food Item\n3. Delete Food Item\n4. View All Food Items\n5. Exit\nEnter your choice:";
            case "Chef":
                return "Chef Menu:\n1. Get Recommendation\n2. view FoodItem\n3. Exit\n4. RollOut FoodItems\nEnter your choice:";
            case "Employee":
                return "Employee Menu:\n1. vote Item\n2. view FoodItem\n3. Exit\n4. Give Feedback\nEnter your choice:";
            default:
                return "Unknown role.";
        }
    }
}
*/

import org.example.utils.MenuUtils;
import org.example.utils.MessageUtils;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

  /*  public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            Gson gson = new Gson();

            MessageUtils.sendMessage(out, gson, new MessageType("LOGIN"));

            System.out.println("Enter your email:");
            String email = scanner.nextLine();
            System.out.println("Enter your name:");
            String name = scanner.nextLine();

            MessageUtils.sendMessage(out, gson, new LoginMessage(email, name));

            RoleMessage roleMessage = MessageUtils.receiveMessage(in, gson, RoleMessage.class);
            String role = roleMessage.getRole();
            int userId = roleMessage.getUserId();
            System.out.println("Role: " + role);
            System.out.println("UserId: " + userId);

            boolean exit = false;
            while (!exit) {
                System.out.println(MenuUtils.getMenuForRole(role));
                String selection = scanner.nextLine();

                switch (role) {
                    case "Admin":
                        exit = AdminHandler.handleSelection(selection, scanner, out, in, gson);
                        break;
                    case "Chef":
                        exit = ChefHandler.handleSelection(selection, scanner, out, in, gson);
                        break;
                    case "Employee":
                        exit = EmployeeHandler.handleSelection(selection, scanner, out, in, gson, userId);
                        break;
                    default:
                        System.out.println("Unknown role.");
                        exit = true;
                }
            }

        } catch (Exception ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }*/


    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            Gson gson = new Gson();

            boolean loggedIn = false;
            int userId = -1;
            String role = null;

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. LogIn");
                System.out.println("2. LogOut");
                System.out.print("Selection: ");
                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        if (!loggedIn) {
                            MessageUtils.sendMessage(out, gson, new MessageType("LOGIN"));

                            System.out.println("Enter your email:");
                            String email = scanner.nextLine();
                            System.out.println("Enter your name:");
                            String name = scanner.nextLine();

                            MessageUtils.sendMessage(out, gson, new LoginMessage(email, name));

                            RoleMessage roleMessage = MessageUtils.receiveMessage(in, gson, RoleMessage.class);
                            if (roleMessage != null) {  // Check if roleMessage is not null
                                role = roleMessage.getRole();
                                userId = roleMessage.getUserId();
                                System.out.println("Role: " + role);
                                System.out.println("UserId: " + userId);

                                loggedIn = true;
                            } else {
                                System.out.println("Failed to retrieve role information.");
                                // Consider handling this case gracefully, e.g., retry or exit
                            }
                        } else {
                            System.out.println("You are already logged in.");
                        }
                        break;

                    case "2":
                        if (loggedIn) {
                            loggedIn = false;
                            userId = -1;
                            role = null;
                            System.out.println("Logged out successfully.");
                        } else {
                            System.out.println("You are not logged in.");
                        }
                        break;

                    default:
                        System.out.println("Invalid option.");
                        continue;
                }

                if (loggedIn) {
                    boolean exitMenu = false;
                    while (!exitMenu) {
                        System.out.println(MenuUtils.getMenuForRole(role));
                        System.out.print("Selection: ");
                        String selection = scanner.nextLine();

                        switch (role) {
                            case "Admin":
                                exitMenu = AdminHandler.handleSelection(selection, scanner, out, in, gson);
                                break;
                            case "Chef":
                                exitMenu = ChefHandler.handleSelection(selection, scanner, out, in, gson);
                                break;
                            case "Employee":
                                exitMenu = EmployeeHandler.handleSelection(selection, scanner, out, in, gson, userId);
                                break;
                            default:
                                System.out.println("Unknown role.");
                                exitMenu = true;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Client exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}


