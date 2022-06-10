package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.dto.ErrorDTO;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class GetAllAccountsDefinitions extends BaseStepDefinitions implements En{
    
    
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    @Value("${io.swagger.api.token_USER}")
    private String INVALID_TOKEN;

    private static final String VALID_HEADER = "Accept";
    private static final String INVALID_HEADER = "Invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;

    public GetAllAccountsDefinitions(){
        Given("^'getall-account' I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^'getall-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'getall-account' I perform a get all accounts operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(header, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts" + "?offset=1&limit=20", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a get all accounts status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^'getall-account' I provide invalid admin credentials", () -> {
            token = INVALID_TOKEN;
        });
    }
}
