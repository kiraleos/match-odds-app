package org.example.database.model;

public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2);

    private final int code;

    Sport(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Sport fromCode(int code) {
        for (var sport : values()) {
            if (sport.code == code) {
                return sport;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
