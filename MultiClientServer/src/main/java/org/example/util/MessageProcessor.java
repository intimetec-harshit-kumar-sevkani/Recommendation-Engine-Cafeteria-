package org.example.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class MessageProcessor {

    private final Gson gson;

    public MessageProcessor(Gson gson) {
        this.gson = gson;
    }

    public <T> MessageWrapper<T> processMessage(BufferedReader in, Class<T> messageClass) throws IOException {
        // Read the incoming JSON string
        String json = in.readLine();
        if (json != null) {
            // Deserialize JSON to MessageWrapper
            Type wrapperType = TypeToken.getParameterized(MessageWrapper.class, messageClass).getType();
            return gson.fromJson(json, wrapperType);
        }
        return null;
    }

    public static class MessageWrapper<T> {
        private String ipAddress;
        private T message;

        public String getIpAddress() {
            return ipAddress;
        }

        public T getMessage() {
            return message;
        }
    }
}

