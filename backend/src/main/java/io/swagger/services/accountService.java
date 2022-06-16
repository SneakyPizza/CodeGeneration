package io.swagger.services;

import io.swagger.exeption.custom.InvalidIbanException;
import io.swagger.exeption.custom.UnauthorizedException;
import io.swagger.model.AccountDTO;
import io.swagger.model.GetUserDTO.Role;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.User;
import io.swagger.repositories.AccountRepository;
import io.swagger.utils.ibanGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class accountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    private ibanGenerator ibanGen = new ibanGenerator();

    //Add a new account object to the database (POST)
    public PostAccountDTO addAccount(PostAccountDTO account, User user) {
        validateUserRoleAdmin(user);
        validatePostAccountDTO(account);

        Account<User> a = new Account<>();
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setBalance(new BigDecimal(0));
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setUser(userService.getUser(account.getUserid()));
        a.setIBAN(ibanGen.GenerateIban());
        accountRepository.save(a);
        return account;
    }


    
    //Get all accounts (GET)
    public List<AccountDTO> getAllAccounts(User user){
        validateUserRoleAdmin(user);
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        List<AccountDTO> dto = new ArrayList();
        for(Account account : accounts){
            dto.add(account.toAccountDTO());
        }
        return dto;
    }

    
    //Get a Account with IBAN (GET)
    public AccountDTO getAccountDTOWithIBAN(String iban, User user){
        validateUserRoleAdmin(user);
        validateIban(iban);
        Account<User> account = getAccountWithIBAN(iban);
        if(account == null){
            throw new IllegalArgumentException("account is not found");
        }
        return account.toAccountDTO();
    }

    //update an account object to the database
    public AccountDTO updateAccount(String iban, AccountDTO account, User user) {
        validateIban(iban);
        validateUserRoleAdmin(user);
        validateAccountDTO(account);
        //validate accountdto
        Account<User> a = getAccountWithIBAN(iban);
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setIBAN(account.getIBAN());
        accountRepository.save(a);
        return account;
    }

    public List<Account> findByUserId(UUID userid){
        return accountRepository.findByUserId(userid);
    }
    public Object findByIBAN(String iban) {return accountRepository.findByIBAN(iban);}

    //Boolean checks / Validations
    
    private boolean checkIfUserRoleAdmin(User user){
        return user.getRoles().contains(Role.ADMIN);
    }

    private boolean checkIfUserRoleUser(User user){
        return user.getRoles().contains(Role.USER);
    }

    private boolean checkIban(String iban){
        return ibanGen.ValidateIban(iban);
    }

    private boolean getAccountdtoNullCheck(AccountDTO accountdto){
        if(accountdto == null) throw new NullPointerException("accountdto is null");
        return accountdto != null;
    }

    private boolean getAccountNullCheck(Account account){
        if(account == null) throw new NullPointerException("account is null");
        return account != null;
    }

    private boolean checkPostAccountType(PostAccountDTO postaccountdto){
        int enumcheck = 0;
        for (PostAccountDTO.AccountTypeEnum accountType : PostAccountDTO.AccountTypeEnum.values()){
            if(accountType.name().equalsIgnoreCase(postaccountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 0;
    }

    private boolean checkPostAccountActive(PostAccountDTO postaccountdto){
        int enumcheck = 0;
        for (PostAccountDTO.ActiveEnum accountType : PostAccountDTO.ActiveEnum.values()){
            if(accountType.name().equalsIgnoreCase(postaccountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 0;
    }

    private boolean checkAccountType(AccountDTO accountdto) {
        int enumcheck = 0;
        for (AccountDTO.AccountTypeEnum accountType : AccountDTO.AccountTypeEnum.values()){
            if(accountType.name().equalsIgnoreCase(accountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 0;
    }

    private boolean checkAccountActive(AccountDTO accountdto) {
        int enumcheck = 0;
        for (AccountDTO.ActiveEnum accountType : AccountDTO.ActiveEnum.values()){
            if(accountType.name().equalsIgnoreCase(accountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 0;
    }

    private void validateUserRoleAdmin(User user){
        if(!checkIfUserRoleAdmin(user)){
            throw new UnauthorizedException("You need to be a admin to preform this action");
        }
    }

    private void validateUserRoleUser(User user){
        if(!checkIfUserRoleUser(user) || !checkIfUserRoleAdmin(user)){
            throw new UnauthorizedException("You need to be a User to preform this action");
        }
    }

    private void validateIban(String iban){
        if(!checkIban(iban)){
            throw new InvalidIbanException("Invalid iban");
        }
    }

    private Account<User> getAccountWithIBAN (String iban){
        return (Account<User>) accountRepository.findByIBAN(iban);
    }

    public void validatePostAccountDTO(PostAccountDTO postaccountdto){
        if(postaccountdto.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unable to set limit below ZERO");
        } else if(!userService.checkIfUserExists(postaccountdto.getUserid())){
            //User not found exception
        } else if(!checkPostAccountType(postaccountdto)){
            //invalid accounttype
        } else if(!checkPostAccountActive(postaccountdto)){
            // invalid active status
        }
    }

    public void validateAccountDTO(AccountDTO accountdto){
        if(accountdto.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unable to set limit below ZERO");
        } else if(!userService.checkIfUserExists(accountdto.getUserid())){
            //user not found exception
        } else if(!checkAccountType(accountdto)){
            //Invalid accounttype
        } else if(!checkAccountActive(accountdto)){
            //invalid active status
        }
    }




}
