package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.dto.PostUserDTO;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.model.entities.UserStatus;
import io.swagger.repositories.UserRepository;
import io.swagger.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private User createdUser;
    private PostUserDTO postUserDTO;
    private PostAsUserDTO postAsUserDTO;
    private List<User> userList;

    @BeforeEach
    void setup() {
        //get the user from the database
        testUser = userRepository.findByUsername("test").orElseThrow(() -> new NotFoundException("User not found"));

        postUserDTO = new PostUserDTO();
        postUserDTO.setUsername("harry");
        postUserDTO.setPassword(passwordEncoder.encode("test"));
        postUserDTO.setEmail("harryharrinton@test.nl");
        postUserDTO.setFirstName("Harry");
        postUserDTO.setLastName("Harrinton");
        postUserDTO.setStreet("test");
        postUserDTO.setCity("test");
        postUserDTO.setZipcode("test");
        postUserDTO.setDayLimit(new BigDecimal(10000));
        postUserDTO.setTransactionLimit(new BigDecimal(500));

        postAsUserDTO = new PostAsUserDTO();
        postAsUserDTO.setUsername("harry");
        postAsUserDTO.setPassword(passwordEncoder.encode("test"));
        postAsUserDTO.setEmail("harryharrinton@test.nl");
        postAsUserDTO.setFirstName("Harry");
        postAsUserDTO.setLastName("Harrinton");
        postAsUserDTO.setStreet("test");
        postAsUserDTO.setCity("test");
        postAsUserDTO.setZipcode("test");
        postAsUserDTO.setDayLimit(new BigDecimal(10000));
        postAsUserDTO.setTransactionLimit(new BigDecimal(500));
    }

    @Test
    void getUser() {
//        userList = userService.getAllUsers(0, 1);
        testUser = userService.getUser(UUID.fromString(String.valueOf(userList.get(0).getId())));
        assertUser(testUser);
    }

    @Test
    void getAllUsers() {
//        userList = userService.getAllUsers(0, 1);
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals("test", userList.get(0).getUsername());
        Assertions.assertTrue(passwordEncoder.matches("test", userList.get(0).getPassword()));
    }

    @Test
    void createUser() {
        createdUser = userService.createUser(postAsUserDTO);
        assertUser(createdUser);
    }

    @Test
    void createUserAsAdmin() {
        createdUser = userService.createUserAdmin(postUserDTO);
        assertUser(createdUser);
    }

    @Test
    void updateUser() {
        testUser = userService.updateUser(postUserDTO);
        assertUser(testUser);
    }


    private boolean assertUser(User user) {
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals("harry", createdUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches("test", createdUser.getPassword()));
        Assertions.assertNotNull(createdUser.getPincode());
        Assertions.assertEquals("harryharrinton@test.nl", createdUser.getEmail());
        Assertions.assertEquals("Harry", createdUser.getFirstName());
        Assertions.assertEquals("Harrinton", createdUser.getLastName());
        Assertions.assertEquals("test", createdUser.getStreet());
        Assertions.assertEquals("test", createdUser.getCity());
        Assertions.assertEquals("test", createdUser.getZipcode());
        Assertions.assertEquals(UserStatus.DISABLED, createdUser.getUserstatus());
        Assertions.assertEquals(new BigDecimal(10000), createdUser.getDayLimit());
        Assertions.assertEquals(new BigDecimal(500), createdUser.getTransactionLimit());
        Assertions.assertEquals(1, createdUser.getRoles().size());
        Assertions.assertEquals(Role.ROLE_USER, createdUser.getRoles().get(0));
        return true; // not good
    }
}
