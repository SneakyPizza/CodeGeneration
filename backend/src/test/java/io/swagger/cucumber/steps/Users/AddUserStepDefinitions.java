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

        /*Given("^I provide all the user details", () -> {
            userDTO = new UserDTO();
            userDTO.setUserid(UUID.randomUUID());
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

        When("I call the AddUser endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            request = new HttpEntity<>(objectMapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.exchange("/Users", HttpMethod.POST, request, UserDTO.class);
            status = response.getStatusCode().value();
            userDTO = response.getBody();
        });

        Then("The user is added to the database and i get status code (\\d+)", (Integer statusCode) -> {
            // get the user from the database with id from the response
            id = userDTO.getUserid().toString();
            request = new HttpEntity<>(httpHeaders);
            // get user with id from /users/{id}
            response = restTemplate.exchange("/Users/" + id, HttpMethod.GET, request, UserDTO.class);

            Assertions.assertNotNull(response.getBody());
            assertEquals(statusCode, status);
        });*/
    }
}
