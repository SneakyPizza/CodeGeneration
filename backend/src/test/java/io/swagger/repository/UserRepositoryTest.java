package io.swagger.repository;


import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.model.entities.UserStatus;
import io.swagger.repositories.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findById() {
        User user = userRepository.findById(any()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Assertions.assertNotNull(user);
    }

    @Test
    public void save() {
        User testUser = new User();
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
        testUser.setDayLimit(new BigDecimal(1000));
        testUser.setTransactionLimit(new BigDecimal(500));
        testUser.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));

        User user = userRepository.save(testUser);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(user.getUsername(), "test");
        Assertions.assertEquals(user.getPincode(), "1234");
        Assertions.assertEquals(user.getPassword(), "test");
        Assertions.assertEquals(user.getEmail(), "test@test.nl");
        Assertions.assertEquals(user.getFirstName(), "test");
        Assertions.assertEquals(user.getLastName(), "test");
        Assertions.assertEquals(user.getStreet(), "test");
        Assertions.assertEquals(user.getCity(), "test");
        Assertions.assertEquals(user.getZipcode(), "test");
        Assertions.assertEquals(user.getUserstatus(), UserStatus.ACTIVE);
        Assertions.assertEquals(user.getDayLimit(), new BigDecimal(1000));
        Assertions.assertEquals(user.getTransactionLimit(), new BigDecimal(500));
        Assertions.assertEquals(user.getRoles(), new ArrayList<>(List.of(Role.ROLE_USER)));
    }
}
