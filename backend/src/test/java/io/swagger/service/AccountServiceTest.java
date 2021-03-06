package io.swagger.service;

import io.swagger.exception.custom.NotFoundException;
import io.swagger.model.AccountDTO;
import io.swagger.model.dto.NameSearchAccountDTO;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.dto.PostAccountDTO.AccountTypeEnum;
import io.swagger.model.dto.PostAccountDTO.ActiveEnum;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.UserRepository;
import io.swagger.services.AccountService;
import io.swagger.services.UserService;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private SecurityContext securityContext;

    public User testUser;
    public User adminUser;
    public PostAccountDTO postaccountdto;
    public AccountDTO accountdto;
    public String fullname;

    @BeforeEach
    void setup(){
        Authentication authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        //accountService = new accountService();
        testUser = new User();
        adminUser = new User();

        testUser = userRepository.findByUsername("test").orElseThrow(() -> new NotFoundException("User not found"));;
        adminUser = userRepository.findByUsername("Bank").orElseThrow(() -> new NotFoundException("User not found"));;
        fullname = new String("test-test");

        postaccountdto = new PostAccountDTO();
        postaccountdto.setAbsoluteLimit(new BigDecimal(1337));
        postaccountdto.setAccountType(AccountTypeEnum.CURRENT);
        postaccountdto.setActive(ActiveEnum.ACTIVE);
        postaccountdto.setUserid(adminUser.getId());

        accountdto = new AccountDTO();
        accountdto.setAbsoluteLimit(new BigDecimal(1337));
        accountdto.setAccountType(AccountDTO.AccountTypeEnum.SAVINGS);
        accountdto.setActive(AccountDTO.ActiveEnum.ACTIVE);
        accountdto.setUserid(testUser.getId());
        accountdto.setBalance(new BigDecimal(0));
        
    }

    @Test
    public void A_addAccount(){
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");

        int amount = accountService.getAllAccounts(adminUser).size();
        accountService.addAccount(postaccountdto, adminUser);
        Assertions.assertEquals((amount + 1), accountService.getAllAccounts(adminUser).size());
    }

    @Test
    public void B_getAllAccounts(){
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");
        testUser = userRepository.findByUsername("Bank").orElseThrow(() -> new NotFoundException("User not found"));
        //List<NameSearchAccountDTO> dtos = accountService.searchAccountDTOs(fullname, 10, 10, testUser);
        List<AccountDTO> dtos = accountService.getAllAccounts(testUser);
        Assertions.assertEquals(dtos.size(), accountService.getAllAccounts(testUser).size());
    }

    @Test
    public void C_getAccountDTOWithIBAN(){
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");

        String iban = accountService.getAllAccounts(adminUser).get(0).getIBAN();
        accountdto = accountService.getAccountDTOWithIBAN(iban, adminUser);
        Assertions.assertEquals(iban, accountdto.getIBAN());
    }

    @Test
    public void D_searchAccountDTOs(){
        when(securityContext.getAuthentication().getName()).thenReturn("test");
        testUser = userRepository.findByUsername("test").orElseThrow(() -> new NotFoundException("User not found"));
        List<NameSearchAccountDTO> dtos = accountService.searchAccountDTOs(fullname, 10, 10, testUser);
        Assertions.assertEquals(dtos.size(), accountService.searchAccountDTOs(fullname, 10, 10, testUser).size());
    }

    @Test
    public void E_updateAccount(){
        when(securityContext.getAuthentication().getName()).thenReturn("Bank");

        String iban = accountService.getAllAccounts(adminUser).get(0).getIBAN();
        accountdto = new AccountDTO();
        accountdto.setAbsoluteLimit(new BigDecimal(1337));
        accountdto.setAccountType(AccountDTO.AccountTypeEnum.SAVINGS);
        accountdto.setActive(AccountDTO.ActiveEnum.ACTIVE);
        accountdto.setUserid(testUser.getId());
        accountdto.setBalance(new BigDecimal(0));
        accountdto.setIBAN(iban);

        accountService.updateAccount(iban, accountdto, adminUser);
        AccountDTO test = accountService.getAccountDTOWithIBAN(iban, adminUser);
        
        Assertions.assertEquals(accountdto.getAbsoluteLimit(), test.getAbsoluteLimit());
        Assertions.assertEquals(accountdto.getAccountType(), test.getAccountType());
        Assertions.assertEquals(accountdto.getActive(), test.getActive());
        Assertions.assertEquals(accountdto.getBalance(), test.getBalance());
        Assertions.assertEquals(accountdto.getIBAN(), test.getIBAN());
        //Assertions.assertEquals(accountdto.getUserid(), test.getUserid());
    }

    
    
}
