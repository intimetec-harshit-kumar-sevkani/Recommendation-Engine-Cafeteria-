package org.example.Enums;

public enum SpiceLevel {
    HIGH(1, "High"),
    MEDIUM(2, "Medium"),
    LOW(3, "Low");

    private final int code;
    private final String description;

    SpiceLevel(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SpiceLevel fromCode(int code) {
        for (SpiceLevel level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

    @Override
    public String toString() {
        return description;
    }
}
