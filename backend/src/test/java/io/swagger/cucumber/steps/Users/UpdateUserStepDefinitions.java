package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.GetUserDTO;
import io.swagger.model.UserDTO;
import org.aspectj.weaver.ast.And;
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
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UpdateUserStepDefinitions extends BaseStepDefinitions implements En  {
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
    private ResponseEntity<String> getAllResponse;

    private List<UserDTO> userList;
    private HttpEntity<String> request;
    private Integer status;

    private UserDTO userDTO;
    private String id;

    public UpdateUserStepDefinitions() {
        Given("^I give valid user information", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        When("^I call the UpdateUser endpoint", () -> {
            // gives json user
            /*userDTO = new UserDTO();
            userDTO.setId(UUID.randomUUID().toString());
            userDTO.setFirstName("Test");
            userDTO.setLastName("Test");
            userDTO.setEmail("test@test.nl");
            userDTO.setPassword("test");
            userDTO.setRole("USER");
            userDTO.setActive(true);*/
            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            request = new HttpEntity<>(objectMapper.writeValueAsString(userDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + userDTO.getUserid(), HttpMethod.PUT, request, UserDTO.class);
            status = response.getStatusCode().value();
            userDTO = response.getBody();
        });

        Then("^I should get my updated user and get a status code of (\\d+)", (Integer statusCode) -> {
            // checks if user is received and if status code is correct
            assertNotNull(userDTO.getUserid());
            assertNotNull(userDTO.getUsername());
            assertNotNull(userDTO.getPassword());
            assertNotNull(userDTO.getEmail());
            assertNotNull(userDTO.getFirstName());
            assertNotNull(userDTO.getLastName());
            assertNotNull(userDTO.getStreet());
            assertNotNull(userDTO.getCity());
            assertNotNull(userDTO.getZipcode());
            assertNotNull(userDTO.getUserstatus());
            assertNotNull(userDTO.getDayLimit());
            assertNotNull(userDTO.getTransactionLimit());
            assertNotNull(userDTO.getRoles());
            Assertions.assertEquals(statusCode, status);
        });

        And("^I have a valid jwt token", () -> {
            token = VALID_TOKEN_USER;
        });
    }
}
