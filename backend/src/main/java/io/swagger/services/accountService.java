package io.swagger.services;

import io.swagger.model.AccountDTO;
import io.swagger.model.AccountDTO.AccountTypeEnum;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.utils.ibanGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

@Service
public class accountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    private ibanGenerator ibanGen = new ibanGenerator();

    //Add a new account object to the database (POST)
    public void addAccount(PostAccountDTO account) {
        Account a = new Account<User>();
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setBalance(new BigDecimal(0));
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setUser(userService.getUser(account.getUserid()));
        a.setIBAN(ibanGen.GenerateIban());
        accountRepository.save(a);
    }

    public boolean validateIban(String IBAN){
        return ibanGen.ValidateIban(IBAN);
    }
    

    //Get all accounts (GET)
    public List<Account> getAllAccounts(){
        return (List<Account>) accountRepository.findAll();
    }

    
    //Get a Account with IBAN (GET)
    public Account getAccountWithIBAN (String iban){
        return (Account) accountRepository.findByIBAN(iban);
    }

    public AccountDTO getAccountDTOWithIBAN(String iban){
        Account a = getAccountWithIBAN(iban);
        return a.toAccountDTO();
    }

    //Update a existing account with a new account object (PUT)
    /*
    public void updateAccount (Account account){
        if(accountRepository.findById(account.getId()) != null){
            accountRepository.save(account); //Not sure if this updates the account corresponding with he UUID or adds another account.
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }*/
    
    //update an account object to the database
    public void updateAccount(String iban, AccountDTO account) {
        Account a = getAccountWithIBAN(iban);
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setIBAN(account.getIBAN());
        accountRepository.save(a);
    }

    public List<Account> findByUserId(UUID userid){
        return accountRepository.findByUserId(userid);
    }
    public Object findByIBAN(String iban) {return accountRepository.findByIBAN(iban);}

}
