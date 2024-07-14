package org.example.handler;

import com.google.gson.Gson;
import org.example.controller.AuthenticationController;
import org.example.model.LoginMessage;
import org.example.model.MessageType;
import org.example.model.RoleMessageDTO;
import org.example.server.MultiClientServer;
import org.example.util.MessageProcessor;

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

    private RoleHandler roleHandler;

    public ClientHandler(Socket socket) throws SQLException {
        this.socket = socket;
        this.authController = new AuthenticationController();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            MessageProcessor messageProcessor = new MessageProcessor(gson);
            String role = null;

            MessageProcessor.MessageWrapper<MessageType> wrapper;
            while ((wrapper = messageProcessor.processMessage(in, MessageType.class)) != null) {
                String ipAddress = wrapper.getIpAddress();
                MessageType messageType = wrapper.getMessage();

                System.out.println("Received message: " + gson.toJson(messageType) + " from IP: " + ipAddress);

                if (messageType.type.equals("LOGIN")) {
                    role = handleLogin(in, out);
                    if (role != null) {
                        roleHandler = RoleHandlerFactory.getHandler(role);
                    }
                }

                if (roleHandler != null) {
                    roleHandler.handleRequest(messageType, in, out);
                } else {
                    System.out.println("Role not set yet. Awaiting login.");
                }

                if ("LOGOUT".equals(messageType.type)) {
                    break;
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
        MessageProcessor messageProcessor = new MessageProcessor(new Gson());
        try {
            MessageProcessor.MessageWrapper<LoginMessage> loginMessageWrapper = messageProcessor.processMessage(in, LoginMessage.class);
            LoginMessage loginMessage = loginMessageWrapper.getMessage();
            System.out.println("Received login info: " + loginMessage.getEmail());
            RoleMessageDTO roleMessageDTO = authController.login(loginMessage.getEmail(), loginMessage.getName());
            String json = gson.toJson(roleMessageDTO);
            messageProcessor.sendMessage(out, json);
            return roleMessageDTO.getRole();
        } catch (Exception ex) {
            messageProcessor.sendMessage(out, "Error: " + ex.getMessage());
            return null;
        }
    }
    public String getClientInfo() {
        return socket.getInetAddress().toString();
    }
}

