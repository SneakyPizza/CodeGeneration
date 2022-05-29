package io.swagger.api;

import io.swagger.model.AccountDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import io.swagger.model.entities.Account;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.User;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-28T11:30:29.125Z[GMT]")
@RestController
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

    public ResponseEntity<List<GetTransactionDTO>> getTransactionHistory(@Parameter(in = ParameterIn.PATH, description = "IBAN of a user.", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
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
                        //ceck if account exists
                        if (accountService.findByIBAN(IBAN) == null) {
                            return new ResponseEntity<List<GetTransactionDTO>>(HttpStatus.NOT_FOUND);
                        }
                        //if Account is found
                        else {
                            //if user has no transactions
                            if (transactionService.getTransactions(IBAN).isEmpty()) {
                                return new ResponseEntity<List<GetTransactionDTO>>(HttpStatus.NO_CONTENT);
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
                        return new ResponseEntity<List<GetTransactionDTO>>( HttpStatus.UNAUTHORIZED);
                    }
                }
            } catch (Exception e) {
                log.error("Internal server error", e);
                return new ResponseEntity<List<GetTransactionDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            log.error("Accept header is not valid");
            return new ResponseEntity<List<GetTransactionDTO>>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<AccountDTO> transaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostTransactionDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AccountDTO>(objectMapper.readValue("{\n  \"accountType\" : \"savings\",\n  \"userid\" : \"5e9f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\",\n  \"balance\" : \"0\",\n  \"active\" : \"active\",\n  \"absoluteLimit\" : \"1000\"\n}", AccountDTO.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AccountDTO>( HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AccountDTO>(HttpStatus.NOT_IMPLEMENTED);
    }
}