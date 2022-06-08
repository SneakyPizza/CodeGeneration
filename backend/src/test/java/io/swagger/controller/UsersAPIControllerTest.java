package io.swagger.controller;

import io.swagger.api.TransactionsApiController;
import io.swagger.api.UsersApiController;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.TransactionRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.print.attribute.standard.Media;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    public User user;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test.nl");
        user.setFirstName("test");
        user.setLastName("test");
        user.setStreet("test");
        user.setCity("test");
        user.setZipcode("test");
        user.setDayLimit(BigDecimal.valueOf(100));
        user.setTransactionLimit(BigDecimal.valueOf(50));
        user.setPincode("1234");

        Account account = new Account();
        account.setUser(user);
        account.setIBAN("NL12INGB000123456789");
        account.setBalance(BigDecimal.valueOf(10));
        account.setActive(Account.ActiveEnum.ACTIVE);
        account.setAbsoluteLimit(BigDecimal.valueOf(10));
        List<Account> accounts = List.of(account);
        user.setAccounts(accounts);

        List<Role> roles = List.of(Role.ROLE_USER);
        user.setRoles(roles);
    }

    @ParameterizedTest
    @ValueSource(strings = {"4d6727c8-5d1d-4f28-81c9-b1ed3cc33815"})
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void getUser(UUID id) throws Exception {
        // create new user
        User user = new User();
        when(userService.getUser(id)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(user.getId().toString())))
                .andExpect((ResultMatcher) jsonPath("$.username", is(user.getUsername())))
                .andExpect((ResultMatcher) jsonPath("$.password", is(user.getPassword())))
                .andExpect((ResultMatcher) jsonPath("$.email", is(user.getEmail())))
                .andExpect((ResultMatcher) jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect((ResultMatcher) jsonPath("$.lastName", is(user.getLastName())))
                .andExpect((ResultMatcher) jsonPath("$.street", is(user.getStreet())))
                .andExpect((ResultMatcher) jsonPath("$.city", is(user.getCity())))
                .andExpect((ResultMatcher) jsonPath("$.zipcode", is(user.getZipcode())))
                .andExpect((ResultMatcher) jsonPath("$.dayLimit", is(user.getDayLimit().toString())))
                .andExpect((ResultMatcher) jsonPath("$.transactionLimit", is(user.getTransactionLimit().toString())))
                .andExpect((ResultMatcher) jsonPath("$.roles[0]", is(user.getRoles().get(0).toString())));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    public void createUser() throws Exception {
        // create new user
        User user = new User();
        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(user.getId().toString())))
                .andExpect((ResultMatcher) jsonPath("$.username", is(user.getUsername())))
                .andExpect((ResultMatcher) jsonPath("$.password", is(user.getPassword())))
                .andExpect((ResultMatcher) jsonPath("$.email", is(user.getEmail())))
                .andExpect((ResultMatcher) jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect((ResultMatcher) jsonPath("$.lastName", is(user.getLastName())))
                .andExpect((ResultMatcher) jsonPath("$.street", is(user.getStreet())))
                .andExpect((ResultMatcher) jsonPath("$.city", is(user.getCity())))
                .andExpect((ResultMatcher) jsonPath("$.zipcode", is(user.getZipcode())))
                .andExpect((ResultMatcher) jsonPath("$.dayLimit", is(user.getDayLimit().toString())))
                .andExpect((ResultMatcher) jsonPath("$.transactionLimit", is(user.getTransactionLimit().toString())))
                .andExpect((ResultMatcher) jsonPath("$.roles[0]", is(user.getRoles().get(0).toString())));
    }
}