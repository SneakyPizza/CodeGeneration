package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.AccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.*;
import io.swagger.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-28T11:30:29.125Z[GMT]")
@RestController
@Api(tags = {"Transactions"}, description = "the transaction API")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private io.swagger.services.transactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    io.swagger.services.accountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<? extends Object> getTransactionHistory(@Parameter(in = ParameterIn.PATH, description = "IBAN of a user.", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        try {
            //if iban is null
            if (IBAN == null) {
                return new ResponseEntity<List<GetTransactionDTO>>(HttpStatus.BAD_REQUEST);
            }
            //if iban is not null
            else {
                //get curent user from security context
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                //ceck if user is owner of the account or is admin
                List<Account> list = user.getAccounts();

                if (list.stream().filter(a -> a.getIBAN().equals(IBAN)).findAny().isPresent() || user.getRoles().contains("ROLE_ADMIN")) {
                    //check if account exists
                    if (accountService.findByIBAN(IBAN) == null) {
                        return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "This account does not exist!", 404, "NOT_FOUND"), HttpStatus.NOT_FOUND);
                    }
                    //if Account is found
                    else {
                        //if user has no transactions
                        if (transactionService.getTransactions(IBAN).isEmpty()) {
                            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User does not have any transactions.", 204, "NO_CONTENT"), HttpStatus.NO_CONTENT);
                        }
                        //if user has transactions
                        else {
                            List<Transaction> transactions = transactionService.getTransactions(IBAN);
                            List<GetTransactionDTO> transactionDTOs = transactions.stream().map(t -> t.toGetTransactionDTO()).collect(java.util.stream.Collectors.toList());
                            return new ResponseEntity<List<GetTransactionDTO>>( transactionDTOs, HttpStatus.OK);
                        }
                    }
                }
                //if user is not owner of the account or is not admin
                else {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "You do not have acces!", 401, "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
                }
            }
        } catch (Exception e) {
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<? extends Object> transaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostTransactionDTO body) {
        try {
            //get curent user from security context
                String name = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(name);
                //ceck if user is owner of the account or is admin
                List<Account> list = user.getAccounts();

            if (list.stream().filter(a -> a.getIBAN().equals(body.getFromIBAN())).findAny().isPresent() || user.getRoles().contains("ROLE_ADMIN")) {
                //ceck if fromIBAN and toIBAN exists
                if (accountService.findByIBAN(body.getFromIBAN()) == null || accountService.findByIBAN(body.getToIBAN()) == null) {
                    return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Account does not exist!", 404, "NOT_FOUND"), HttpStatus.NOT_FOUND);
                }
                else {
                    //creste transaction object
                    Transaction transaction = new Transaction();
                    transaction.setPerformer(user);
                    transaction.setIBAN(body.getFromIBAN());
                    transaction.setType(TransactionType.TRANSFER);
                    transaction.setOrigin((Account) accountService.findByIBAN(body.getFromIBAN()));
                    transaction.setTarget((Account) accountService.findByIBAN(body.getToIBAN()));
                    transaction.setAmount(body.getAmount());
                    transaction.setPincode(body.getPincode());
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
            //if user is not owner of the account or is not admin
            else {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "You do not have acces!", 401, "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception e) {
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}