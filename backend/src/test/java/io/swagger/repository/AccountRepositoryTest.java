package io.swagger.repository;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    Account testAccount;

    @BeforeEach
    void setup() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = userRepository.findAll(pageable).getContent();
        UUID id = users.get(0).getId();

        List<Account> accounts = accountRepository.findByUserId(id);
        testAccount = accounts.get(0);
    }

    @Test
    void findByIBAN() {
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = userRepository.findAll(pageable).getContent();
        UUID id = users.get(0).getId();

        List<Account> accounts = accountRepository.findByUserId(id);
        Account account = accounts.get(0);

        assertAccount(account);
    }

    void assertAccount(Account account) {
        Assertions.assertNotNull(account);
        Assertions.assertEquals(testAccount.getId(), account.getId());
        Assertions.assertEquals(testAccount.getAccountType(), account.getAccountType());
        Assertions.assertEquals(testAccount.getUser(), account.getUser());
        Assertions.assertEquals(testAccount.getBalance(), account.getBalance());
        Assertions.assertEquals(testAccount.getActive(), account.getActive());
        Assertions.assertEquals(testAccount.getAbsoluteLimit(), account.getAbsoluteLimit());
    }
}
