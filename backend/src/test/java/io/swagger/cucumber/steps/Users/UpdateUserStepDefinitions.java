package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.GetUserDTO;
import io.swagger.model.dto.PostUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UpdateUserStepDefinitions extends BaseStepDefinitions implements En  {
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;

    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    private static final String INVALID_TOKEN = "invalid";

    private static final String INVALID_USER_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private ResponseEntity<PostUserDTO> response;
    private ResponseEntity<String> getAllResponse;

    private List<GetUserDTO> userList;
    private HttpEntity<String> request;
    private Integer status;

    private PostUserDTO postUserDTO;
    private String id;

    public UpdateUserStepDefinitions() {
        Given("^I give valid user information", () -> {
            postUserDTO = new PostUserDTO();
            postUserDTO.setUsername("test");
            postUserDTO.setFirstName("Test");
            postUserDTO.setLastName("Test");
            postUserDTO.setEmail("test@test.nl");
            postUserDTO.setPassword("test");
            postUserDTO.setStreet("Test");
            postUserDTO.setCity("Test");
            postUserDTO.setZipcode("Test");
            postUserDTO.setDayLimit(BigDecimal.valueOf(10));
            postUserDTO.setTransactionLimit(BigDecimal.valueOf(10));
            postUserDTO.setRoles(Collections.singletonList(PostUserDTO.Role.ADMIN));
        });

        Given("^I give invalid user information", () -> {
            postUserDTO = new PostUserDTO();
            postUserDTO.setUsername("test");
            postUserDTO.setFirstName("Test");
            postUserDTO.setLastName(null);
            postUserDTO.setEmail("test@test.nl");
            postUserDTO.setPassword("test");
            postUserDTO.setStreet(null);
            postUserDTO.setCity(null);
            postUserDTO.setZipcode("Test");
            postUserDTO.setDayLimit(BigDecimal.valueOf(10));
            postUserDTO.setTransactionLimit(BigDecimal.valueOf(10));
            postUserDTO.setRoles(Collections.singletonList(PostUserDTO.Role.ADMIN));
        });

        When("^I call the UpdateUser endpoint", () -> {
            httpHeaders.clear();
            // first set userdto to json
            String json = objectMapper.writeValueAsString(postUserDTO);
            // then set the token
            httpHeaders.set("Authorization", "Bearer " + token);
            // then set the content type
            httpHeaders.set("Content-Type", "application/json");
            // then set the request
            request = new HttpEntity<>(json, httpHeaders);
            // then call the endpoint
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.PUT, request, PostUserDTO.class);
            // then get the status
            status = response.getStatusCodeValue();
            // then set the userdto from response
            postUserDTO = response.getBody();
            httpHeaders.clear();
        });

        Then("^I should get my updated user and get a status code of (\\d+)", (Integer statusCode) -> {
            // checks if userdto is defined
            assertNotNull(postUserDTO);
            Assertions.assertEquals(statusCode, status);
        });

        Then("^I should get an error message and get a status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
        });

        And("^I have a valid jwt token", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^I have an invalid jwt token$", () -> {
            token = INVALID_TOKEN;
        });

        And("^The user id is null", () -> {
            id = null;
        });

        And("^The user id is not in the database", () -> {
            id = INVALID_USER_ID;
        });

        And("^I have a valid user id", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  VALID_TOKEN_ADMIN);
            request = new HttpEntity<>(null, httpHeaders);
            getAllResponse = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            id = String.valueOf(userList.get(0).getUserid());
        });
    }
}
