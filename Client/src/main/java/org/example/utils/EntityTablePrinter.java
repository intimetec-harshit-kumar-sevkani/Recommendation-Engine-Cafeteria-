package org.example.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityTablePrinter {
    private static final String HEADER_COLOR = "\u001B[35m";
    private static final String DATA_COLOR = "\u001B[36m";
    private static final String RESET_COLOR = "\u001B[0m";
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
                    Object value = field.get(entity);
                    if ("mealTypeId".equals(field.getName())) {
                        if (value instanceof Integer) {
                            value = TypeUtil.getMealTypeName((Integer) value);
                        }
                    }

                    if ("notificationTypeId".equals(field.getName())) {
                        if (value instanceof Integer) {
                            value = TypeUtil.getNotificationTypeName((Integer) value);
                        }
                    }

                    row.add(value != null ? value.toString() : "N/A");
                } catch (IllegalAccessException e) {
                    row.add("N/A");
                }
            }
            rows.add(row);
        }

        printTable(headers, rows);
    }

    private static void printTable(List<String> headers, List<List<String>> rows) {
        System.out.print(HEADER_COLOR);
        for (String header : headers) {
            System.out.print(String.format("%-20s", header));
        }
        System.out.println(RESET_COLOR);
        for (List<String> row : rows) {
            System.out.print(DATA_COLOR);
            for (String data : row) {
                System.out.print(String.format("%-20s", data));
            }
            System.out.println(RESET_COLOR);
        }
    }

}
