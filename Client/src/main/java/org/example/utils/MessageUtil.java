package org.example.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.InetAddress;

public class MessageUtil {

        private static class MessageWrapper<T> {
            private InetAddress ipAddress;
            private T message;

            public MessageWrapper(InetAddress ipAddress, T message) {
                this.ipAddress = ipAddress;
                this.message = message;
            }

            public InetAddress getIpAddress() {
                return ipAddress;
            }

            public T getMessage() {
                return message;
            }
        }

        public static <T> void sendMessage(PrintWriter out, Gson gson, T message, InetAddress ipAddress) {
            MessageWrapper<T> wrapper = new MessageWrapper<>(ipAddress, message);
            String json = gson.toJson(wrapper);
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
