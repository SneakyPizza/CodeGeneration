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

import java.util.List;
import java.util.UUID;

import static org.h2.value.DataType.readValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetUserStepDefinitions extends BaseStepDefinitions implements En {

    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;

    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    private static final String INVALID_TOKEN = "invalid";

    private static final String INVALID_USER_ID = "kaaskaas-kaas-kaas-kaas-kaaskaaskaas";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private ResponseEntity<GetUserDTO> response;
    private ResponseEntity<String> getAllResponse;

    private List<GetUserDTO> userList;
    private int limit = 10;
    private int offset = 0;
    private HttpEntity<String> request;
    private Integer status;

    private GetUserDTO getUserDTO;
    private String id;

    public GetUserStepDefinitions() {

        Given("^I have a valid token for an admin", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        /*Given("^The limit is higher than 50", () -> {
            limit = 51;
        });

        Given("^The limit is lower than 1", () -> {
            limit = -10;
        });*/

        When("^I call the GetAllUsers endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(null, httpHeaders);
            getAllResponse = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));
        });

        /*Then("^I should get a status code of (\\d+)", (Integer statusCode) -> {
            assertEquals(statusCode, status);
        });*/

        Then("^I should see all the users in a list and return a (\\d+) status code", (Integer statusCode) -> {
            assertEquals(statusCode, status);
            userList.forEach(user -> {
                assertEquals(true, user.getUserid() != null);
                assertEquals(true, user.getUsername() != null);
                assertEquals(true, user.getEmail() != null);
                assertEquals(true, user.getFirstName() != null);
                assertEquals(true, user.getLastName() != null);
                assertEquals(true, user.getStreet() != null);
                assertEquals(true, user.getCity() != null);
                assertEquals(true, user.getZipcode() != null);
                assertEquals(true, user.getUserstatus() != null);
                assertEquals(true, user.getDayLimit() != null);
                assertEquals(true, user.getTransactionLimit() != null);
                assertEquals(true, user.getRoles() != null);
            });
        });

        /*Then ("^I should get the first 20 users and return a (\\d+) status code", (Integer statusCode) -> {
            assertEquals(statusCode, status);
            assertEquals(2, userList.size());
        });

        And("^I have a valid admin token", () -> {
            token = VALID_TOKEN_ADMIN;
        });*/

        Given("^I provide a correct user id", () -> {
        });

        Given("^I have an invalid user id", () -> {
            id = INVALID_USER_ID;
        });

        Given("I have a user id that is null", () -> {
            id = null;
        });

        When("^I call the GetAllUser endpoint i get all users and use the id of the first user to get a user from GetUser", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(null, httpHeaders);
            getAllResponse = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            id = String.valueOf(userList.get(0).getUserid());

            httpHeaders.clear();
            // get id from /Users/{id}
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(id, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.GET, request, GetUserDTO.class);
            status = response.getStatusCode().value();
        });

        When("^I call the GetUser endpoint", () -> {
            httpHeaders.clear();
            // get id from /Users/{id}
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(id, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.GET, request, GetUserDTO.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a user status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^I have a valid token", () -> {
            token = VALID_TOKEN_USER;
        });
    }
}
