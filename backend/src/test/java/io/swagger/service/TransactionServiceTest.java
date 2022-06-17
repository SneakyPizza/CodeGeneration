package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.exception.custom.InvalidTransactionsException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.TransactionDeniedException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.TransactionService;

import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionServiceTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper mapper;

    public Transaction transaction;

    public User testUser;

    public User Bank;

    public Account testAccount;

    public Account BankAccount;

    private static final String userNotFound = "User not found";
    private SecurityContext securityContext;


    @BeforeEach
    void setup(){
        Authentication authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

       //get the user from the database
        testUser = userRepository.findByUsername("test").orElseThrow(() -> new NotFoundException(userNotFound));
        Bank = userRepository.findByUsername("Bank").orElseThrow(() -> new NotFoundException(userNotFound));

        //get the account from the database
        testAccount = (Account) accountRepository.findByIBAN("NL01INHO0000000002");
        BankAccount = (Account) accountRepository.findByIBAN("NL01INHO0000000001");

        //create a transaction
        transaction = new Transaction();
        transaction.setAmount(new BigDecimal(99));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTarget(testAccount);
        transaction.setOrigin(BankAccount);
        transaction.setPincode("1234");
        transaction.setPerformer(Bank);
        transaction.setIBAN(BankAccount.getIBAN());
        transaction.execute();

    }

    @Test
    void getTransactions() {
        Assertions.assertEquals(1, transactionService.getAllTransactions().size());
        Assertions.assertEquals(testAccount.getIBAN(), transactionService.getAllTransactions().get(0).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), transactionService.getAllTransactions().get(0).getOrigin().getIBAN());
    }

    @Test
    void addTransaction() {
        Assertions.assertEquals(1, transactionService.getAllTransactions().size());
        transactionService.addTransaction(transaction);
        Assertions.assertEquals(2, transactionService.getAllTransactions().size());
        Assertions.assertEquals(testAccount.getIBAN(), transactionService.getAllTransactions().get(1).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), transactionService.getAllTransactions().get(1).getOrigin().getIBAN());

    }

    @Test
    //find a transaction by IBAN
    void findTransactionByIBAN() {
        Transaction t = transactionService.getTransactionById(transactionService.getAllTransactions().get(0).getId());
        Assertions.assertEquals(testAccount.getIBAN(), t.getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), t.getOrigin().getIBAN());
    }

    @Test
    //find a transaction by IBAN and timestamp
    void findTransactionByIBANAndTimestamp() {
        List<Transaction> t = transactionService.findByIBANAndTimestampBetween(BankAccount.getIBAN(),  LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        Assertions.assertEquals(1, t.size());
        Assertions.assertEquals(testAccount.getIBAN(), t.get(0).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), t.get(0).getOrigin().getIBAN());
    }


    @Test
    //transaction with invalid pin
    void invalidIBAN() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(BankAccount);
        t.setPincode("1232");
        t.setPerformer(Bank);
        t.setType(TransactionType.TRANSFER);
        t.setIBAN("NL01INHO0000000001");
        t.execute();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            transactionService.isValidTransaction(t);
        });
    }

    @Test
    //transaction with invalid origin is same as target
    void invalidOrigin() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(testAccount);
        t.setPincode("1234");
        t.setPerformer(Bank);
        t.setType(TransactionType.TRANSFER);
        t.setIBAN(testAccount.getIBAN());
        t.execute();
        Assertions.assertThrows(InvalidTransactionsException.class, () -> {
            transactionService.isValidTransaction(t);
        });
    }

    @Test
    //transaction with invalid amount
    void invalidAmount() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(10000));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(BankAccount);
        t.setOrigin(testAccount);
        t.setPincode("1234");
        t.setPerformer(testUser);
        t.setType(TransactionType.TRANSFER);
        t.setIBAN(testAccount.getIBAN());
        t.execute();
        Assertions.assertThrows(TransactionDeniedException.class, () -> {
            transactionService.isValidTransaction(t);
        });
    }

    @Test
    //performer is not the same as the origin
    void invalidPerformer() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(BankAccount);
        t.setPincode("1234");
        t.setPerformer(testUser);
        t.setIBAN(testAccount.getIBAN());
        t.setType(TransactionType.TRANSFER);
        t.execute();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            transactionService.isValidTransaction(t);
        });
    }

    @Test
    //transaction with amount over the limit
    void invalidAmountOverLimit() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(10001));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(BankAccount);
        t.setOrigin(testAccount);
        t.setPincode("1234");
        t.setPerformer(testUser);
        t.setIBAN(testAccount.getIBAN());
        t.setType(TransactionType.TRANSFER);
        t.execute();
        Assertions.assertThrows(TransactionDeniedException.class, () -> {
            transactionService.isValidTransaction(t);
        });
    }

    @Test
    //execute a transaction
    void executeTransaction() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(BankAccount);
        t.setPincode("1234");
        t.setPerformer(Bank);
        t.setIBAN(testAccount.getIBAN());
        t.setType(TransactionType.TRANSFER);
        t.execute();
        Assertions.assertEquals(99, t.getAmount().intValue());
        Assertions.assertEquals(testAccount.getIBAN(), t.getTarget().getIBAN());
    }

    @Test
    void doTransactionTest() {
        when(securityContext.getAuthentication().getName()).thenReturn("test");

        TransactionService service = mock(TransactionService.class);
        PostTransactionDTO postTransactionDTO = new PostTransactionDTO();
        postTransactionDTO.setAmount(new BigDecimal(99));
        postTransactionDTO.setFromIBAN(testAccount.getIBAN());
        postTransactionDTO.setToIBAN(BankAccount.getIBAN());
        postTransactionDTO.setPincode("1234");

        Transaction newTransaction = transactionService.doTransaction(postTransactionDTO, TransactionType.TRANSFER);

        Assertions.assertEquals(99, newTransaction.getAmount().intValue());
        Assertions.assertEquals(testAccount.getIBAN(), newTransaction.getOrigin().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), newTransaction.getTarget().getIBAN());
        Assertions.assertEquals(testUser, newTransaction.getPerformer());
        Assertions.assertEquals(TransactionType.TRANSFER, newTransaction.getType());
    }

    @Test
    void getHistory() {
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");

        List<GetTransactionDTO> t = transactionService.getHistory(BankAccount.getIBAN());
        Assertions.assertEquals(1, t.size());
        Assertions.assertEquals(BankAccount.getIBAN(), t.get(0).getFromIBAN());
        Assertions.assertNotNull(t.get(0).getTimestamp());
        Assertions.assertNotNull(t.get(0).getAmount());
        Assertions.assertNotNull(t.get(0).getToIBAN());
        Assertions.assertNotNull(t.get(0).getType());

    }

    @Test
    void getHistoryAsUnauthorizedUser() {
        when(securityContext.getAuthentication().getName()).thenReturn("test");
        String iban = BankAccount.getIBAN();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            List<GetTransactionDTO> t = transactionService.getHistory(iban);
        });
    }

    @Test
    void getHistoryForTestUser() {
        when(securityContext.getAuthentication().getName()).thenReturn("test");
        String iban = testAccount.getIBAN();
        Assertions.assertThrows(NotFoundException.class, () -> {
            List<GetTransactionDTO> t = transactionService.getHistory(iban);
        });
    }

    @Test
    void getHistoryBetweenTwoDates() {
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");

        List<GetTransactionDTO> t = transactionService.getHistory(BankAccount.getIBAN(), LocalDate.now().minusDays(1).toString(), LocalDate.now().plusDays(1).toString());
        Assertions.assertEquals(1, t.size());
        Assertions.assertEquals(BankAccount.getIBAN(), t.get(0).getFromIBAN());
        Assertions.assertNotNull(t.get(0).getTimestamp());
        Assertions.assertNotNull(t.get(0).getAmount());
        Assertions.assertNotNull(t.get(0).getToIBAN());
        Assertions.assertNotNull(t.get(0).getType());

    }
}
