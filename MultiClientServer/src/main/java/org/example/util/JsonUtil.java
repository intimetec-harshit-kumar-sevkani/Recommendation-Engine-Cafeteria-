package org.example.util;

// JsonUtil.java

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/*
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    public static String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
*/


import com.google.gson.Gson;
import java.util.*;

public class JsonUtil {
    public static final Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> List<T> fromJsonList(String json, Class<T[]> classOfT) {
        T[] array = gson.fromJson(json, classOfT);
        return Arrays.asList(array);
    }
}
