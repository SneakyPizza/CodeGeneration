package io.swagger.model.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER, ROLE_BANK;

    @Override
    public String getAuthority() {
        return name();
    }
}
