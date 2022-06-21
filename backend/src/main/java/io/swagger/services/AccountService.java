package io.swagger.services;

import io.swagger.exeption.custom.InvalidIbanException;
import io.swagger.exception.custom.NotFoundException;
import io.swagger.exception.custom.UnauthorizedException;
import io.swagger.model.AccountDTO;
//import io.swagger.model.GetUserDTO.Role;
import io.swagger.model.entities.Role;
import io.swagger.model.dto.NameSearchAccountDTO;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.Users;
import io.swagger.repositories.AccountRepository;
import io.swagger.utils.ibanGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    private ibanGenerator ibanGen = new ibanGenerator();

    //Add a new account object to the database (POST)
    public PostAccountDTO addAccount(PostAccountDTO account, Users users) {
        validateUserRoleAdmin(users);
        validatePostAccountDTO(account);

        Account<Users> a = new Account<>();
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setBalance(new BigDecimal(0));
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setUsers(userService.getUser(account.getUserid().toString()));
        a.setIBAN(ibanGen.GenerateIban());
        accountRepository.save(a);
        return account;
    }
    
    //Get all accounts (GET)
    public List<AccountDTO> getAllAccounts(Users users){
        validateUserRoleAdmin(users);
        List<Account> accounts = (List<Account>) accountRepository.findAll();
        List<AccountDTO> dto = new ArrayList();
        for(Account account : accounts){
            dto.add(account.toAccountDTO());
        }
        return dto;
    }

    
    //Get a Account with IBAN (GET)
    public AccountDTO getAccountDTOWithIBAN(String iban, Users users){
        validateUserRoleAdmin(users, iban);
        validateIban(iban);
        Account<Users> account = getAccountWithIBAN(iban);
        if(account == null){
            throw new IllegalArgumentException("account is not found");
        }
        return account.toAccountDTO();
    }

    public List<NameSearchAccountDTO> searchAccountDTOs(String fullname, int limit, int offset, Users users){
    validateUserRoleUser(users);
    validateNameSearchRequest(fullname, limit, offset);

    return findAccountUsers(splitFullNameToUsers(fullname));
    }

    public void validateNameSearchRequest(String fullname, int limit, int offset){
        if(!checkSearchAccountFullname(fullname)){
            throw new IllegalArgumentException("Invalid fullname, please use '-' between the first and last name once.");
        } else if(!checkLimit(limit)){
            throw new IllegalArgumentException("Invalid limit, limit needs to be between 1 and 20");
        } else if(!checkOffset(offset)){
            throw new IllegalArgumentException("Invalid offset value, needs to be between 1 and 50");
        }
    }

    //update an account object to the database
    public AccountDTO updateAccount(String iban, AccountDTO account, Users users) {
        validateIban(iban);
        validateUserRoleAdmin(users);
        validateAccountDTO(account);
        Account<Users> a = getAccountWithIBAN(iban);
        a.setAccountType(Account.AccountTypeEnum.fromValue(account.getAccountType().toString()));
        a.setActive(Account.ActiveEnum.fromValue(account.getActive().toString()));
        a.setAbsoluteLimit(account.getAbsoluteLimit());
        a.setBalance(account.getBalance());
        a.setIBAN(account.getIBAN());
        a.setUsers(userService.getUser(account.getUserid().toString()));
        accountRepository.save(a);
        return account;
    }

    private List<Account> findByUserId(UUID userid){
        return accountRepository.findByUsersId(userid);
    }

    public Object findByIBAN(String iban) {return accountRepository.findByIBAN(iban);}

    //Boolean checks / Validations
    
    private boolean checkIfUserRoleAdmin(Users users){
        //return user.getRoles().contains(Role.ROLE_ADMIN);
        return users.getRoles().get(0).equals(Role.ROLE_ADMIN);
    }

    private boolean checkIfUserRoleUser(Users users){
        //return user.getRoles().contains(Role.ROLE_USER);
        return users.getRoles().get(0).equals(Role.ROLE_USER);
    }

    private boolean checkIban(String iban){
        return ibanGen.ValidateIban(iban);
    }

    private boolean checkOffset(int offset){
        return (offset > 0 && offset < 51);
    }

    private boolean checkLimit(int limit){
        return (limit > 0 && limit < 21);
    }

    private boolean getAccountdtoNullCheck(AccountDTO accountdto){
        if(accountdto == null) throw new NullPointerException("accountdto is null");
        return accountdto != null;
    }

    private boolean getAccountNullCheck(Account account){
        if(account == null) throw new NullPointerException("account is null");
        return account != null;
    }

    private boolean checkSearchAccountFullname(String fullname){
        long count = fullname.chars().filter(ch -> ch == '-').count();
        return count == 1;
    }

    private boolean checkPostAccountType(PostAccountDTO postaccountdto){
        
        int enumcheck = 0;
        for (PostAccountDTO.AccountTypeEnum accountType : PostAccountDTO.AccountTypeEnum.values()){
            if(accountType.name().equalsIgnoreCase(postaccountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 1;

    }

    private boolean checkPostAccountActive(PostAccountDTO postaccountdto){
        int enumcheck = 0;
        for (PostAccountDTO.ActiveEnum accountActive : PostAccountDTO.ActiveEnum.values()){
            if(accountActive.name().equalsIgnoreCase(postaccountdto.getActive().name())){
                enumcheck++;
            }
        }
        return enumcheck == 1;
    }

    private boolean checkAccountType(AccountDTO accountdto) {
        int enumcheck = 0;
        for (AccountDTO.AccountTypeEnum accountType : AccountDTO.AccountTypeEnum.values()){
            if(accountType.name().equalsIgnoreCase(accountdto.getAccountType().name())){
                enumcheck++;
            }
        }
        return enumcheck == 1;
    }

    private boolean checkAccountActive(AccountDTO accountdto) {
        int enumcheck = 0;
        for (AccountDTO.ActiveEnum accountActive : AccountDTO.ActiveEnum.values()){
            if(accountActive.name().equalsIgnoreCase(accountdto.getActive().name())){
                enumcheck++;
            }
        }
        return enumcheck == 1;
    }

    private void validateUserRoleAdmin(Users users){
        if(!checkIfUserRoleAdmin(users)){
            throw new UnauthorizedException("You need to be a admin to preform this action");
        }
    }

    private void validateUserRoleUser(Users users){
        if(!checkIfUserRoleUser(users) && !checkIfUserRoleAdmin(users)){
            throw new UnauthorizedException("You need to be a User to preform this action");
        }
    }

    private void validateUserRoleAdmin(Users users, String iban){
        if(!checkIfUserRoleAdmin(users) && users.getAccounts().stream().noneMatch(a -> a.getIBAN().equals(iban))){
            throw new UnauthorizedException("You need to be a admin to preform this action or own the account");
        }
    }

    private void validateIban(String iban){
        if(!checkIban(iban)){
            throw new InvalidIbanException("Invalid iban");
        }
    }

    private Account<Users> getAccountWithIBAN (String iban){
        return (Account<Users>) accountRepository.findByIBAN(iban);
    }

    public void validatePostAccountDTO(PostAccountDTO postaccountdto){
        if(postaccountdto.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unable to set limit below ZERO");
        } else if(!userService.checkIfUserExists(postaccountdto.getUserid())){
            throw new NotFoundException("User was not found");
        } else if(!checkPostAccountType(postaccountdto)){
            throw new IllegalArgumentException("Invalid account type");
        } else if(!checkPostAccountActive(postaccountdto)){
            throw new IllegalArgumentException("Invalid active type");
        }
    }

    public void validateAccountDTO(AccountDTO accountdto){
        if(accountdto.getAbsoluteLimit().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Unable to set limit below ZERO");
        } else if(!userService.checkIfUserExists(accountdto.getUserid())){
            throw new NotFoundException("User was not found");
        } else if(!checkAccountType(accountdto)){
            throw new IllegalArgumentException("Invalid account type");
        } else if(!checkAccountActive(accountdto)){
            throw new IllegalArgumentException("Invalid active type");
        }
    }

    private List<Users> splitFullNameToUsers(String fullname){
        List<Users> users = new ArrayList<Users>();
        String[] split = fullname.toLowerCase().split("-");
        for(int i = 0; i < split.length;i++){
            if(!split[i].isEmpty()){
                List<Users> users_fname = userService.findByFirstName(split[i]);
                List<Users> users_lname = userService.findByLastName(split[i]);
                users.addAll(users_fname);
                users.addAll(users_lname);
            }
        }
        return users;
    }
    
    private List<NameSearchAccountDTO> findAccountUsers(List<Users> users){
        List<NameSearchAccountDTO> dtos = new ArrayList<NameSearchAccountDTO>();
        for (Users user : users) {
            List<Account> user_accounts = findByUserId(user.getId());
            for (Account account : user_accounts){
                NameSearchAccountDTO dto = user.toNameSearchAccountDTO(account.getIBAN());
                if(!dtos.contains(dto)){
                    //System.out.println(dto.toString());
                    dtos.add(dto);
                }
            }
        }
        return dtos;
    }




}
