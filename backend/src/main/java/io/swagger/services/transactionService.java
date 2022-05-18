package io.swagger.services;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import io.swagger.repositories.accountRepository;
import io.swagger.repositories.transactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class transactionService {
    @Autowired
    private transactionRepository transactionRepository;

    //Add a new transaction object to the database
    public void addTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    //Get all transactions from the database
    public Iterable<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

}
