package io.swagger.configuration;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.services.UserService;
import io.swagger.services.accountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    io.swagger.services.accountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.nl");
        testUser.setFirstName("test");
        testUser.setLastName("test");
        testUser.setStreet("test");
        testUser.setCity("test");
        testUser.setZipcode("test");
        testUser.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_USER)));

        Account BankAccount = new Account();
        BankAccount.setIBAN("NL01INHO0000000001");
        BankAccount.setBalance(new BigDecimal(1000000000));
//        BankAccount.setUser(testUser);
        BankAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        BankAccount.setAbsoluteLimit(new BigDecimal(1000000000));

//        testUser.setAccounts(new ArrayList<>(List.of(BankAccount)));
        userService.createUser(testUser);
        accountService.addAccount(BankAccount);
    }
}
