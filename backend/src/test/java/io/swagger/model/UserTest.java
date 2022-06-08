package io.swagger.model;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Role;
import io.swagger.model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
    }

    @Test
    void setIdToAGeneratedUUID() {
        UUID id = UUID.randomUUID();
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void setIdToNull() {
        assertThrows(NullPointerException.class, () -> user.setId(null));
    }

    @Test
    void setUsernameToNull() {
        assertThrows(NullPointerException.class, () -> user.setUsername(null));
    }

    @Test
    void setUsername() {
        String username = "username";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    void setPasswordToNull() {
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    @Test
    void setPassword() {
        String password = "password";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    void setEmailToNull() {
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
    }

    @Test
    void setEmail() {
        String email = "test@test.nl";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    void setFirstNameToNull() {
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    }

    @Test
    void setFirstName() {
        String firstName = "firstName";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void setLastNameToNull() {
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
    }

    @Test
    void setLastName() {
        String lastName = "lastName";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void setStreetToNull() {
        assertThrows(NullPointerException.class, () -> user.setStreet(null));
    }

    @Test
    void setStreet() {
        String street = "street";
        user.setStreet(street);
        assertEquals(street, user.getStreet());
    }

    @Test
    void setCityToNull() {
        assertThrows(NullPointerException.class, () -> user.setCity(null));
    }

    @Test
    void setCity() {
        String city = "city";
        user.setCity(city);
        assertEquals(city, user.getCity());
    }

    @Test
    void setZipcodeToNull() {
        assertThrows(NullPointerException.class, () -> user.setZipcode(null));
    }

    @Test
    void setZipcode() {
        String zipcode = "1234";
        user.setZipcode(zipcode);
        assertEquals(zipcode, user.getZipcode());
    }

    @Test
    void setDayLimitToNull() {
        assertThrows(NullPointerException.class, () -> user.setDayLimit(null));
    }

    @Test
    void setDayLimit() {
        BigDecimal dayLimit = BigDecimal.valueOf(10);
        user.setDayLimit(dayLimit);
        assertEquals(dayLimit, user.getDayLimit());
    }

    @Test
    void setDaylimitToNegative() {
        assertThrows(IllegalArgumentException.class, () -> user.setDayLimit(BigDecimal.valueOf(-10)));
    }

    @Test
    void setTransactionLimitToNull() {
        assertThrows(NullPointerException.class, () -> user.setTransactionLimit(null));
    }

    @Test
    void setTransactionLimit() {
        BigDecimal transactionLimit = BigDecimal.valueOf(10);
        user.setTransactionLimit(transactionLimit);
        assertEquals(transactionLimit, user.getTransactionLimit());
    }

    @Test
    void setTransactionLimitToNegative() {
        assertThrows(IllegalArgumentException.class, () -> user.setTransactionLimit(BigDecimal.valueOf(-10)));
    }

    @Test
    void setPincodeToNull() {
        assertThrows(NullPointerException.class, () -> user.setPincode(null));
    }

    @Test
    void setPincode() {
        String pincode = "1234";
        user.setPincode(pincode);
        assertEquals(pincode, user.getPincode());
    }

    @Test
    void setAccounts() {
        Account account = new Account();
        account.setUser(user);
        account.setIBAN("NL12INGB000123456789");
        account.setBalance(BigDecimal.valueOf(10));
        account.setActive(Account.ActiveEnum.ACTIVE);
        account.setAbsoluteLimit(BigDecimal.valueOf(10));
        List<Account> accounts = List.of(account);
        user.setAccounts(accounts);
        assertEquals(accounts, user.getAccounts());
    }

    @Test
    void setRolesToNull() {
        assertThrows(NullPointerException.class, () -> user.setRoles(null));
    }

    @Test
    void setRoles() {
        List<Role> roles = List.of(Role.ROLE_USER);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }
}
