package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.PostAsUserDTO;
import io.swagger.model.dto.PostUserDTO;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

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
    private ResponseEntity<String> response;

    private HttpEntity<String> request;
    private Integer status;

    private PostUserDTO postUserDTO;
    private PostAsUserDTO postAsUserDTO;
    private ErrorDTO errorDTO;

    private String id;

    public AddUserStepDefinitions() {
        Given("^I provide valid user details", () -> {
            postAsUserDTO = new PostAsUserDTO();
            postAsUserDTO.setUsername("Klaas");
            postAsUserDTO.setFirstName("Jansen");
            postAsUserDTO.setLastName("Test");
            postAsUserDTO.setEmail("klaasjansen@test.nl");
            postAsUserDTO.setPassword("test");
            postAsUserDTO.setStreet("Test");
            postAsUserDTO.setCity("Test");
            postAsUserDTO.setZipcode("Test");
            postAsUserDTO.setDayLimit(BigDecimal.valueOf(1000));
            postAsUserDTO.setTransactionLimit(BigDecimal.valueOf(100));
        });

        Given("^I provide wrong user details with null values", () -> {
            postUserDTO = new PostUserDTO();
            postUserDTO.setUsername(null);
        });

        When("I call the AddUser endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(postUserDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/signup", HttpMethod.POST, request, String.class);
            status = response.getStatusCode().value();
        });

        When("I call the AddUser endpoint twice", () -> {
            httpHeaders.clear();
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(postUserDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/signup", HttpMethod.POST, request, String.class);
            status = response.getStatusCode().value();

            httpHeaders.clear();
            httpHeaders.add("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(postUserDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/signup", HttpMethod.POST, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a user status code of (\\d+)", (Integer statusCode) -> {
            assertEquals(statusCode, status);
        });

        And("^I should receive an error message with \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  objectMapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        And("^I should receive the user added to the database", () -> {
            assertNotNull(response.getBody());
        });
    }
}
