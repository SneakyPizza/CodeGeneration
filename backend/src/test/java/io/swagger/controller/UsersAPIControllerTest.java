package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.TransactionsApiController;
import io.swagger.api.UsersApiController;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.JwtDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.TransactionRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.TransactionService;
import io.swagger.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.put;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersApiController.class)
public class UsersAPIControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private UserRepository userRepository;

    public User testUser;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.nl");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setStreet("test");
        testUser.setCity("test");
        testUser.setZipcode("test");
        testUser.setUserstatus(UserStatus.ACTIVE);
        testUser.setDayLimit(new BigDecimal(10000));
        testUser.setTransactionLimit(new BigDecimal(500));
        testUser.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));
        testUser.setAccounts(new ArrayList<>(List.of()));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void getUser() throws Exception {
        when(userService.getUser(any())).thenReturn(testUser);
        mockMvc.perform(get("/Users/" + testUser.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(testUser.getId().toString()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.street").value(testUser.getStreet()))
                .andExpect(jsonPath("$.city").value(testUser.getCity()))
                .andExpect(jsonPath("$.zipcode").value(testUser.getZipcode()))
                .andExpect(jsonPath("$.userstatus").value(testUser.getUserstatus().toString().toLowerCase()))
                .andExpect(jsonPath("$.dayLimit").value(testUser.getDayLimit().toString()))
                .andExpect(jsonPath("$.transactionLimit").value(testUser.getTransactionLimit().toString()))
                .andExpect(jsonPath("$.roles[0]").value("admin"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void getAllUsers() throws Exception {
        List<GetUserDTO> users = List.of(testUser.getGetUserDTO());
        when(userService.getAllUsers(any(), any())).thenReturn(users);

        mockMvc.perform(get("/Users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userid").value(testUser.getId().toString()))
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(jsonPath("$[0].password").value(testUser.getPassword()))
                .andExpect(jsonPath("$[0].email").value(testUser.getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$[0].street").value(testUser.getStreet()))
                .andExpect(jsonPath("$[0].city").value(testUser.getCity()))
                .andExpect(jsonPath("$[0].zipcode").value(testUser.getZipcode()))
                .andExpect(jsonPath("$[0].userstatus").value(testUser.getUserstatus().toString().toLowerCase()))
                .andExpect(jsonPath("$[0].dayLimit").value(testUser.getDayLimit().toString()))
                .andExpect(jsonPath("$[0].transactionLimit").value(testUser.getTransactionLimit().toString()))
                .andExpect(jsonPath("$[0].roles[0]").value("admin"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    void addUser() throws Exception {
        when(userService.createUser(any())).thenReturn(testUser);

        String post = "{\n  \"city\": \"test\",\n  \"dayLimit\": 10000,\n  \"email\": \"test\",\n  " +
                "\"firstName\": \"test\",\n  \"lastName\": \"test\",\n  \"password\": \"test\",\n  " +
                "\"street\": \"test\",\n  \"transactionLimit\": 500,\n  \"username\": \"test\",\n  " +
                "\"userstatus\": \"active\",\n  \"zipcode\": \"test\"\n}";

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(post)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.street").value(testUser.getStreet()))
                .andExpect(jsonPath("$.city").value(testUser.getCity()))
                .andExpect(jsonPath("$.zipcode").value(testUser.getZipcode()))
                .andExpect(jsonPath("$.dayLimit").value(testUser.getDayLimit().toString()))
                .andExpect(jsonPath("$.transactionLimit").value(testUser.getTransactionLimit().toString()));
    }

    @Test
    @WithMockUser(username = "Bank", password = "Bank", roles = "ADMIN")
    void addUserAdmin() throws Exception {
        when(userService.createUserAdmin(any())).thenReturn(testUser);

        String post = "{\n  \"city\": \"test\",\n  \"dayLimit\": 10000,\n  \"email\": \"test\",\n  " +
                "\"firstName\": \"test\",\n  \"lastName\": \"test\",\n  \"password\": \"test\",\n  " +
                "\"roles\": [\n    \"admin\"\n  ],\n  \"street\": \"test\",\n  \"transactionLimit\": 500,\n  " +
                "\"username\": \"test\",\n  \"userstatus\": \"active\",\n  \"zipcode\": \"test\"\n}";

        mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(post)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.street").value(testUser.getStreet()))
                .andExpect(jsonPath("$.city").value(testUser.getCity()))
                .andExpect(jsonPath("$.zipcode").value(testUser.getZipcode()))
                .andExpect(jsonPath("$.userstatus").value(testUser.getUserstatus().toString().toLowerCase()))
                .andExpect(jsonPath("$.dayLimit").value(testUser.getDayLimit().toString()))
                .andExpect(jsonPath("$.transactionLimit").value(testUser.getTransactionLimit().toString()))
                .andExpect(jsonPath("$.roles[0]").value("admin"));
    }

    @Test
    @WithMockUser(username = "Bank", password = "Bank", roles = "ADMIN")
    void updateUser() throws Exception {
        when(userService.updateUser(any(), any())).thenReturn(testUser);

        String put = "{\n  \"city\": \"test\",\n  \"dayLimit\": 10000,\n  \"email\": \"test\",\n  " +
                "\"firstName\": \"test\",\n  \"lastName\": \"test\",\n  \"password\": \"test\",\n  " +
                "\"roles\": [\n    \"admin\"\n  ],\n  \"street\": \"test\",\n  \"transactionLimit\": 500,\n  " +
                "\"username\": \"test\",\n  \"userstatus\": \"active\",\n  \"zipcode\": \"test\"\n}";

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/Users/" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(put)
                        .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.password").value(testUser.getPassword()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.street").value(testUser.getStreet()))
                .andExpect(jsonPath("$.city").value(testUser.getCity()))
                .andExpect(jsonPath("$.zipcode").value(testUser.getZipcode()))
                .andExpect(jsonPath("$.userstatus").value(testUser.getUserstatus().toString().toLowerCase()))
                .andExpect(jsonPath("$.dayLimit").value(testUser.getDayLimit().toString()))
                .andExpect(jsonPath("$.transactionLimit").value(testUser.getTransactionLimit().toString()))
                .andExpect(jsonPath("$.roles[0]").value("admin"));
    }
}