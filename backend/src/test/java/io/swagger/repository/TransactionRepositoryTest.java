package io.swagger.repository;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.TransactionType;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.TransactionRepository;
import io.swagger.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
//@DataJpaTest
@Transactional
class TransactionRepositoryTest {
    @Autowired
    TransactionRepository repository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    private TestEntityManager em;

    Transaction transaction;

    @BeforeEach
    void setup(){
        User testUser = userRepository.findByUsername("test").orElseThrow(() -> new IllegalArgumentException("User not found"));

        User bankUser = userRepository.findByUsername("Bank").orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account testAccount = testUser.getAccounts().get(0);

        Account bankAccount = bankUser.getAccounts().get(0);

        transaction = new Transaction();
        transaction.setAmount(new BigDecimal(100));
        transaction.setType(TransactionType.TRANSFER);
        transaction.setOrigin(bankAccount);
        transaction.setTarget(testAccount);
        transaction.setIBAN(bankAccount.getIBAN());
        transaction.setTimestamp(LocalDateTime.now());
    }

    //test save transaction
    @Test
    void saveTransaction(){
        Transaction result = repository.save(transaction);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.getTimestamp(), transaction.getTimestamp());
    }

    //test find all transactions
    @Test
    void findAllTransactions(){
        repository.save(transaction);
        List<Transaction> result = (List<Transaction>) repository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 2);

        Assertions.assertEquals(result.get(1).getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.get(1).getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.get(1).getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.get(1).getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.get(1).getTimestamp(), transaction.getTimestamp());
    }

    //test find transaction by id
    @Test
    void findTransactionById() {
        List<Transaction> resultList = (List<Transaction>) repository.findAll();
        Transaction firstTransaction = resultList.get(0);
        Transaction result = repository.findById(firstTransaction.getId()).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        Assertions.assertNotNull(result);

        Assertions.assertEquals(result.getAmount(), new BigDecimal("9999.00"));
        Assertions.assertEquals(result.getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertNotNull(result.getTimestamp());
    }

    //test find transaction by IBAN
    @Test
    void findTransactionByIBAN() {
        repository.save(transaction);
        List<Transaction> resultList = (List<Transaction>) repository.findAll();
        Transaction firstTransaction = resultList.get(0);
        List<Transaction> result = (List<Transaction>) repository.findByIBAN(firstTransaction.getOrigin().getIBAN());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(1).getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.get(1).getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.get(1).getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.get(1).getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.get(1).getTimestamp(), transaction.getTimestamp());
    }

    //find transactions by iban and 2 datetimes
    @Test
    void findTransactionsByIBANAndDate() {
        repository.save(transaction);
        List<Transaction> resultList = (List<Transaction>) repository.findAll();
        Transaction firstTransaction = resultList.get(0);
        List<Transaction> result = ( List<Transaction>) repository.findByIBANAndTimestampBetween(firstTransaction.getIBAN(), firstTransaction.getTimestamp().minusDays(1), firstTransaction.getTimestamp().plusDays(1));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(1).getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.get(1).getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.get(1).getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.get(1).getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.get(1).getTimestamp(), transaction.getTimestamp());

    }
    //find transaction by origin
    @Test
    void findTransactionsByOrigin() {
        repository.save(transaction);
        List<Transaction> resultList = (List<Transaction>) repository.findAll();
        Transaction firstTransaction = resultList.get(0);
        List<Transaction> result = ( List<Transaction>) repository.findByOriginId(firstTransaction.getOrigin().getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(1).getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.get(1).getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.get(1).getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.get(1).getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.get(1).getTimestamp(), transaction.getTimestamp());
    }

    //find transaction by target
    @Test
    void findTransactionsByTarget() {
        repository.save(transaction);
        List<Transaction> resultList = (List<Transaction>) repository.findAll();
        Transaction firstTransaction = resultList.get(0);
        List<Transaction> result = ( List<Transaction>) repository.findByTargetId(firstTransaction.getTarget().getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(1).getAmount(), new BigDecimal(100));
        Assertions.assertEquals(result.get(1).getType(), TransactionType.TRANSFER);
        Assertions.assertEquals(result.get(1).getOrigin().getIBAN(), transaction.getOrigin().getIBAN());
        Assertions.assertEquals(result.get(1).getTarget().getIBAN(), transaction.getTarget().getIBAN());
        Assertions.assertEquals(result.get(1).getTimestamp(), transaction.getTimestamp());
    }

}
