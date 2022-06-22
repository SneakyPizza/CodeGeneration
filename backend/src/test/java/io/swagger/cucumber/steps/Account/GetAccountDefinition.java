package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.AccountDTO;
import io.swagger.model.dto.ErrorDTO;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetAccountDefinition extends BaseStepDefinitions implements En {

    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    @Value("${io.swagger.api.token_USER}")
    private String INVALID_TOKEN;

    private static final String VALID_HEADER = "Accept";
    private static final String INVALID_HEADER = "Invalid";

    private static final String VALID_IBAN = "NL01INHO0000000001";
    private static final String INVALID_IBAN = "DE01INHO0000000001";
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;
    private String iban;
    private ErrorDTO errorDTO;
    private AccountDTO dto;


    public GetAccountDefinition(){

        Given("^'get-account' I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^'get-account' My iban is valid", () -> {
            iban = VALID_IBAN;
        });

        And("^'get-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'get-account' I perform a get account operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts/" + iban, HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a get account status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^'get-account' My accept header is invalid", () -> {
            header = INVALID_HEADER;
        });

        And("^I should receive an get account error message with \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        And("^'get-account' I should receive the account", () -> {
            dto = mapper.readValue(response.getBody(), AccountDTO.class);
            assertNotNull(dto.getAbsoluteLimit());
            assertNotNull(dto.getAccountType());
            assertNotNull(dto.getActive());
            assertNotNull(dto.getBalance());
            assertNotNull(dto.getIBAN());
            assertNotNull(dto.getUserid());
        });

        And("^'get-account' My iban is invalid", () -> {
            iban = INVALID_IBAN;
        });

        And("^'get-account' I provide invalid admin credentials", () -> {
            token = INVALID_TOKEN;
        });
    }
}
