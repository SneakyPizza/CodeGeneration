package io.swagger.services;

import io.swagger.model.AccountDTO;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.entities.Account;
import io.swagger.repositories.AccountRepository;

import java.util.UUID;

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

    //Add a new account object to the database (POST)
    
    public void addAccount(PostAccountDTO account) {
        //if(accountRepository.findByIBAN(account.getIBAN()) == null){
            //accountRepository.save(account);
        //} 
        //else {
            //throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "IBAN already in use");
        //}
    }
    

    //Get all accounts (GET)
    public Iterable<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    
    //Get a Account with IBAN (GET)
    public Account getAccountWithIBAN (String iban){
        if(accountRepository.findByIBAN(iban) != null){
            //return accountRepository.findByIBAN(iban);
            return (Account) accountRepository.findByIBAN(iban);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with given Iban was not found");
        }
    }

    //Update a existing account with a new account object (PUT)
    public void updateAccount (Account account){
        if(accountRepository.findById(account.getId()) != null){
            accountRepository.save(account); //Not sure if this updates the account corresponding with he UUID or adds another account.
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }
    public Object findByIBAN(String iban) {return accountRepository.findByIBAN(iban);}
}
