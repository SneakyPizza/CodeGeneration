package io.swagger.cucumber.steps.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class WithdrawDefinitions extends BaseStepDefinitions implements En {

    // Token is valid for one a rly short time apparentlyprivate static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMDUwLCJleHAiOjE2NTQ1MjY2NTB9.N0-U8GlkNxeHG8pR9IiqJVbVopgAMEvKBRbMwmzGCQk";private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYW5rIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMTAzLCJleHAiOjE2NTQ1MjY3MDN9.H3t7QDx2s-L_bOSSojujupT-i8stdq0cZcjBxcOI0vY";
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;
    private static final String INVALID_TOKEN = "invalid";
    private static final String VALID_TRANSACTION_USER = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000003\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\"\n}";
    private static final String INVALID_PIN_TRANSACTION_USER = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000003\",\n  \"fromUserId\": \"d36dc11d-ed99-404f-9f0b-f6649ee61983\",\n  \"pincode\": \"5981\"\n}";
    private static final String INVALID_IBAN_TRANSACTION_USER = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000004\",\n  \"fromUserId\": \"d36dc11d-ed99-404f-9f0b-f6649ee61983\",\n  \"pincode\": \"1234\"\n}";
    private static final String LIMIT_EXCEEDED_TRANSACTION_USER = "{\n  \"amount\": 5000,\n  \"fromIBAN\": \"NL01INHO0000000003\",\n  \"fromUserId\": \"d36dc11d-ed99-404f-9f0b-f6649ee61983\",\n  \"pincode\": \"1234\"\n}";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private WithdrawDTO dto;
    private GetTransactionDTO resultTransaction;
    private ErrorDTO errorDTO;
    private String token;
    private String transaction;

    public WithdrawDefinitions() {

        Given("^'withdraw' I provide valid user credentials", () -> {
            token = VALID_TOKEN_USER;
        });

        Given("^'withdraw' I provide invalid credentials", () -> {
            token = INVALID_TOKEN;
        });

        And("^'withdraw' My user withdraw object is valid", () -> {
            transaction = VALID_TRANSACTION_USER;
        });

        When("^I perform a withdraw", () -> {
            httpHeaders.clear();
            //post to /Transactions
            httpHeaders.add("Authorization", "Bearer " + token);
            //trasnsaction string to postTransactionDTO
            dto = mapper.readValue(transaction, WithdrawDTO.class);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/Accounts/Withdraw", request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a withdraw status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^'withdraw' My Pin is incorrect!", () -> {
            transaction = INVALID_PIN_TRANSACTION_USER;
        });

        And("^'withdraw' IBAN does not exist", () -> {
            transaction = INVALID_IBAN_TRANSACTION_USER;
        });

        And("^'withdraw' I have exceeded the withdraw limit", () -> {
            transaction = LIMIT_EXCEEDED_TRANSACTION_USER;
        });

        And("^I should have a withdraw object with type \"([^\"]*)\"$", (String arg0) -> {
            resultTransaction = mapper.readValue(response.getBody(), GetTransactionDTO.class);
            Assertions.assertEquals(arg0, resultTransaction.getType());
            Assertions.assertNotNull(resultTransaction.getFromUserId());
            Assertions.assertEquals(dto.getFromIBAN(), resultTransaction.getFromIBAN());
            Assertions.assertEquals(dto.getAmount(), resultTransaction.getAmount());
            Assertions.assertNotNull(resultTransaction.getToIBAN());
        });

        And("^I should have a error object with message \"([^\"]*)\"$", (String arg0) -> {
            errorDTO = mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

    }
}