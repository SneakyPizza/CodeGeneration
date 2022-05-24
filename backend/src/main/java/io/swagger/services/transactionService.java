package io.swagger.services;

import io.swagger.model.entities.Transaction;
import io.swagger.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class transactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    //Add a new transaction object to the database
    public void addTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    //Get all transactions from the database
    public Iterable<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    //Get a transaction by id
    public Transaction getTransactionById(UUID id){ return transactionRepository.findById(id).get(); }

    //Get all transactions from a specific user
    public Iterable<Transaction> getTransactionsByUserId(UUID userId){
        return transactionRepository.findByfromUserId(userId);
    }

    //Get all transaction by fromIban
//    public Iterable<Transaction> getTransactionsByFromIban(String fromIban){
//        return transactionRepository.findByfromIban(fromIban);
//    }

}
