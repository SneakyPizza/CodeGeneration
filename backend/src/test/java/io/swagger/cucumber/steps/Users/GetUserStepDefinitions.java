package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class GetUserStepDefinitions extends BaseStepDefinitions implements En {

    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;

    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    private static final String INVALID_USER_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

    private static final String WRONG_FORMAT_USER_ID = "3fa85f64-5717-4562-b3fc";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private ResponseEntity<String> response;
    private ResponseEntity<String> getAllResponse;

    private List<GetUserDTO> userList;
    private int limit = 10;
    private int offset = 0;
    private HttpEntity<String> request;
    private Integer status;
    private GetUserDTO getUserDTO;
    private ErrorDTO errorDTO;
    private String id;

    public GetUserStepDefinitions() {
        //
        // GetAllUsers
        //
        Given("^I have a valid token for an admin", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        Given("^I have a valid token for a user", () -> {
            token = VALID_TOKEN_USER;
        });

        When("^I call the GetAllUsers endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(httpHeaders);
            // query with limit and offset
            getAllResponse = restTemplate.exchange( getBaseUrl() + "/Users?limit=" + limit + "&offset=" + offset, HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
        });

        And("^I get a list of users", () -> {
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));
        });

        And("^I have a valid limit and offset", () -> {
            limit = 10;
            offset = 0;
        });

        And("^The limit is higher than 50", () -> {
            limit = 51;
            offset = 0;
        });

        And("^The limit is lower than 1", () -> {
            limit = -10;
            offset = 0;
        });

        And("^The offset is lower than 0", () -> {
            limit = 10;
            offset = -10;
        });

        And("^The offset is higher than the total number of users", () -> {
            limit = 10;
            offset = 1000000000;
        });

        And("^I should see a list of users", () -> {
            userList.forEach(user -> {
                assertNotNull(user.getUserid());
                assertNotNull(user.getUsername());
                assertNotNull(user.getEmail());
                assertNotNull(user.getFirstName());
                assertNotNull(user.getLastName());
                assertNotNull(user.getStreet());
                assertNotNull(user.getCity());
                assertNotNull(user.getZipcode());
                assertNotNull(user.getUserstatus());
                assertNotNull(user.getDayLimit());
                assertNotNull(user.getTransactionLimit());
                assertNotNull(user.getRoles());
                assertNotNull(user.getAccounts());
            });
        });

        And("^I get an error object with message \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  objectMapper.readValue(getAllResponse.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        //
        // GetUser
        //
        And("^I provide a correct user id from the GetAllUsers endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  VALID_TOKEN_ADMIN);
            request = new HttpEntity<>(httpHeaders);
            getAllResponse = restTemplate.exchange( getBaseUrl() + "/Users?limit=" + limit + "&offset=" + offset, HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            id = String.valueOf(userList.get(0).getUserid());
        });

        Given("^I have a user id that is in wrong format", () -> {
            id = WRONG_FORMAT_USER_ID;
        });

        Given("^I have a user id that is not in the database", () -> {
            id = INVALID_USER_ID;
        });

        When("^I call the GetUser endpoint", () -> {
            httpHeaders.clear();
            // get id from /Users/{id}
            httpHeaders.add("Authorization", "Bearer " +  token);
            request = new HttpEntity<>(id, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a (\\d+) status code", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
        });

        And("^I should see a user object" , () -> {
            getUserDTO = objectMapper.readValue(response.getBody(), GetUserDTO.class);
            assertNotNull(getUserDTO.getUserid());
            assertNotNull(getUserDTO.getUsername());
            assertNotNull(getUserDTO.getEmail());
            assertNotNull(getUserDTO.getFirstName());
            assertNotNull(getUserDTO.getLastName());
            assertNotNull(getUserDTO.getStreet());
            assertNotNull(getUserDTO.getCity());
            assertNotNull(getUserDTO.getZipcode());
            assertNotNull(getUserDTO.getUserstatus());
            assertNotNull(getUserDTO.getDayLimit());
            assertNotNull(getUserDTO.getTransactionLimit());
            assertNotNull(getUserDTO.getRoles());
            assertNotNull(getUserDTO.getAccounts());
        });

        And("^I have a valid admin token", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^I get an user error with message \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  objectMapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });
    }
}
