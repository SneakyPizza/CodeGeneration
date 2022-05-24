package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.AccountDTO;
import io.swagger.model.dto.TransactionDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entities.Transaction;
import io.swagger.model.entities.User;
import io.swagger.services.UserService;
import io.swagger.services.transactionService;
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
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
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

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<TransactionDTO> getTransactionHistory(@Parameter(in = ParameterIn.PATH, description = "IBAN of a user.", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                //get transactionDTO from request
                TransactionDTO transactionDTO = objectMapper.readValue("{  \"fromIBAN\" : \"fromIBAN\",  \"toIBAN\" : \"toIBAN\",  \"pincode\" : \"pincode\",  \"amount\" : 1,  \"timestamp\" : \"timestamp\",  \"fromUserId\" : \"fromUserId\"}", TransactionDTO.class);

                //get user from securety context
                String userName = (String) SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userService.getUserByUserName(userName);
                //get  transaction history for the user
//                Transaction transaction = (Transaction) transactionService.getTransactionsByFromIban(IBAN);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<TransactionDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TransactionDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<AccountDTO> transaction(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody TransactionDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AccountDTO>(objectMapper.readValue("{\n  \"accountType\" : \"savings\",\n  \"userid\" : \"5e9f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\",\n  \"balance\" : \"0\",\n  \"active\" : \"active\",\n  \"absoluteLimit\" : \"1000\"\n}", AccountDTO.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AccountDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<AccountDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

}
