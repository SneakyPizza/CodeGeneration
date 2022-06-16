package io.swagger.cucumber.steps.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class HistoryStepDefinitions  extends BaseStepDefinitions implements En {

    // Token is valid for one a rly short time apparently
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;
    private static final String INVALID_TOKEN = "invalid";
    private static final String USER_IBAN = "NL01INHO0000000003";
    private static final String ADMIN_IBAN = "NL01INHO0000000001";
    private static final String IBAN_INVALID = "invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private List<GetTransactionDTO> dto;
    private ErrorDTO error;
    private String token;
    private String IBAN;

    public HistoryStepDefinitions() {

        When("^I call the transaction history endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Transactions/" + IBAN, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
            status = response.getStatusCode().value();
        });

        Then("^History the result is a status of (\\d+)$", (Integer code) -> {
            Assertions.assertEquals(code, status);
        });

        Given("^I have an invalid token$", () -> {
            token = INVALID_TOKEN;
        });

        Given("^I have an valid user token$", () -> {
            token = VALID_TOKEN_USER;
        });

        Given("^I have an valid admin token$", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^I provide the correct bank IBAN$", () -> {
            IBAN = ADMIN_IBAN;
        });

        And("^I provide the correct user IBAN$", () -> {
            IBAN = USER_IBAN;
        });

        And("^I provide the incorrect IBAN$", () -> {
            IBAN = IBAN_INVALID;
        });

        Then("^The result is a list of transactions of size 1$", () -> {
            dto = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, GetTransactionDTO.class));
            Assertions.assertEquals(1, dto.size());
        });
        And("^Body is Null$", () -> {
            Assertions.assertNull(response.getBody());
        });

        And("^'History 'I have a error object with message \"([^\"]*)\"$", (String arg0) -> {
            error = mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, error.getMessage());
            Assertions.assertNotNull(error.getTimestamp());
            Assertions.assertNotNull(error.getStatus());
            Assertions.assertNotNull(error.getError());
        });


        And("^It contains a transaction object$", () -> {
            dto = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, GetTransactionDTO.class));
            Assertions.assertNotNull(dto.get(0).getType());
            Assertions.assertNotNull(dto.get(0).getAmount());
            Assertions.assertNotNull(dto.get(0).getTimestamp());
            Assertions.assertNotNull(dto.get(0).getFromUserId());
            Assertions.assertNotNull(dto.get(0).getToIBAN());
            Assertions.assertNotNull(dto.get(0).getFromIBAN());
        });
    }
}