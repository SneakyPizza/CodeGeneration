package io.swagger.configuration;

import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.UserService;
import io.swagger.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Value("${server.bank.iban}")
    private String bank_Iban;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Users testUsers = new Users();
        testUsers.setUsername("test");
        testUsers.setPincode("1234");
        testUsers.setPassword(passwordEncoder.encode("test"));
        testUsers.setEmail("test@test.nl");
        testUsers.setFirstName("test");
        testUsers.setLastName("test");
        testUsers.setStreet("test");
        testUsers.setCity("test");
        testUsers.setZipcode("test");
        testUsers.setUserstatus(UserStatus.ACTIVE);
        testUsers.setDayLimit(new BigDecimal(1000));
        testUsers.setTransactionLimit(new BigDecimal(500));
        testUsers.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));

        Users testUsers2 = new Users();
        testUsers2.setUsername("test2");
        testUsers2.setPincode("1234");
        testUsers2.setPassword(passwordEncoder.encode("test2"));
        testUsers2.setEmail("test2@test2.nl");
        testUsers2.setFirstName("test2");
        testUsers2.setLastName("test2");
        testUsers2.setStreet("test2");
        testUsers2.setCity("test2");
        testUsers2.setZipcode("test2");
        testUsers2.setUserstatus(UserStatus.DISABLED);
        testUsers2.setDayLimit(new BigDecimal(10000));
        testUsers2.setTransactionLimit(new BigDecimal(500));
        testUsers2.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));

        Users Bank = new Users();
        Bank.setUsername("Bank");
        Bank.setPincode("1234");
        Bank.setPassword(passwordEncoder.encode("Bank"));
        Bank.setEmail("bang@bank.nl");
        Bank.setFirstName("Bank");
        Bank.setLastName("Bank");
        Bank.setStreet("Bank");
        Bank.setCity("Bank");
        Bank.setZipcode("Bank");
        Bank.setUserstatus(UserStatus.ACTIVE);
        Bank.setDayLimit(new BigDecimal(String.valueOf(BigDecimal.valueOf(900000000000000L))));
        Bank.setTransactionLimit(new BigDecimal(String.valueOf(BigDecimal.valueOf(900000000000000L))));
        Bank.setRoles(new ArrayList<>(List.of(Role.ROLE_ADMIN)));

        Account BankAccount = new Account();
        BankAccount.setIBAN(bank_Iban);
        BankAccount.setBalance(new BigDecimal(1000000000));
//        BankAccount.setUser(Bank);
        BankAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        BankAccount.setAbsoluteLimit(new BigDecimal(0));
        BankAccount.setActive(Account.ActiveEnum.ACTIVE);

        Account testAccount = new Account();
        testAccount.setIBAN("NL01INHO0000000002");
        testAccount.setBalance(new BigDecimal(10000));
//        testAccount.setUser(testUser);
        testAccount.setAccountType(Account.AccountTypeEnum.CURRENT);
        testAccount.setAbsoluteLimit(new BigDecimal(0));
        testAccount.setActive(Account.ActiveEnum.ACTIVE);

        Account testAccountForTest = new Account();
        testAccountForTest.setIBAN("NL01INHO0200000002");
        testAccountForTest.setBalance(new BigDecimal(10000));
//        testAccount.setUser(testUser);
        testAccountForTest.setAccountType(Account.AccountTypeEnum.SAVINGS);
        testAccountForTest.setAbsoluteLimit(new BigDecimal(0));
        testAccountForTest.setActive(Account.ActiveEnum.ACTIVE);

        Account testAccount2 = new Account();
        testAccount2.setIBAN("NL01INHO0000000003");
        testAccount2.setBalance(new BigDecimal(10000));
//        testAccount2.setUser(testUser2);
        testAccount2.setAccountType(Account.AccountTypeEnum.CURRENT);
        testAccount2.setAbsoluteLimit(new BigDecimal(0));
        testAccount2.setActive(Account.ActiveEnum.ACTIVE);


        accountRepo.save(BankAccount);
        accountRepo.save(testAccount);
        accountRepo.save(testAccount2);
        accountRepo.save(testAccountForTest);

        BankAccount.setUsers(Bank);
        testAccount.setUsers(testUsers);
        testAccount2.setUsers(testUsers2);
        testAccountForTest.setUsers(testUsers);

        Bank.setAccounts(new ArrayList<>(List.of(BankAccount)));
        testUsers.setAccounts(new ArrayList<>());
        testUsers.getAccounts().add(testAccount);
        testUsers.getAccounts().add(testAccountForTest);
        testUsers2.setAccounts(new ArrayList<>(List.of(testAccount2)));

        userRepository.save(testUsers);
        userRepository.save(testUsers2);
        userRepository.save(Bank);


        //test transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(9999));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTarget(testAccount);
        transaction.setOrigin(BankAccount);
        transaction.setType(TransactionType.TRANSFER);
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
