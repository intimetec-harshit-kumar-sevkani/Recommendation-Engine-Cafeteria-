package org.example;

import com.google.gson.Gson;
import org.example.handlers.RoleHandler;
import org.example.handlers.RoleHandlerFactory;
import org.example.models.LoginMessage;
import org.example.models.MessageType;
import org.example.models.RoleMessageDTO;
import org.example.utils.MessageUtil;
import org.example.utils.MenuUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        boolean serverConnected = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            serverConnected = true;

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
                            LoginResult loginResult = performLogin(scanner, out, in, gson , socket.getInetAddress());

                            if (loginResult != null) {
                                role = loginResult.getRole();
                                userId = loginResult.getUserId();
                                loggedIn = true;
                            } else {
                                System.out.println("Login failed.");
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
                    handleUserMenu(scanner, out, in, gson, role, userId , socket.getInetAddress());
                }
            }

        } catch (Exception ex) {
            if (!serverConnected) {
                System.out.println("Could not connect to the server. Please ensure the server is running and try again.");
            }
            else {
                System.out.println("Client exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private LoginResult performLogin(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson , InetAddress ip) throws IOException {
        MessageUtil.sendMessage(out,gson,new MessageType("LOGIN"),ip);
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        MessageUtil.sendMessage(out,gson,new LoginMessage(email),ip);
        RoleMessageDTO roleMessageDTO = MessageUtil.receiveMessage(in, gson, RoleMessageDTO.class);

        if (roleMessageDTO != null) {
            return new LoginResult(roleMessageDTO.getRole(), roleMessageDTO.getUserId());
        }
        return null;
    }

    private void handleUserMenu(Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, String role, int userId, InetAddress ip) throws IOException {
        boolean exitMenu = false;
        RoleHandler roleHandler = RoleHandlerFactory.getRoleHandler(role);
        while (!exitMenu) {
            System.out.println(MenuUtils.getMenuForRole(role));
            System.out.print("Selection: ");
            String selection = scanner.nextLine();
            exitMenu = roleHandler.handleSelection(selection, scanner, out, in, gson, userId , ip);
        }
    }

    private static class LoginResult {
        private final String role;
        private final int userId;

        public LoginResult(String role, int userId) {
            this.role = role;
            this.userId = userId;
        }

        public String getRole() {
            return role;
        }

        public int getUserId() {
            return userId;
        }
    }
}

