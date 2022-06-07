package io.swagger.cucumber.steps.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetTransactionDTO;
import io.swagger.model.dto.PostTransactionDTO;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class TransactionStepDefinitions extends BaseStepDefinitions implements En {

    // Token is valid for one a rly short time apparentlyprivate static final String VALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMDUwLCJleHAiOjE2NTQ1MjY2NTB9.N0-U8GlkNxeHG8pR9IiqJVbVopgAMEvKBRbMwmzGCQk";private static final String VALID_TOKEN_ADMIN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYW5rIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU0NTIzMTAzLCJleHAiOjE2NTQ1MjY3MDN9.H3t7QDx2s-L_bOSSojujupT-i8stdq0cZcjBxcOI0vY";
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;
   private static final String INVALID_TOKEN = "invalid";
    private static final String VALID_TRANSACTION_USER = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000002\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000001\"\n}";
    private static final String VALID_TRANSACTION_ADMIN = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000001\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000002\"\n}";
    private static final String INVALID_PIN_TRANSACTION_ADMIN = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000002\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1534\",\n  \"toIBAN\": \"NL01INHO0000000001\"\n}";
    private static final String INVALID_IBAN_TRANSACTION_ADMIN = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000003\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000002\"\n}";
    private static final String INVALID_TARGET_TRANSACTION_ADMIN = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000001\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000001\"\n}";
    private static final String INVALID_AMOUNT_TRANSACTION_USER = "{\n  \"amount\": 5000,\n  \"fromIBAN\": \"NL01INHO0000000002\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000001\"\n}";
    private static final String NO_ACCESS_TRANSACTION_USER = "{\n  \"amount\": 500,\n  \"fromIBAN\": \"NL01INHO0000000001\",\n  \"fromUserId\": \"d36dc67d-ed99-404f-9f0b-f66497e67983\",\n  \"pincode\": \"1234\",\n  \"toIBAN\": \"NL01INHO0000000002\"\n}";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> response;
    private HttpEntity<String> request;

    private Integer status;
    private PostTransactionDTO dto;
    private GetTransactionDTO resultTransaction;
    private ErrorDTO errorDTO;
    private String token;
    private String transaction;

    public TransactionStepDefinitions() {

        Given("^I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        Given("^I provide valid user credentials", () -> {
            token = VALID_TOKEN_USER;
        });

        Given("^I provide invalid credentials", () -> {
            token = INVALID_TOKEN;
        });

        And("^My admin transaction object is valid", () -> {
            transaction = VALID_TRANSACTION_ADMIN;
        });

        And("^My user transaction object is valid", () -> {
            transaction = VALID_TRANSACTION_USER;
        });

        When("^I perform a transaction", () -> {
            httpHeaders.clear();
            //post to /Transactions
            httpHeaders.add("Authorization", "Bearer " + token);
            //trasnsaction string to postTransactionDTO
            dto = mapper.readValue(transaction, PostTransactionDTO.class);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/Transactions", request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a transaction status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");


        });

        And("^My Pin is incorrect", () -> {
            transaction = INVALID_PIN_TRANSACTION_ADMIN;
        });

        And("^Transaction origin is the same as target", () -> {
            transaction = INVALID_TARGET_TRANSACTION_ADMIN;
        });

        And("^IBAN does not exist", () -> {
            transaction = INVALID_IBAN_TRANSACTION_ADMIN;
        });

        And("^I have exceeded the transaction limit", () -> {
            transaction = INVALID_AMOUNT_TRANSACTION_USER;
        });

        And("^I am not the owner of the account", () -> {
            transaction = NO_ACCESS_TRANSACTION_USER;
        });

    }
}