package io.swagger.model;

import io.swagger.model.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {

    private Role roleAdmin;
    private Role roleUser;

    @BeforeEach
    void setup() {
        roleAdmin = Role.ROLE_ADMIN;
        roleUser = Role.ROLE_USER;
    }

    @Test
    void getAuthority() {
        assertEquals("ROLE_ADMIN", roleAdmin.getAuthority());
        assertEquals("ROLE_USER", roleUser.getAuthority());
    }

    @Test
    void fromValue() {
        assertEquals(roleAdmin, Role.fromValue("ROLE_ADMIN"));
        assertEquals(roleUser, Role.fromValue("ROLE_USER"));
    }
}
