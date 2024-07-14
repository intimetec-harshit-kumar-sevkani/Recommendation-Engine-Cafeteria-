package org.example.handlers;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public interface RoleHandler {
    //boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId) throws IOException;
    boolean handleSelection(String selection, Scanner scanner, PrintWriter out, BufferedReader in, Gson gson, int userId , InetAddress ip) throws IOException;


}
