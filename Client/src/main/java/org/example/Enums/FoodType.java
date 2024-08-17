package org.example.Enums;

public enum FoodType {
    VEGETARIAN(1, "Vegetarian"),
    NON_VEGETARIAN(2, "Non-Vegetarian");

    private final int code;
    private final String description;

    FoodType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FoodType fromCode(int code) {
        for (FoodType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    @Override
    public String toString() {
        return description;
    }
}
