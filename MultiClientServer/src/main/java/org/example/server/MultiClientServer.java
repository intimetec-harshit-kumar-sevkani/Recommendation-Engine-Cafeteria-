package org.example.server;


import org.example.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MultiClientServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public MultiClientServer() {}

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
                } catch (IOException | SQLException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void removeClient(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        System.out.println("Removed client: " + clientHandler.getClientInfo());
    }
}

