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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.h2.value.DataType.readValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddUserStepDefinitions extends BaseStepDefinitions implements En {

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
    private ResponseEntity<UserDTO> response;

    private HttpEntity<String> request;
    private Integer status;

    private UserDTO userDTO;
    private String id;

    public AddUserStepDefinitions() {
        Given("^I provide valid user details", () -> {
            userDTO = new UserDTO();
            userDTO.setUserid(UUID.randomUUID());
            userDTO.setUsername("Test");
            userDTO.setFirstName("Test");
            userDTO.setLastName("Test");
            userDTO.setEmail("test@test.nl");
            userDTO.setPassword("test");
            userDTO.setStreet("Test");
            userDTO.setCity("Test");
            userDTO.setZipcode("Test");
            userDTO.setUserstatus(UserDTO.UserstatusEnum.DISABLED);
            userDTO.setDayLimit(BigDecimal.valueOf(10));
            userDTO.setTransactionLimit(BigDecimal.valueOf(10));
            userDTO.setRoles(Collections.singletonList(UserDTO.Role.ADMIN));
        });

        Given("^I provide wrong user details with null values", () -> {
            userDTO = new UserDTO();
            userDTO.setUserid(UUID.randomUUID());
            userDTO.setUsername(null);
            userDTO.setFirstName("Test");
            userDTO.setLastName("Test");
            userDTO.setEmail(null);
            userDTO.setPassword("test");
            userDTO.setStreet("Test");
            userDTO.setCity("Test");
            userDTO.setZipcode("Test");
            userDTO.setUserstatus(null);
            userDTO.setDayLimit(BigDecimal.valueOf(10));
            userDTO.setTransactionLimit(null);
            userDTO.setRoles(Collections.singletonList(UserDTO.Role.ADMIN));
        });

        When("I call the AddUser endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.POST, request, UserDTO.class);
            status = response.getStatusCode().value();
        });

        When("I call the AddUser endpoint twice", () -> {
            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.POST, request, UserDTO.class);
            status = response.getStatusCode().value();

            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.POST, request, UserDTO.class);
            status = response.getStatusCode().value();
        });

        Then("^The user is added to the database and i get status code (\\d+)", (Integer statusCode) -> {
            assertNotNull(response);
            assertEquals(statusCode, status);
        });

        Then("^The user is not added to the database and i get status code (\\d+)", (Integer statusCode) -> {
            assertEquals(statusCode, status);
        });

        And("^I have a valid token jwt", () -> {
            token = VALID_TOKEN_USER;
        });

        And("^I have an invalid token jwt", () -> {
            token = INVALID_TOKEN;
        });
    }
}
