package io.swagger.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.TransactionService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;




@SpringBootTest
public class TransactionServiceTest {

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


    @BeforeEach
    void setup(){
       //get the user from the database
        testUser = userRepository.findByUsername("test");
        Bank = userRepository.findByUsername("Bank");

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
    @Order(1)
    void A_getTransactions() {
        Assertions.assertEquals(1, transactionService.getAllTransactions().size());
        Assertions.assertEquals(testAccount.getIBAN(), transactionService.getAllTransactions().get(0).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), transactionService.getAllTransactions().get(0).getOrigin().getIBAN());
    }

    @Test
    @Order(2)
    void B_addTransaction() {
        Assertions.assertEquals(1, transactionService.getAllTransactions().size());
        transactionService.addTransaction(transaction);
        Assertions.assertEquals(2, transactionService.getAllTransactions().size());
        Assertions.assertEquals(testAccount.getIBAN(), transactionService.getAllTransactions().get(1).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), transactionService.getAllTransactions().get(1).getOrigin().getIBAN());

    }

    @Test
    @Order(3)
    //find a transaction by IBAN
    void C_findTransactionByIBAN() {
        Transaction t = transactionService.getTransactionById(transactionService.getAllTransactions().get(0).getId());
        Assertions.assertEquals(testAccount.getIBAN(), t.getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), t.getOrigin().getIBAN());
    }

    @Test
    @Order(4)
    //find a transaction by IBAN and timestamp
    void D_findTransactionByIBANAndTimestamp() {
        List<Transaction> t = transactionService.findByIBANAndTimestampBetween(BankAccount.getIBAN(),  LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        Assertions.assertEquals(2, t.size());
        Assertions.assertEquals(testAccount.getIBAN(), t.get(0).getTarget().getIBAN());
        Assertions.assertEquals(BankAccount.getIBAN(), t.get(0).getOrigin().getIBAN());
    }

    @Test
    @Order(5)
    //validate a transaction
    void E_validateTransaction() {
        TransactionValidation v = transactionService.isValidTransaction(transaction);
        Assertions.assertEquals(v.getStatus(), TransactionValidation.TransactionValidationStatus.VALID);
    }

    @Test
    @Order(6)
    //transaction with invalid pin
    void F_invalidIBAN() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(BankAccount);
        t.setPincode("1232");
        t.setPerformer(Bank);
        t.setIBAN("NL01INHO0000000001");
        t.execute();
        TransactionValidation v = transactionService.isValidTransaction(t);
        Assertions.assertEquals(v.getStatus(), TransactionValidation.TransactionValidationStatus.INVALID_PIN);
    }

    @Test
    @Order(7)
    //transaction with invalid origin is same as target
    void G_invalidOrigin() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(99));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(testAccount);
        t.setOrigin(testAccount);
        t.setPincode("1234");
        t.setPerformer(Bank);
        t.setIBAN(testAccount.getIBAN());
        t.execute();
        TransactionValidation v = transactionService.isValidTransaction(t);
        Assertions.assertEquals(v.getStatus(), TransactionValidation.TransactionValidationStatus.NOT_ALLOWED);
    }

    @Test
    @Order(8)
    //transaction with transaction with invalid amount
    void H_invalidAmount() {
        Transaction t = new Transaction();
        t.setAmount(new BigDecimal(1000));
        t.setTimestamp(LocalDateTime.now());
        t.setTarget(BankAccount);
        t.setOrigin(testAccount);
        t.setPincode("1234");
        t.setPerformer(testUser);
        t.setIBAN(testAccount.getIBAN());
        t.execute();
        TransactionValidation v = transactionService.isValidTransaction(t);
        Assertions.assertEquals(v.getStatus(), TransactionValidation.TransactionValidationStatus.TRANSACTION_LIMIT_EXCEEDED);
    }
}
