package io.swagger.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.User;
import io.swagger.utils.ibanGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class accountTest {
    private Account account;
    private ibanGenerator ibanGenerator;
    
    @BeforeEach
    void setup(){
        account = new Account<User>();
    }

    //Id
    @Test
    void setIdToAGeneratedUUID() {
        UUID id = UUID.randomUUID();
        account.setId(id);
        assertEquals(id, account.getId());
    }

    @Test
    void setIdToNull() {
        assertThrows(NullPointerException.class, () -> account.setId(null));
    }

    //Iban
    @Test
    void setIbanToNull() {
        assertThrows(NullPointerException.class, () -> account.setIBAN(null));
    }

    @Test
    void setIban() {
        String iban = "NL01INH10000000001";
        account.setIBAN(iban);
        assertEquals(iban, account.getIBAN());
    }

    //Balance
    @Test
    void setBalanceToNull(){
        assertThrows(NullPointerException.class, () -> account.setBalance(null));
    }

    @Test
    void setBalanceToNegative(){
        assertThrows(IllegalArgumentException.class, () -> account.setBalance(BigDecimal.valueOf(-1)));
    }

    @Test
    void setBalance(){
        BigDecimal balance = new BigDecimal(new Random().nextDouble());
        account.setBalance(balance);
        assertEquals(balance, account.getBalance());
    }

    //Active enum
    @Test
    void setActive(){
        Account.ActiveEnum active = Account.ActiveEnum.ACTIVE;
        account.setActive(active);
        assertEquals(active, account.getActive());
    }

    @Test
    void setAccountType(){
        Account.AccountTypeEnum type = Account.AccountTypeEnum.CURRENT;
        account.setAccountType(type);
        assertEquals(type, account.getAccountType());
    }

    //Absolute limit
    @Test
    void setAbsoluteLimitToNull(){
        assertThrows(NullPointerException.class, () -> account.setAbsoluteLimit(null));

    }

    @Test
    void setAbsoluteLimitToNegative(){
        assertThrows(IllegalArgumentException.class, () -> account.setAbsoluteLimit(BigDecimal.valueOf(-1)));
    }

    //toAccountDTO
    @Test
    void toAccountDTO(){
        ibanGenerator = new ibanGenerator();
        BigDecimal limit = new BigDecimal(new Random().nextDouble());
        account.setAbsoluteLimit(limit);

        Account.AccountTypeEnum accountType = Account.AccountTypeEnum.CURRENT;
        account.setAccountType(accountType);

        Account.ActiveEnum active = Account.ActiveEnum.ACTIVE;
        account.setActive(active);

        BigDecimal balance = new BigDecimal(new Random().nextDouble());
        account.setBalance(balance);

        String iban = ibanGenerator.GenerateIban();
        account.setIBAN(iban);

        User user = new User();
        account.setUser(user);

        AccountDTO dto = account.toAccountDTO();
        assertEquals(account.getAbsoluteLimit(), dto.getAbsoluteLimit());
        assertEquals(account.getAccountType().toString(), dto.getAccountType().toString());
        assertEquals(account.getActive().toString(), dto.getActive().toString());
        assertEquals(account.getBalance(), dto.getBalance());
        assertEquals(account.getIBAN(), dto.getIBAN());
        assertEquals(user.getId(), dto.getUserid());
    }
    
}
