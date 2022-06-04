package io.swagger.configuration;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.services.UserService;
import io.swagger.services.transactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    transactionService transactionService;

    @Autowired
    UserService userService;

    @Value("${server.bank.iban}")
    private String bank_Iban;

    @Autowired
    AccountRepository accountRepo;

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
        Bank.setDayLimit(new BigDecimal(String.valueOf(BigDecimal.valueOf(900000000000000L))));
        Bank.setTransactionLimit(new BigDecimal(0));
        Bank.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        Account BankAccount = new Account();
        BankAccount.setIBAN(bank_Iban);
        BankAccount.setBalance(new BigDecimal(1000000000));
        BankAccount.setUser(Bank);
        BankAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        BankAccount.setAbsoluteLimit(new BigDecimal(0));
        BankAccount.setActive(Account.ActiveEnum.ACTIVE);

        Account testAccount = new Account();
        testAccount.setIBAN("NL01INHO0000000002");
        testAccount.setBalance(new BigDecimal(0));
        testAccount.setUser(testUser);
        testAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        testAccount.setAbsoluteLimit(new BigDecimal(0));
        testAccount.setActive(Account.ActiveEnum.ACTIVE);

        testUser.setAccounts(new ArrayList<>(List.of(testAccount)));
        Bank.setAccounts(new ArrayList<>(List.of(BankAccount)));
        userService.createUser(testUser);
        userService.createUser(Bank);
        accountRepo.save(BankAccount);
        accountRepo.save(testAccount);

        //test transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(9999));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTarget(testAccount);
        transaction.setOrigin(BankAccount);
        transaction.setPincode("1234");
        transaction.setPerformer(Bank);
        transaction.setIBAN(BankAccount.getIBAN());
        transaction.execute();
        System.out.println(transaction.getOrigin().getBalance());
        System.out.println(transaction.getTarget().getBalance());
        transactionService.addTransaction(transaction);
        if(transactionService.transactionExists(transaction.getId())){
            accountRepo.save(transaction.getOrigin());
            accountRepo.save(transaction.getTarget());
        }
    }
}
