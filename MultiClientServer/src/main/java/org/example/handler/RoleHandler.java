package org.example.handler;

import org.example.model.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public interface RoleHandler {
    void handleRequest(MessageType messageType, BufferedReader in, PrintWriter out) throws IOException, SQLException;
}
