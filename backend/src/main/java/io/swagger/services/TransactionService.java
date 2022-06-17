package io.swagger.services;

import io.swagger.exception.custom.InvalidTransactionsException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.TransactionDeniedException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.*;
import io.swagger.repositories.AccountRepository;
import io.swagger.repositories.TransactionRepository;
import io.swagger.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Service
public class TransactionService {

    @Value("${server.bank.iban}")
    private String bankIban;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    ////Repository methods////
    //Add a new transaction object to the database
    public void addTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    //Get all transactions from the database
    public List<Transaction> getAllTransactions(){
        return (List<Transaction>) transactionRepository.findAll();
    }

    //Get a transaction by id
    public Transaction getTransactionById(UUID id){
        return transactionRepository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    //Get all transactions from a specific user
    public List<Transaction> getTransactions(String iban) {return (List<Transaction>) transactionRepository.findByIBAN(iban);}

    //check if transaction is exists
    public boolean transactionExists(UUID id){
        return transactionRepository.existsById(id);
    }

    //Get today's transactions from a user
    public List<Transaction> getTodaysTransactions(Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        return (List<Transaction>) transactionRepository.findByIBANAndTimestamp(transaction.getIBAN(), now);
    }

    //findByIBANAndTimestampBetween()
    public List<Transaction> findByIBANAndTimestampBetween(String iban, LocalDateTime startDate, LocalDateTime endDate){
        return (List<Transaction>) transactionRepository.findByIBANAndTimestampBetween(iban, startDate, endDate);
    }

    private Transaction executeTransaction(Transaction transaction) {
        transaction.execute();
        transactionRepository.save(transaction);
        //save accounts
        accountRepository.save(transaction.getOrigin());
        accountRepository.save(transaction.getTarget());
        return transaction;
    }

    ////Validation methods////
    public void isValidTransaction(Transaction transaction){
        if(!validateIsOwner(transaction) &&  transaction.getType() == TransactionType.TRANSFER) {
            throw new UnauthorizedException("You do not have access to this account");
        }
        else if(!validatePinCode(transaction)){
            throw new UnauthorizedException("Invalid Pin code");
        }
        else if(!validateIsActive(transaction)){
            throw new InvalidTransactionsException("Account is not active");
        }
        else if(!validateSavingsAccount(transaction)){
            throw new InvalidTransactionsException("Savings account cannot be used for this transaction");
        }
        else if(!validateThatOriginIsNotTarget(transaction)){
            throw new InvalidTransactionsException("Cannot transfer money to the same account");
        }
        else if(!validateBalance(transaction)){
            throw new TransactionDeniedException("Insufficient funds for this transaction");
        }
        else if(!validateTransactionLimit(transaction)){
            throw new TransactionDeniedException("Transaction limit exceeded");
        }
        else if(!validateDayLimit(transaction) && transaction.getType() != TransactionType.DEPOSIT){
            throw new TransactionDeniedException("Daily limit exceeded");
        }
        else if(!validateNotNegative(transaction)){
            throw new IllegalArgumentException("Amount can not be negative!");
        }
    }

    private boolean validateNotNegative(Transaction transaction){
        return transaction.getAmount().doubleValue() > 0;
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
    private boolean validatePinCode(Transaction transaction) {
        return transaction.getPerformer().getPincode().equals(transaction.getPincode());
    }

    private boolean validateDayLimit(Transaction transaction){
        if(transaction.getPerformer().getRoles().contains(Role.ROLE_ADMIN) && !transaction.getOrigin().getIBAN().equals(bankIban)) {
            return true;
        }
       //check if performer is admin and if so, return true
        if(transaction.getPerformer().getRoles().contains(Role.ROLE_ADMIN)  && !transaction.getOrigin().getUser().getId().equals(transaction.getPerformer().getId())) {
            //if performer owns origin
            return true;
        }
        //get all transactions from origin
        List<Transaction> transactions = getTodaysTransactions(transaction);
        if(transactions.isEmpty()){
            return true;
        }
        //get sum of all transactions
        BigDecimal sum = BigDecimal.ZERO;
        for(Transaction t : transactions){
            sum = sum.add(t.getAmount());
        }
        //check if sum is greater than day limit
        return sum.add(transaction.getAmount()).doubleValue() <= transaction.getPerformer().getDayLimit().doubleValue();
    }

    private boolean validateTransactionLimit(Transaction transaction){
        //check if transaction limit is not exceeded
        if(transaction.getOrigin().getIBAN().equals(bankIban) && transaction.getPerformer().getRoles().contains(Role.ROLE_ADMIN) && transaction.getType() != TransactionType.DEPOSIT){
            return true;
        }
        else if(transaction.getType() == TransactionType.DEPOSIT){
            return transaction.getTarget().getUser().getTransactionLimit().doubleValue() >= transaction.getAmount().doubleValue();
        }
        else{
            return transaction.getOrigin().getUser().getTransactionLimit().doubleValue() >= transaction.getAmount().doubleValue();

        }
    }
    private boolean validateSavingsAccount(Transaction transaction){
        //check if origin is a savings account
        if(transaction.getOrigin().getAccountType() == Account.AccountTypeEnum.SAVINGS || transaction.getTarget().getAccountType() == Account.AccountTypeEnum.SAVINGS){
            //origin mus have the same owner as the target
            return transaction.getOrigin().getUser().getId().equals(transaction.getTarget().getUser().getId());
        }
        else {
            //this part of the transaction is approved
            return true;
        }
    }
    private boolean validateThatOriginIsNotTarget(Transaction transaction){
        return !transaction.getOrigin().getId().equals(transaction.getTarget().getId());
    }

    //check if performer is the owner of the account
    private boolean validateIsOwner(Transaction transaction){
        //check if performer is an admin
        if(transaction.getPerformer().getRoles().contains(Role.ROLE_ADMIN)){
            return true;
        }
        else {
            //check if performer is the owner of the account
            return transaction.getOrigin().getUser().getId().equals(transaction.getPerformer().getId());
        }
    }

    public User getUserFromSecurityContext(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //get user if not null
        User user = userRepository.findByUsername(name).orElseThrow(() -> new NotFoundException("User not found"));
        if(user == null){
            throw new UnauthorizedException("Could not find user from provided token");
        }
        return user;
    }

    private void validateAccessToAccount (String iban, User user){
        List<Account> accountList = user.getAccounts();
        if(accountList.isEmpty()){
            throw new InvalidTransactionsException("User has no accounts");
        }
        if (accountList.stream().noneMatch(a -> a.getIBAN().equals(iban)) && !user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new UnauthorizedException("You are not the owner of this account");
        }
    }

    //////Do Transaction//////
    public Transaction doTransaction(PostTransactionDTO transaction, TransactionType type) {
        //get curent user from security context
        User user = getUserFromSecurityContext();
        //check if user is owner of the account or is admin
        if(type != TransactionType.DEPOSIT){
            validateAccessToAccount(transaction.getFromIBAN(), user);
        }
        else{
            validateAccessToAccount(transaction.getToIBAN(), user);
        }

        Transaction newTransaction = createTransactionFromPostTransaction(transaction, user, type);
        //validate transaction
        isValidTransaction(newTransaction);
        return executeTransaction(newTransaction);
    }

    private Transaction createTransactionFromPostTransaction(PostTransactionDTO transaction, User user, TransactionType type) {
        //create transaction object
        Transaction t = new Transaction();
        t.setPerformer(user);
        if(type == TransactionType.DEPOSIT){
            t.setIBAN(transaction.getToIBAN());
        }
        else{
            t.setIBAN(transaction.getFromIBAN());
        }
        t.setType(type);
        //set origin and target if they exist, otherwise throw exception
        if(accountService.findByIBAN(transaction.getFromIBAN()) == null){
            throw new IllegalArgumentException("Given iban for origin account does not exist");
        }
        t.setOrigin((Account) accountService.findByIBAN(transaction.getFromIBAN()));
        if(accountService.findByIBAN(transaction.getToIBAN()) == null){
            throw new IllegalArgumentException("Given iban for target account does not exist");
        }
        t.setTarget((Account) accountService.findByIBAN(transaction.getToIBAN()));
        t.setAmount(transaction.getAmount());
        t.setPincode(transaction.getPincode());
        t.setType(type);
        return t;
    }

    /////Get History/////
    public List<GetTransactionDTO> getHistory(String iban) {
        validationForGetHistory(iban);
        List<Transaction> transactions = getTransactions(iban);
        return transactions.stream().map(Transaction::toGetTransactionDTO).collect(java.util.stream.Collectors.toList());
    }

    public List<GetTransactionDTO> getHistory(String iban, String date1, String date2) {
        validationForGetHistory(iban);
        LocalDateTime dateOne = LocalDateTime.of(LocalDate.parse(date1), LocalTime.of(0, 0, 0));
        LocalDateTime dateTwo = LocalDateTime.of(LocalDate.parse(date2), LocalTime.of(0, 0, 0));
        List<Transaction> transactions = findByIBANAndTimestampBetween(iban, dateOne, dateTwo);
        if(transactions.isEmpty()){
            throw new IllegalArgumentException("No transactions found for given period: " + date1 + " - " + date2);
        }
        return transactions.stream().map(Transaction::toGetTransactionDTO).collect(java.util.stream.Collectors.toList());
    }

    private void validationForGetHistory(String iban){
        if (accountService.findByIBAN(iban) == null) {
            throw new IllegalArgumentException("Account does not exist");
        }
        //get current user from security context
        User user = getUserFromSecurityContext();
        //check if user is owner of the account or is admin
        validateAccessToAccount(iban, user);
        if (getTransactions(iban).isEmpty()) {
            throw new NotFoundException("Account has no transactions");
        }
    }

}
