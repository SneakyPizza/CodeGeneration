package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.TransactionsApiController;
import io.swagger.configuration.WebSecurityConfig;
import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.transactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransactionsApiController.class)
public class TransactionAPIControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private transactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper mapper;

    public Transaction transaction;

    public User testUser;

    public User Bank;

    public Account testAccount;

    public Account BankAccount;


    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.nl");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setStreet("test");
        testUser.setCity("test");
        testUser.setZipcode("test");
        testUser.setDayLimit(new BigDecimal(10000));
        testUser.setTransactionLimit(new BigDecimal(500));
        testUser.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        Bank = new User();
        Bank.setUsername("Bank");
        Bank.setPassword("Bank");
        Bank.setEmail("bang@bank.nl");
        Bank.setFirstName("Bank");
        Bank.setLastName("Bank");
        Bank.setStreet("Bank");
        Bank.setCity("Bank");
        Bank.setZipcode("Bank");
        Bank.setDayLimit(new BigDecimal(0));
        Bank.setTransactionLimit(new BigDecimal(0));
        Bank.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        BankAccount = new Account();
        BankAccount.setIBAN("NL01INHO0000000001");
        BankAccount.setBalance(new BigDecimal(1000000000));
        BankAccount.setUser(Bank);
        BankAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        BankAccount.setAbsoluteLimit(new BigDecimal(1000000000));
        BankAccount.setActive(Account.ActiveEnum.ACTIVE);

        testAccount = new Account();
        testAccount.setIBAN("NL01INHO0000000002");
        testAccount.setBalance(new BigDecimal(0));
        testAccount.setUser(testUser);
        testAccount.setAccountType(Account.AccountTypeEnum.SAVINGS);
        testAccount.setAbsoluteLimit(new BigDecimal(1000000000));
        testAccount.setActive(Account.ActiveEnum.ACTIVE);

        testUser.setAccounts(new ArrayList<>(List.of(testAccount)));
        Bank.setAccounts(new ArrayList<>(List.of(BankAccount)));

        transaction = new Transaction();
        transaction.setAmount(new BigDecimal(9999));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTarget(testAccount);
        transaction.setOrigin(BankAccount);
        transaction.setPincode("1234");
        transaction.setPerformer(Bank);
        transaction.setIBAN(BankAccount.getIBAN());
        transaction.execute();
    }

    @Test
    //mock user bank
    @WithMockUser(username = "Bank", password = "Bank", roles = "ADMIN")
    public void getAllTransactions() throws Exception {
        //create new transaction
        List<Transaction> transactions = List.of(transaction);
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/Transactions/NL01INHO0000000001")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

}
