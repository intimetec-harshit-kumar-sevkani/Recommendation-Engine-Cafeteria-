package org.example.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityTablePrinter {

    private static final String HEADER_COLOR = "\u001B[34m"; // Blue
    private static final String DATA_COLOR = "\u001B[32m"; // Green
    private static final String RESET_COLOR = "\u001B[0m"; // Reset

    public static void printEntitiesAsTable(List<?> entities) {
        if (entities == null || entities.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        Class<?> entityClass = entities.get(0).getClass();
        Field[] fields = entityClass.getDeclaredFields();

        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            headers.add(field.getName());
        }

        List<List<String>> rows = new ArrayList<>();
        for (Object entity : entities) {
            List<String> row = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    row.add(field.get(entity).toString());
                } catch (IllegalAccessException e) {
                    row.add("N/A");
                }
            }
            rows.add(row);
        }

        printTable(headers, rows);
    }

    private static void printTable(List<String> headers, List<List<String>> rows) {
        // Print headers with color
        System.out.print(HEADER_COLOR);
        for (String header : headers) {
            System.out.print(String.format("%-20s", header));
        }
        System.out.println(RESET_COLOR);

        // Print rows with color
        for (List<String> row : rows) {
            System.out.print(DATA_COLOR);
            for (String data : row) {
                System.out.print(String.format("%-20s", data));
            }
            System.out.println(RESET_COLOR);
        }
    }
}
