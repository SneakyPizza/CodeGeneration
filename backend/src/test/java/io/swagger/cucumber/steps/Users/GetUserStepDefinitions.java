package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.GetUserDTO;
import io.swagger.model.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetUserStepDefinitions extends BaseStepDefinitions implements En {

    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN;

    private static final String INVALID_TOKEN = "invalid";

    private static final String USER_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;

    private ResponseEntity<UserDTO> response;
    private HttpEntity<String> request;
    private Integer status;

    private GetUserDTO getUserDTO;
    private String id;

    public GetUserStepDefinitions() {

        Given("^I have a valid user id", () -> {
            token = VALID_TOKEN;
        });

        Given("^I have an invalid user id", () -> {
            token = INVALID_TOKEN;
        });


        When("^I call the GetUser endpoint", () -> {
            httpHeaders.clear();
            // get id from /Users/{id}
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(id, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.GET, request, UserDTO.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a user status code of 200", () -> {
            Integer statusCode = 200;
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^I should see a user status code of 404", () -> {
            Integer statusCode = 404;
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });
    }
}
