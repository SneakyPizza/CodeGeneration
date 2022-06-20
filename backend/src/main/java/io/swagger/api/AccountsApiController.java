package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.AccountDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.entities.Account;
import io.swagger.model.dto.*;

import io.swagger.model.entities.*;
import io.swagger.services.TransactionService;
import io.swagger.services.UserService;
import io.swagger.services.AccountService;
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
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = {"Accounts"}, description = "the account API")
@Log
public class AccountsApiController implements AccountsApi {

    @Value("${server.bank.iban}")
    private String bankIban;

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    //niet nodig

    @Autowired
    private TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    private AccountService accountservice;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }
    public ResponseEntity<? extends Object> accountDeposit(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody DepositDTO body) {
        Transaction transaction = transactionService.doTransaction(body.toPostTransactionDTO(bankIban), TransactionType.DEPOSIT);
        return new ResponseEntity<>(transaction.toGetTransactionDTO(), HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> accountWithdraw(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody WithdrawDTO body) {
        Transaction transaction = transactionService.doTransaction(body.toPostTransactionDTO(bankIban), TransactionType.WITHDRAWAL);
        return new ResponseEntity<GetTransactionDTO>(transaction.toGetTransactionDTO(), HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> addAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostAccountDTO body) {
        /*
        String accept = request.getHeader("Content-Type");
        if (accept != null && accept.contains("application/json")) {
            try{
                Users user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
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
        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is invalid", 415, "UNSUPPORTED_MEDIA_TYPE"), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        */

        Users users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        PostAccountDTO dto = accountservice.addAccount(body, users);
        return new ResponseEntity<PostAccountDTO>(dto, HttpStatus.CREATED);
    }

    public ResponseEntity<? extends Object> getAccount(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        /*
        try {
            if(accountservice.validateIban(IBAN)){
                Users user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if(user.getRoles().contains(Role.ROLE_ADMIN) || user.getAccounts().stream().anyMatch(a -> a.getIBAN().equals(IBAN))){
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
        }*/
        //return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Accept header is invalid", 403, "FORBIDDEN"), HttpStatus.FORBIDDEN);

        Users users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        AccountDTO dto = accountservice.getAccountDTOWithIBAN(IBAN, users);
        return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        //String accept = request.getHeader("Accept");
        /*
        if (accept != null && accept.contains("application/json")) {
            
            try {
                Users user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
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
        */
        Users users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        //AccountDTO dto = accountservice.getAccountDTOWithIBAN(IBAN, user);
        List<AccountDTO> dto = accountservice.getAllAccounts(users);
        return new ResponseEntity<List<AccountDTO>>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> searchAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fullName", required = true) String fullName,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
//        String accept = request.getHeader("Search");
//
//            try {
//                Users logged_users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//                if(logged_users.getRoles().contains(Role.ROLE_ADMIN) || logged_users.getRoles().contains(Role.ROLE_USER)){
//                    List<Users> users = new ArrayList<Users>();
//                if(fullName.contains("-")){
//                    //add both parts of the string to array
//                    String[] split = fullName.toLowerCase().split("-");
//                    for(int i = 0; i < split.length;i++){
//                        if(!split[i].isEmpty()){
//                            //search once on firstame inside user -> return list
//                            List<Users> user_fname = userService.findByFirstName(split[i]);
//                            //search once on lastname inside user -> return list
//                            List<Users> user_lname = userService.findByLastName(split[i]);
//                            //Add everything from both lists to
//                            users.addAll(user_fname);
//                            users.addAll(user_lname);
//                        }
//                    }
//                } else {
//                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Please use '-' between the first-and lastname. ", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
//                }
//
//                List<NameSearchAccountDTO> dtos = new ArrayList<NameSearchAccountDTO>();
//                for (Users user : users) {
//                    List<Account> user_accounts = accountservice.findByUserId(user.getId());
//                    for (Account account : user_accounts){
//                        NameSearchAccountDTO dto = user.toNameSearchAccountDTO(account.getIBAN());
//
//                        //filter duplicates
//                        if(!dtos.contains(dto)){
//                            System.out.println(dto.toString());
//                            dtos.add(dto);
//                        }
//                    }
//                }
//                return new ResponseEntity<List<NameSearchAccountDTO>>(dtos ,HttpStatus.OK);
//                } else {
//                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User role is invalid", 401, "FORBIDDEN"), HttpStatus.FORBIDDEN);
//                }
//            } catch (Exception e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Couldn't serialize response for content type application/json", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
//            }

        Users users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<NameSearchAccountDTO> dtos = accountservice.searchAccountDTOs(fullName, limit, offset, users);
        return new ResponseEntity<List<NameSearchAccountDTO>>(dtos, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> updateAccountStatus(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody AccountDTO body) {
        /*
            try {
                Users logged_user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
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
        */

        Users users = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        AccountDTO dto = accountservice.updateAccount(IBAN, body, users);
        return new ResponseEntity<AccountDTO>(dto, HttpStatus.CREATED);
    }
}
