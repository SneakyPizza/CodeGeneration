package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.dto.AccountDTO;
import io.swagger.model.dto.NameSearchAccountDTO;
import io.swagger.model.dto.PostAccountDTO;
import io.swagger.model.dto.TransactionDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Api(tags = {"Accounts"}, description = "the account API")
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<AccountDTO> accountDeposit(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody TransactionDTO body) {
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

    public ResponseEntity<AccountDTO> accountWithdraw(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody TransactionDTO body) {
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

    public ResponseEntity<Void> addAccount(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody PostAccountDTO body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<AccountDTO> getAccount(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN) {
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

    public ResponseEntity<List<AccountDTO>> getAllAccounts(@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<AccountDTO>>(objectMapper.readValue("[ {\n  \"accountType\" : \"savings\",\n  \"userid\" : \"5e9f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\",\n  \"balance\" : \"0\",\n  \"active\" : \"active\",\n  \"absoluteLimit\" : \"1000\"\n}, {\n  \"accountType\" : \"savings\",\n  \"userid\" : \"5e9f8f8f-8f8f-8f8f-8f8f-8f8f8f8f8f8f\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\",\n  \"balance\" : \"0\",\n  \"active\" : \"active\",\n  \"absoluteLimit\" : \"1000\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<AccountDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<AccountDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<NameSearchAccountDTO>> searchAccount(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "fullName", required = true) String fullName,@Min(0)@Parameter(in = ParameterIn.QUERY, description = "The number of items to skip before starting to collect the result set." ,schema=@Schema(allowableValues={  }
)) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Min(1) @Max(50) @Parameter(in = ParameterIn.QUERY, description = "The numbers of items to return." ,schema=@Schema(allowableValues={  }, minimum="1", maximum="50"
, defaultValue="20")) @Valid @RequestParam(value = "limit", required = false, defaultValue="20") Integer limit) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<NameSearchAccountDTO>>(objectMapper.readValue("[ {\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Doe\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\"\n}, {\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Doe\",\n  \"IBAN\" : \"NL 0750 8900 0000 0175 7814\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<NameSearchAccountDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<NameSearchAccountDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<AccountDTO> updateAccountStatus(@Parameter(in = ParameterIn.PATH, description = "Gets the account of the IBAN", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody AccountDTO body) {
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
