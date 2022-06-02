package io.swagger.model;

import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.TransactionType;
import io.swagger.model.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class transactionTest {

    private Transaction transaction;

    @BeforeEach
    void setup() {
         transaction = new Transaction();
    }
    @Test
    void setIdToAGeneratedUUID() {
        UUID id = UUID.randomUUID();
        transaction.setId(id);
        assertEquals(id, transaction.getId());
    }

    @Test
    void setIdToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setId(null));
    }

    @Test
    void setIbanToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setIBAN(null));
    }

    @Test
    void setIban() {
        String iban = "NL01INH10000000001";
        transaction.setIBAN(iban);
        assertEquals(iban, transaction.getIBAN());
    }

    @Test
    void setTargetToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setTarget(null));
    }

    @Test
    void setTarget() {
        Account account = new Account();
        transaction.setTarget(account);
        assertEquals(account, transaction.getTarget());
    }

    @Test
    void setPincodeToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setPincode(null));
    }

    @Test
    void setPincode() {
        String pincode = "1234";
        transaction.setPincode(pincode);
        assertEquals(pincode, transaction.getPincode());
    }

    @Test
    void setAmountToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setAmount(null));
    }

    @Test
    void setAmountToNegative() {
        assertThrows(IllegalArgumentException.class, () -> transaction.setAmount(BigDecimal.valueOf(-1)));
    }

    @Test
    void setAmount() {
        BigDecimal amount = new BigDecimal(new Random().nextDouble());
        transaction.setAmount(amount);
        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void setTimestampToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setTimestamp(null));
    }

    @Test
    void setTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        transaction.setTimestamp(timestamp);
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void setOriginToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setOrigin(null));
    }

    @Test
    void setOrigin() {
        Account account = new Account();
        transaction.setOrigin(account);
        assertEquals(account, transaction.getOrigin());
    }

    @Test
    void setPerformerToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setPerformer(null));
    }

    @Test
    void setPerformer() {
        User user = new User();
        transaction.setPerformer(user);
        assertEquals(user, transaction.getPerformer());
    }

    @Test
    void toGetTransactionDTO() {
        String iban = "NL01INH10000000001";
        Account account = new Account();
        account.setIBAN(iban);
        transaction.setIBAN(iban);
        BigDecimal b = new BigDecimal(new Random().nextDouble());
        transaction.setAmount(b);
        LocalDateTime l = LocalDateTime.now();
        transaction.setTimestamp(l);
        User user = new User();
        transaction.setTarget(account);
        transaction.setOrigin(account);
        transaction.setPerformer(user);
        account.setUser(user);
        GetTransactionDTO transactionDTO = transaction.toGetTransactionDTO();
        assertEquals(iban, transactionDTO.getFromIBAN());
        assertEquals(iban, transactionDTO.getToIBAN());
        assertEquals(b, transactionDTO.getAmount());
        //timestamp must be formatted
        assertEquals(l.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), LocalDateTime.parse(transactionDTO.getTimestamp()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertEquals(user.getId(), transactionDTO.getFromUserId());
    }

    @Test
    void toGetTransactionDTOWithNull() {
        assertThrows(NullPointerException.class, () -> transaction.toGetTransactionDTO());
    }

    //testing type
    @Test
    void setTypeToNull() {
        assertThrows(NullPointerException.class, () -> transaction.setType(null));
    }

    @Test
    void setType() {
        TransactionType type = TransactionType.TRANSFER;
        transaction.setType(type);
        assertEquals(type, transaction.getType());
    }

}
