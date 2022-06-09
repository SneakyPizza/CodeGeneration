package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class SearchAccountDefinitions extends BaseStepDefinitions implements En {
    
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;
    private static final String INVALID_TOKEN_USER = "Search";

    private static final String VALID_HEADER = "Search";
    private static final String INVALID_HEADER = "Invalid";

    private static final String VALID_FULLNAME = "test-test";
    private static final String INVALID_FULLNAME = "Invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;
    private String fullname;

    public SearchAccountDefinitions(){
        Given("^'search-account' I provide valid user credentials", () -> {
            token = VALID_TOKEN_USER;
        });

        And("^'search-account' My fullname is valid", () -> {
            fullname = VALID_FULLNAME;
        });

        And("^'search-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'search-account' I perform a search account operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts/Search?fullName=" + fullname + "&offset=1&limit=20", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a search account status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        Given("^'search-account' I provide invalid user credentials", () -> {
            token = INVALID_TOKEN_USER;
        });

        And("^'search-account' My fullname is invalid", () -> {
            fullname = INVALID_FULLNAME;
        });

        And("^'search-account' My accept header is invalid", () -> {
            header = INVALID_HEADER;
        });
    }
}
