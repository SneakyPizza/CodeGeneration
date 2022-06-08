package io.swagger.model.entities;

public enum UserStatus {
    ACTIVE, DISABLED;

    public static UserStatus fromValue(String text) {
        for (UserStatus b : UserStatus.values()) {
            if (String.valueOf(b.name()).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
