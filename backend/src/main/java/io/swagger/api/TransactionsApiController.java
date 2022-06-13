package io.swagger.api;

import io.swagger.annotations.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.*;
import io.swagger.services.TransactionService;
import io.swagger.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-28T11:30:29.125Z[GMT]")
@RestController
@Api(tags = {"Transactions"}, description = "the transaction API")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    io.swagger.services.accountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<? extends Object> getTransactionHistory(@Parameter(in = ParameterIn.PATH, description = "IBAN of a user.", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "dateONE", required = false) String dateONE,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "dateTWO", required = false) String dateTWO) {
        try {
            //if iban is null
            if (IBAN == null) {
                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "A IBAN value is required", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
            }
            //if iban is not null
            else {
                //get curent user from security context
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                //ceck if user is owner of the account or is admin
                List<Account> list = user.getAccounts();

                if (list.stream().filter(a -> a.getIBAN().equals(IBAN)).findAny().isPresent() || user.getRoles().contains(Role.ROLE_ADMIN)) {
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
                        else if(dateONE != null && dateTWO != null){
                            System.out.println("DATE ONE: " + dateONE);
                            System.out.println("DATE TWO: " + dateTWO);
                            LocalDateTime date1 = LocalDateTime.of(LocalDate.parse(dateONE), LocalTime.of(0, 0, 0));
                            LocalDateTime date2 = LocalDateTime.of(LocalDate.parse(dateTWO), LocalTime.of(0, 0, 0));
                            List<Transaction> transactions = transactionService.findByIBANAndTimestampBetween(IBAN, date1, date2);
                            if(transactions.isEmpty()){
                                return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "User does not have any transactions between dates:" + dateONE + " " + dateTWO, 204, "NO_CONTENT"), HttpStatus.NO_CONTENT);
                            }
                            else{
                                List<GetTransactionDTO> transactionDTOs = transactions.stream().map(t -> t.toGetTransactionDTO()).collect(java.util.stream.Collectors.toList());
                                return new ResponseEntity<List<GetTransactionDTO>>(transactionDTOs, HttpStatus.OK);
                            }

                        }
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
        }
        catch(DateTimeParseException e){
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "Invalid date format!", 400, "BAD_REQUEST"), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error("Internal server error", e);
            return new ResponseEntity<ErrorDTO>(new ErrorDTO(LocalDateTime.now().toString(), "An internal server error had occured!", 500, "INTERNAL_SERVER_ERROR"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<? extends Object> transaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostTransactionDTO body) {
        Transaction transaction = transactionService.doTransaction(body, TransactionType.TRANSFER);
        return new ResponseEntity<GetTransactionDTO>(transaction.toGetTransactionDTO(), HttpStatus.OK);
    }
}