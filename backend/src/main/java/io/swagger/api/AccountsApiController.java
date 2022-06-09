package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.AccountDTO;
import io.swagger.model.AccountDTO.AccountTypeEnum;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.entities.Account;
import io.swagger.model.dto.*;
import io.swagger.model.dto.*;
import io.swagger.model.AccountDTO.AccountTypeEnum;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entities.*;
import io.swagger.services.UserService;
import io.swagger.services.accountService;
import io.swagger.model.entities.Account;
import io.swagger.services.accountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.java.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.io.Console;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
@RestController
@Api(tags = {"Accounts"}, description = "the account API")
@Log
public class AccountsApiController implements AccountsApi {

    @Value("${server.bank.iban}")
    private String bank_Iban;

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private io.swagger.services.transactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    private accountService accountservice;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    public ResponseEntity<? extends Object> accountDeposit(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody DepositDTO body) {
            try {
                //get curent user from security context
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                //ceck if user is owner of the account or is admin
                List<Account> list = user.getAccounts();

                if (list.stream().filter(a -> a.getIBAN().equals(body.getToIBAN())).findAny().isPresent() || user.getRoles().contains("ROLE_ADMIN")) {
                    //ceck if fromIBAN and toIBAN exists
                    if (accountservice.findByIBAN(body.getToIBAN()) == null) {
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Account does not exist!", 404, "NOT_FOUND"), HttpStatus.NOT_FOUND);
                    } else {
                        //creste transaction object
                        Transaction transaction = new Transaction();
                        transaction.setPerformer(user);
                        transaction.setType(TransactionType.DEPOSIT);
                        transaction.setOrigin((Account) accountservice.findByIBAN(bank_Iban));
                        transaction.setTarget((Account) accountservice.findByIBAN(body.getToIBAN()));
                        transaction.setIBAN(bank_Iban);
                        transaction.setAmount(body.getAmount());
                        transaction.setPincode(body.getPincode());
                        return doTransaction(transaction);
                    }
                }
                //if user is not owner of the account or is not admin
                else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "You do not have acces!", 401, "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
                }
            }
            catch (IllegalArgumentException e){
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Ellegal argument in transaction", 400,  "NOT_ALLOWED"), HttpStatus.BAD_REQUEST);
            }
            catch (Exception e) {
                log.error("Internal server error", e);
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity<? extends Object> accountWithdraw(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody WithdrawDTO body) {

            try {
                //get curent user from security context
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                //ceck if user is owner of the account or is admin
                List<Account> list = user.getAccounts();

                if (list.stream().filter(a -> a.getIBAN().equals(body.getFromIBAN())).findAny().isPresent() || user.getRoles().contains("ROLE_ADMIN")) {
                    //ceck if fromIBAN and toIBAN exists
                    if (accountservice.findByIBAN(body.getFromIBAN()) == null ) {
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Account does not exist!", 404, "NOT_FOUND"), HttpStatus.NOT_FOUND);
                    } else {
                        //creste transaction object
                        Transaction transaction = new Transaction();
                        transaction.setPerformer(user);
                        transaction.setType(TransactionType.WITHDRAWAL);
                        transaction.setIBAN(body.getFromIBAN());
                        transaction.setOrigin((Account) accountservice.findByIBAN(body.getFromIBAN()));
                        transaction.setTarget((Account) accountservice.findByIBAN(bank_Iban));
                        transaction.setAmount(body.getAmount());
                        transaction.setPincode(body.getPincode());
                        return doTransaction(transaction);
                    }
                }
                //if user is not owner of the account or is not admin
                else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "You do not have acces!", 401, "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
                }
            }
            catch (IllegalArgumentException e){
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Ellegal argument in transaction", 400,  "NOT_ALLOWED"), HttpStatus.BAD_REQUEST);
            }
            catch (Exception e) {
                log.error("Internal server error", e);
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity<? extends Object> addAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostAccountDTO body) {
        String accept = request.getHeader("Content-Type");
        if (accept != null && accept.contains("application/json")) {
            try{
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(user.getRoles().contains(Role.ROLE_ADMIN)){
                    accountservice.addAccount(body);   
                    return new ResponseEntity<PostAccountDTO>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User role is invalid", 401, "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
                }
            } catch(Exception e){
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Couldn't serialize response for content type application/json", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is invalid", 403, "FORBIDDEN"), HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<? extends Object> getAccount(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {

        try {
            if(accountservice.validateIban(IBAN)){
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(user.getRoles().contains(Role.ROLE_ADMIN)){
                    AccountDTO dto = accountservice.getAccountDTOWithIBAN(IBAN);
                    if(dto == null){
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Account is not found", 404, "NOT_FOUND"), HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);
                } else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User role is invalid", 403, "FORBIDDEN"), HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "IBAN is invalid", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Couldn't serialize response for content type application/json", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is invalid", 403, "FORBIDDEN"), HttpStatus.FORBIDDEN);
    }

    //@GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Accept");


        if (accept != null && accept.contains("application/json")) {
            
            try {
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(user.getRoles().contains(Role.ROLE_ADMIN)) {
                    //Convert iterable to list
                    List<Account> accountlist = StreamSupport.stream(accountservice.getAllAccounts()
                    .spliterator(), false)
                    .collect(Collectors.toList());

                    //Map account list to accountdto
                    List<AccountDTO> dtos = new ArrayList<AccountDTO>();
                    for (Account account : accountlist) {
                        AccountDTO a = account.toAccountDTO();
                        dtos.add(a);
                    }

                    return new ResponseEntity<List<AccountDTO>>(dtos, HttpStatus.OK);
                } else {
                    return new ResponseEntity<List<AccountDTO>>(HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<AccountDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<List<AccountDTO>>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<? extends Object> searchAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fullName", required = true) String fullName,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Search");

        if (accept != null && accept.contains("application/json")) {
            try {
                User logged_user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(logged_user.getRoles().contains(Role.ROLE_ADMIN) || logged_user.getRoles().contains(Role.ROLE_USER)){
                    List<User> users = new ArrayList<User>();
                if(fullName.contains("-")){
                    //add both parts of the string to array
                    String[] split = fullName.toLowerCase().split("-");
                    for(int i = 0; i < split.length;i++){
                        if(!split[i].isEmpty()){
                            //search once on firstame inside user -> return list
                            List<User> user_fname = userService.findByFirstName(split[i]);
                            //search once on lastname inside user -> return list
                            List<User> user_lname = userService.findByLastName(split[i]);
                            //Add everything from both lists to
                            users.addAll(user_fname);
                            users.addAll(user_lname);
                        }
                    }
                } else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Please use '-' between the first-and lastname. ", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
                }

                List<NameSearchAccountDTO> dtos = new ArrayList<NameSearchAccountDTO>();
                for (User user : users) {
                    List<Account> user_accounts = accountservice.findByUserId(user.getId());
                    for (Account account : user_accounts){
                        NameSearchAccountDTO dto = user.toNameSearchAccountDTO(account.getIBAN());

                        //filter duplicates
                        if(!dtos.contains(dto)){
                            System.out.println(dto.toString());
                            dtos.add(dto);
                        }
                    }
                }
                return new ResponseEntity<List<NameSearchAccountDTO>>(dtos ,HttpStatus.OK);
                } else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User role is invalid", 401, "FORBIDDEN"), HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Couldn't serialize response for content type application/json", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is not valid!", 406, "NOT_ACCEPTABLE"), HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<? extends Object> updateAccountStatus(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody AccountDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                User logged_user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(logged_user.getRoles().contains(Role.ROLE_ADMIN)){
                    if(accountservice.validateIban(IBAN)){
                        accountservice.updateAccount(IBAN, body);
                        return new ResponseEntity<AccountDTO>(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "IBAN is invalid", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User role is invalid", 403, "FORBIDDEN"), HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Couldn't serialize response for content type application/json", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is not valid!", 406, "NOT_ACCEPTABLE"), HttpStatus.NOT_ACCEPTABLE);
    }

    private ResponseEntity<? extends Object> doTransaction(Transaction transaction){
        //validate transaction
        TransactionValidation validation = transactionService.isValidTransaction(transaction);
        if(validation.getStatus() == TransactionValidation.TransactionValidationStatus.VALID){
            //if transaction is valid
            transactionService.doTransaction(transaction);
            //check if transaction is executed
            if(transactionService.transactionExists(transaction.getId())){
                return new ResponseEntity<GetTransactionDTO>(transaction.toGetTransactionDTO(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Transaction failed!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else if(validation.getStatus() == TransactionValidation.TransactionValidationStatus.UNAUTHORIZED){
            //return errorDTO
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), validation.getMessage(), 401, validation.getStatus().toString()), HttpStatus.UNAUTHORIZED);
        }
        else if(validation.getStatus() == TransactionValidation.TransactionValidationStatus.NOT_ALLOWED || validation.getStatus() == TransactionValidation.TransactionValidationStatus.NOT_ACTIVE){
            //return errorDTO
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), validation.getMessage(), 403, validation.getStatus().toString()), HttpStatus.FORBIDDEN);
        }
        else{
            //return errorDTO
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), validation.getMessage(), 400, validation.getStatus().toString()), HttpStatus.BAD_REQUEST);
        }
    }

}
