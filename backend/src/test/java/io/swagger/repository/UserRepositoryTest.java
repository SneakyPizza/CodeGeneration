package io.swagger.repository;


import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.model.entities.UserStatus;
import io.swagger.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser;
    UUID  id;

    @BeforeEach
    void setup(){
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = userRepository.findAll(pageable).getContent();
        id = users.get(0).getId();

        testUser = new User();
        testUser.setUsername("test");
        testUser.setPincode("1234");
        testUser.setPassword(("test"));
        testUser.setEmail("test@test.nl");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setStreet("test");
        testUser.setCity("test");
        testUser.setZipcode("test");
        testUser.setUserstatus(UserStatus.ACTIVE);
        testUser.setDayLimit(new BigDecimal("1000.00"));
        testUser.setTransactionLimit(new BigDecimal("500.00"));
        testUser.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));
    }

    @Test
    void findById() {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        assertUser(user);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 1);
        List<User> users = userRepository.findAll(pageable).getContent();
        assertUser(users.get(0));
    }

    @Test
    void save() {
        testUser.setPassword(passwordEncoder.encode(testUser.getPassword()));
        User user = userRepository.save(testUser);
        assertUser(user);
    }

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("test").orElseThrow(() -> new IllegalArgumentException("User not found"));
        assertUser(user);
    }

    @Test
    void findByFirstName() {
        List<User> users = userRepository.findByFirstName("test").orElseThrow(() -> new IllegalArgumentException("User not found"));
        User user = users.get(0);
        assertUser(user);
    }

    @Test
    void findByLastName() {
        List<User> users = userRepository.findByLastName("test").orElseThrow(() -> new IllegalArgumentException("User not found"));
        User user = users.get(0);
        assertUser(user);
    }

    void assertUser(User user) {
        Assertions.assertNotNull(user);
        Assertions.assertEquals("test", user.getUsername());
        Assertions.assertEquals("1234", user.getPincode());
        Assertions.assertTrue(passwordEncoder.matches("test", user.getPassword()));
        Assertions.assertEquals("test@test.nl", user.getEmail());
        Assertions.assertEquals("test", user.getFirstName());
        Assertions.assertEquals("test", user.getLastName());
        Assertions.assertEquals("test", user.getStreet());
        Assertions.assertEquals("test", user.getCity());
        Assertions.assertEquals("test", user.getZipcode());
        Assertions.assertEquals(UserStatus.ACTIVE, user.getUserstatus());
        Assertions.assertEquals(new BigDecimal("1000.00"), user.getDayLimit());
        Assertions.assertEquals(new BigDecimal("500.00"), user.getTransactionLimit());
        Assertions.assertEquals(List.of(Role.ROLE_USER), user.getRoles());
    }
}
