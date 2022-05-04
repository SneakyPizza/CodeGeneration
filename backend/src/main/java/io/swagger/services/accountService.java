package io.swagger.services;

import io.swagger.model.entities.Account;
import io.swagger.repositories.accountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class accountService {
    @Autowired
    private accountRepository accountRepository;

    //Add a new account object to the database
    public void addAccount(Account account) {
        accountRepository.save(account);
    }
    
}
