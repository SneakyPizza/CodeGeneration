package io.swagger.services;

import io.swagger.model.entities.*;
import io.swagger.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public List<Transaction> getAllTransactions(){
        return (List<Transaction>) transactionRepository.findAll();
    }

    //Get a transaction by id
    public Transaction getTransactionById(UUID id){ return transactionRepository.findById(id).get(); }

    //Get all transactions from a specific user
    public List<Transaction> getTransactions(String iban) {return (List<Transaction>) transactionRepository.findByIBAN(iban);}

    //Get today's transactions from a user
    public List<Transaction> getTodaysTransactions(Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        return (List<Transaction>) transactionRepository.findByIBANAndTimestamp(transaction.getIBAN(), now);
    }

    //check if a transaction is valid
    public TransactionValidation isValidTransaction(Transaction transaction){
        if(!validateIsOwner(transaction)){
            return new TransactionValidation(false, "You do not have access to this account",TransactionValidation.TransactionValidationStatus.UNAUTHORIZED);
        }
        else if(!validatePINcode(transaction)){
            return new TransactionValidation(false, "PIN code is incorrect",TransactionValidation.TransactionValidationStatus.INVALID_PIN);
        }
        else if(!validateIsActive(transaction)){
            return new TransactionValidation(false, "Both accounts need to be active",TransactionValidation.TransactionValidationStatus.NOT_ACTIVE);
        }
        else if(!validateSavingsAccount(transaction)){
            return new TransactionValidation(false, "Savings account cannot be used for this transaction", TransactionValidation.TransactionValidationStatus.NOT_ALLOWED);
        }
        else if(!validateThatOriginIsNotTarget(transaction)){
            return new TransactionValidation(false, "Cannot transfer money to the same account", TransactionValidation.TransactionValidationStatus.NOT_ALLOWED);
        }
        else if(!validateBalance(transaction)){
            return new TransactionValidation(false, "Insufficient funds", TransactionValidation.TransactionValidationStatus.INSUFFICIENT_FUNDS);
        }
        else if(!validateTransactionLimit(transaction)){
            return new TransactionValidation(false, "Transaction limit exceeded", TransactionValidation.TransactionValidationStatus.TRANSACTION_LIMIT_EXCEEDED);
        }
        else if(!validateDayLimit(transaction)){
            return new TransactionValidation(false, "Day limit exceeded", TransactionValidation.TransactionValidationStatus.DAILY_LIMIT_EXCEEDED);
        }
        else{
            return new TransactionValidation(true, "Transaction is valid", TransactionValidation.TransactionValidationStatus.VALID);
        }
    }
    //validate that origin and target are active accounts
    private boolean validateIsActive(Transaction transaction){
        //check if origin and target are active
        return transaction.getOrigin().getActive().equals(Account.ActiveEnum.ACTIVE) && transaction.getTarget().getActive().equals(Account.ActiveEnum.ACTIVE);
    }


    private boolean validateBalance(Transaction transaction) {
        //check if origin has enough money
        return transaction.getOrigin().getBalance().subtract(transaction.getAmount()).doubleValue() >= transaction.getOrigin().getAbsoluteLimit().doubleValue();
    }
    //validate pin
    private boolean validatePINcode(Transaction transaction) {
        return transaction.getPerformer().getPincode().equals(transaction.getPincode());
    }

    public boolean validateDayLimit(Transaction transaction){
       //check if performer is admin and if so, return true
        if(transaction.getPerformer().getRoles().contains("ROLE_ADMIN")){
           //if performer owns origin
            if(transaction.getOrigin().getUser().getId().equals(transaction.getPerformer().getId())){
                //check if origin has exceeded day limit
                //get all transactions from origin
                List<Transaction> transactions = getTodaysTransactions(transaction);
                //get sum of all transactions
                BigDecimal sum = BigDecimal.ZERO;
                for(Transaction t : transactions){
                    sum = sum.add(t.getAmount());
                }
                //check if sum is greater than day limit
                return sum.add(transaction.getAmount()).doubleValue() <= transaction.getPerformer().getDayLimit().doubleValue();
            }
            else{
                return true;
            }
        }
        else{
            //check if origin has exceeded day limit
            //get all transactions from origin
            List<Transaction> transactions = getTodaysTransactions(transaction);
            //get sum of all transactions
            BigDecimal sum = BigDecimal.ZERO;
            for(Transaction t : transactions){
                sum = sum.add(t.getAmount());
            }
            //check if sum is greater than day limit
            return sum.add(transaction.getAmount()).doubleValue() <= transaction.getPerformer().getDayLimit().doubleValue();
        }
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
