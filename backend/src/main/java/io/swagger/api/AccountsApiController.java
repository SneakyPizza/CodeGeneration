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
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        PostAccountDTO dto = accountservice.addAccount(body, user);
        return new ResponseEntity<PostAccountDTO>(dto, HttpStatus.CREATED);
    }

    public ResponseEntity<? extends Object> getAccount(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        AccountDTO dto = accountservice.getAccountDTOWithIBAN(IBAN, user);
        return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<AccountDTO> dto = accountservice.getAllAccounts(user);
        return new ResponseEntity<List<AccountDTO>>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> searchAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fullName", required = true) String fullName,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {       
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<NameSearchAccountDTO> dtos = accountservice.searchAccountDTOs(fullName, limit, offset, user);
        return new ResponseEntity<List<NameSearchAccountDTO>>(dtos, HttpStatus.OK);
    }

    public ResponseEntity<? extends Object> updateAccountStatus(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody AccountDTO body) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        AccountDTO dto = accountservice.updateAccount(IBAN, body, user);
        return new ResponseEntity<AccountDTO>(dto, HttpStatus.CREATED);
    }
}
