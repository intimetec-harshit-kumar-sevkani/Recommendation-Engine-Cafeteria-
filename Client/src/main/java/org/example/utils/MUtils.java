package org.example.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

public class MUtils {

    public static <T> void sendMessage(PrintWriter out, Gson gson, T message) {
        String json = gson.toJson(message);
        out.println(json);
    }

    public static String receiveMessage(BufferedReader in) throws IOException {
        return in.readLine();
    }

    public static <T> T receiveMessage(BufferedReader in, Gson gson, Class<T> clazz) throws IOException {
        String json = in.readLine();
        return gson.fromJson(json, clazz);
    }

    public static <T> T receiveMessage(BufferedReader in, Gson gson, Type type) throws IOException {
        String json = in.readLine();
        return gson.fromJson(json, type);
    }
}
