package io.swagger.model.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }

    public static Role fromValue(String text) {
        for (Role b : Role.values()) {
            if (String.valueOf(b.name()).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
