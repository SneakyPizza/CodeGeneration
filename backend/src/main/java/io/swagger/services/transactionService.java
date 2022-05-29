package io.swagger.services;

import io.swagger.model.entities.*;
import io.swagger.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public TransactionValidation isValidTransaction(Transaction transaction){
        if(!validateBalance(transaction)){
            return new TransactionValidation(false, "Insufficient funds");
        }
        else if(!validateTransactionLimit(transaction)){
            return new TransactionValidation(false, "Transaction limit exceeded");
        }
        else if(!validateDayLimit(transaction)){
            return new TransactionValidation(false, "Day limit exceeded");
        }
        else if(!validateSavingsAccount(transaction)){
            return new TransactionValidation(false, "Savings account cannot be used for this transaction");
        }
        else if(!validateThatOriginIsNotTarget(transaction)){
            return new TransactionValidation(false, "Cannot transfer money to the same account");
        }
        else if(!validateIsOwner(transaction)){
            return new TransactionValidation(false, "You do not have access to this account");
        }
        else{
            return new TransactionValidation(true, "Transaction is valid");
        }
    }

    private boolean validateBalance(Transaction transaction) {
        //check if origin has enough money
        return transaction.getOrigin().getBalance().subtract(transaction.getAmount()).doubleValue() >= transaction.getOrigin().getAbsoluteLimit().doubleValue();
    }

    public boolean validateDayLimit(Transaction transaction){
        //check if day limit is not exceeded
        return transaction.getOrigin().getUser().getDayLimit().subtract(transaction.getAmount()).doubleValue() >= 0;
    }
    public boolean validateTransactionLimit(Transaction transaction){
        //check if transaction limit is not exceeded
        return transaction.getOrigin().getUser().getTransactionLimit().doubleValue() >= transaction.getAmount().doubleValue();
    }
    public boolean validateSavingsAccount(Transaction transaction){
        //only the owner of the savings account can transfer money to it or from it.
        //check if origin is a savings account
        if(transaction.getOrigin().getAccountType() == Account.AccountTypeEnum.SAVINGS || transaction.getTarget().getAccountType() == Account.AccountTypeEnum.SAVINGS){
            //origin mus have have the same owner as the target
            return transaction.getOrigin().getUser().getId().equals(transaction.getTarget().getUser().getId());
        }
        else {
            //this part of the transaction is approved
            return true;
        }
    }
    public boolean validateThatOriginIsNotTarget(Transaction transaction){
        return !transaction.getOrigin().getId().equals(transaction.getTarget().getId());
    }

    //check if performer is the owner of the account
    public boolean validateIsOwner(Transaction transaction){
        //check if performer is an admin
        if(transaction.getPerformer().getRoles().contains(Role.ROLE_ADMIN)){
            return true;
        }
        else {
            //check if performer is the owner of the account
            return transaction.getOrigin().getUser().getId().equals(transaction.getPerformer().getId());
        }
    }

}
