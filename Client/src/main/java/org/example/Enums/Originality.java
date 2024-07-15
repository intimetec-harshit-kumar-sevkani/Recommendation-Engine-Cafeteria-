package org.example.Enums;

public enum Originality {
    NORTH_INDIAN(1, "North-Indian"),
    SOUTH_INDIAN(2, "South-Indian");

    private final int code;
    private final String description;

    Originality(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Originality fromCode(int code) {
        for (Originality originality : values()) {
            if (originality.code == code) {
                return originality;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    @Override
    public String toString() {
        return description;
    }
}
