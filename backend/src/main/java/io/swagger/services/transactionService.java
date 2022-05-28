package io.swagger.services;

import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.User;
import io.swagger.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Iterable<Transaction> getTransactionsByUserId(UUID userId){ return transactionRepository.findByfromUserId(userId);}

    public List<Transaction> getTransactions(String iban) {return (List<Transaction>) transactionRepository.findByIBAN(iban);}

    //check if a transaction is valid
    public boolean isValidTransaction(Transaction transaction){
        return validateDayLimit(transaction) && validateTransactionLimit(transaction) && validateBalance(transaction) && validateAbsoluteLimit(transaction) && validateSavingsAccount(transaction) && validateThatOriginIsNotTarget(transaction);
    }

    private boolean validateBalance(Transaction transaction) {
        //check if origin has enough money
        if(transaction.getOrigin().getBalance().compareTo(transaction.getAmount()) < 0){
            return false;
        }
        return false;
    }

    public boolean validateDayLimit(Transaction transaction){
        return false;
    }
    public boolean validateTransactionLimit(Transaction transaction){
        return false;
    }
    public boolean validateAbsoluteLimit(Transaction transaction){
        return false;
    }
    public boolean validateSavingsAccount(Transaction transaction){
        return false;
    }
    public boolean validateThatOriginIsNotTarget(Transaction transaction){
        return false;
    }

    //Falidate admin acces nog toevoegen

}
