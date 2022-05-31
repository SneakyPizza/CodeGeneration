package io.swagger.configuration;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import io.swagger.services.UserService;
import io.swagger.services.accountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.bank.iban}")
    private String bank_Iban;

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
        testUser.setDayLimit(new BigDecimal(10000));
        testUser.setTransactionLimit(new BigDecimal(500));
        testUser.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        User Bank = new User();
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

        Account BankAccount = new Account();
        BankAccount.setIBAN(bank_Iban);
        BankAccount.setBalance(new BigDecimal(1000000000));
        BankAccount.setUser(testUser);
        BankAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        BankAccount.setAbsoluteLimit(new BigDecimal(1000000000));
        BankAccount.setActive(Account.ActiveEnum.ACTIVE);

        Account testAccount = new Account();
        testAccount.setIBAN("NL91ABNA0417164300");
        testAccount.setBalance(new BigDecimal(0));
        testAccount.setUser(testUser);
        testAccount.setAccountType(Account.AccountTypeEnum.SAVINGS);
        testAccount.setAbsoluteLimit(new BigDecimal(1000000000));
        testAccount.setActive(Account.ActiveEnum.ACTIVE);

        testUser.setAccounts(new ArrayList<>(List.of(testAccount)));
        Bank.setAccounts(new ArrayList<>(List.of(BankAccount)));
        userService.createUser(testUser);
        userService.createUser(Bank);
        //accountService.addAccount(BankAccount);
        //accountService.addAccount(testAccount);
    }
}
