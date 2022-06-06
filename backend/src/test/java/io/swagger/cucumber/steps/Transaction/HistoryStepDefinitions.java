package io.swagger.cucumber.steps.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class HistoryStepDefinitions  extends BaseStepDefinitions implements En {

    // Token is valid for one a rly short time apparently
    private static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMDUwLCJleHAiOjE2NTQ1MjY2NTB9.N0-U8GlkNxeHG8pR9IiqJVbVopgAMEvKBRbMwmzGCQk";
    private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYW5rIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMTAzLCJleHAiOjE2NTQ1MjY3MDN9.H3t7QDx2s-L_bOSSojujupT-i8stdq0cZcjBxcOI0vY";
    private static final String INVALID_TOKEN = "invalid";
    private static final String USER_IBAN = "NL01INHO0000000002";
    private static final String ADMIN_IBAN = "NL01INHO0000000001";
    private static final String IBAN_INVALID = "invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private List<GetTransactionDTO> dto;
    private String token;
    private String IBAN;

    public HistoryStepDefinitions() {

        When("^I call the transaction history endpoint$", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
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
    }
}