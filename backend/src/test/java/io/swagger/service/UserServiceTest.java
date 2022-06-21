package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.exception.custom.ForbiddenException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.JwtDTO;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private Authentication authentication;
    SecurityContext securityContext;

    @BeforeEach
    void setup() {
        //get the user from the database
        testUser = userRepository.findByUsername("test").orElseThrow(() -> new NotFoundException("User not found"));

        postUserDTO = new PostUserDTO();
        postUserDTO.setUsername("test");
        postUserDTO.setPassword("test");
        postUserDTO.setEmail("test@test.nl");
        postUserDTO.setFirstName("test");
        postUserDTO.setLastName("test");
        postUserDTO.setStreet("test");
        postUserDTO.setCity("test");
        postUserDTO.setZipcode("test");
        postUserDTO.setUserstatus(PostUserDTO.UserstatusEnum.DISABLED);
        postUserDTO.setDayLimit(new BigDecimal("10000.00"));
        postUserDTO.setTransactionLimit(new BigDecimal(500));
        postUserDTO.setRoles(new ArrayList<>(List.of(PostUserDTO.Role.USER)));

        postAsUserDTO = new PostAsUserDTO();
        postAsUserDTO.setUsername("test");
        postAsUserDTO.setPassword("test");
        postAsUserDTO.setEmail("test@test.nl");
        postAsUserDTO.setFirstName("test");
        postAsUserDTO.setLastName("test");
        postAsUserDTO.setStreet("test");
        postAsUserDTO.setCity("test");
        postAsUserDTO.setZipcode("test");
        postAsUserDTO.setDayLimit(new BigDecimal("10000.00"));
        postAsUserDTO.setTransactionLimit(new BigDecimal(500));

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");
    }

    @Test
    void getUser() {
        assertUser(userService.getUser(testUser.getId().toString()));
    }

    @Test
    void getUserInvalidUUID() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getUser("Invalid UUID"));
    }

    @Test
    void getUserNotOwnerUserAndAdmin() {
        when(securityContext.getAuthentication().getName()).thenReturn("test2");
        String id = testUser.getId().toString();
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.getUser(id));
    }

    @Test
    void getUserNotFound() {
        String randomUUID = UUID.randomUUID().toString();
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(randomUUID));
    }

    @Test
    void getAllUsers() {
        List<GetUserDTO> userDTOS = userService.getAllUsers(0, 10); // not work yet
        Assertions.assertEquals(1, userDTOS.size());
        User user = new User();
        user = user.setPropertiesFromGetUserDTO(userDTOS.get(0));
        assertUser(user);
    }

    @Test
    void getAllUsersNotAdmin() {
        when(securityContext.getAuthentication().getName()).thenReturn("test2");
        Assertions.assertThrows(ForbiddenException.class, () -> userService.getAllUsers(0, 10));
    }

    @Test
    void getAllUsersOffsetHigherThanTotal() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getAllUsers(1000000, 10));
    }

    @Test
    void createUser() {
        createdUser = userService.createUser(postAsUserDTO);
        assertUser(createdUser);
    }

    @Test
    void createUserFieldsNull() {
        postAsUserDTO.setUsername(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUser(postAsUserDTO));
    }

    @Test
    void createUserAsAdmin() {
        createdUser = userService.createUserAdmin(postUserDTO);
        assertUser(createdUser);
    }

    @Test
    void createUserAsAdminNotAdmin() {
        when(securityContext.getAuthentication().getName()).thenReturn("test2");
        Assertions.assertThrows(ForbiddenException.class, () -> userService.createUserAdmin(postUserDTO));
    }

    @Test
    void createUserAsAdminFieldsNull() {
        postUserDTO.setUsername(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.createUserAdmin(postUserDTO));
    }

    @Test
    void updateUser() {
        String id = userService.findByUsername(postUserDTO.getUsername()).getId().toString();
        testUser = userService.updateUser(postUserDTO, id);
        assertUser(testUser);
    }

    @Test
    void updateUserNotAdmin() {
        when(securityContext.getAuthentication().getName()).thenReturn("test2");
        String id = testUser.getId().toString();
        Assertions.assertThrows(ForbiddenException.class, () -> userService.updateUser(postUserDTO, id));
    }

    @Test
    void updateUserInvalidUUID() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(postUserDTO, "Invalid UUID"));
    }

    @Test
    void updateUserFieldsNull() {
        postUserDTO.setUsername(null);
        String id = testUser.getId().toString();
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(postUserDTO, id));
    }

    @Test
    void updateUserNotFound() {
        String randomUUID = UUID.randomUUID().toString();
        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(postUserDTO, randomUUID));
    }

    @Test
    void findByFirstName() {
        List<User> users = userService.findByFirstName("test");
        Assertions.assertEquals(1, users.size());
        assertUser(users.get(0));
    }

    @Test
    void findByLastName() {
        List<User> users = userService.findByLastName("test");
        Assertions.assertEquals(1, users.size());
        assertUser(users.get(0));
    }

    @Test
    void findByUsername() {
        User user = userService.findByUsername("test");
        assertUser(user);
    }

    @Test
    void login() {
        JwtDTO jwtDTO = userService.login("test", "test");
        Assertions.assertNotNull(jwtDTO.getJwtToken());
        Assertions.assertNotNull(jwtDTO.getId());
    }

    @Test
    void loginInvalidUsername() {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login("test2", "test"));
    }

    @Test
    void loginInvalidPassword() {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.login("test", "test2"));
    }

    void assertUser(User user) {
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals("test", user.getUsername());
        Assertions.assertTrue(passwordEncoder.matches("test", user.getPassword()));
        Assertions.assertNotNull(user.getPincode());
        Assertions.assertEquals("test@test.nl", user.getEmail());
        Assertions.assertEquals("test", user.getFirstName());
        Assertions.assertEquals("test", user.getLastName());
        Assertions.assertEquals("test", user.getStreet());
        Assertions.assertEquals("test", user.getCity());
        Assertions.assertEquals("test", user.getZipcode());
        Assertions.assertEquals(UserStatus.DISABLED, user.getUserstatus());
        Assertions.assertEquals(new BigDecimal("10000.00"), user.getDayLimit());
        Assertions.assertEquals(new BigDecimal("500.00"), user.getTransactionLimit());
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Role.ROLE_USER, user.getRoles().get(0));
    }
}
