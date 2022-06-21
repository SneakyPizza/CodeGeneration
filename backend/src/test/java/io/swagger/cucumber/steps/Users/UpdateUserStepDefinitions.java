package io.swagger.cucumber.steps.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
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

    private static final String INVALID_USER_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";

    private static final String WRONG_FORMAT_USER_ID = "3fa85f64-5717-4562-b3fc";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String token;
    private ResponseEntity<String> response;
    private ResponseEntity<String> getAllResponse;

    private List<GetUserDTO> userList;
    private HttpEntity<String> request;
    private Integer status;

    private PostUserDTO postUserDTO;
    private ErrorDTO errorDTO;
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
            postUserDTO.setUserstatus(PostUserDTO.UserstatusEnum.ACTIVE);
            postUserDTO.setDayLimit(BigDecimal.valueOf(10));
            postUserDTO.setTransactionLimit(BigDecimal.valueOf(10));
            postUserDTO.setRoles(Collections.singletonList(PostUserDTO.Role.ADMIN));
        });

        Given("^I give invalid user information", () -> {
            postUserDTO = new PostUserDTO();
            postUserDTO.setUsername(null);
            postUserDTO.setFirstName("Test");
            postUserDTO.setLastName(null);
            postUserDTO.setEmail("test@test.nl");
            postUserDTO.setPassword("test");
            postUserDTO.setStreet(null);
            postUserDTO.setCity(null);
            postUserDTO.setZipcode("Test");
            postUserDTO.setUserstatus(PostUserDTO.UserstatusEnum.ACTIVE);
            postUserDTO.setDayLimit(BigDecimal.valueOf(10));
            postUserDTO.setTransactionLimit(BigDecimal.valueOf(10));
            postUserDTO.setRoles(Collections.singletonList(PostUserDTO.Role.ADMIN));
        });

        When("^I call the UpdateUser endpoint", () -> {
            httpHeaders.clear();
            httpHeaders.set("Authorization", "Bearer " + token);
            httpHeaders.set("Content-Type", "application/json");
            request = new HttpEntity<>(objectMapper.writeValueAsString(postUserDTO), httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users/" + id, HttpMethod.PUT, request, String.class);
            status = response.getStatusCodeValue();
        });

        Then("^I should receive a status code of (\\d+) from the endpoint", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
        });

        And("^I have a valid admin jwt token", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^I have an user jwt token$", () -> {
            token = VALID_TOKEN_USER;
        });

        And("^The user id is in the wrong format", () -> {
            id = WRONG_FORMAT_USER_ID;
        });

        And("^The user id is not in the database", () -> {
            id = INVALID_USER_ID;
        });

        And("^I have a valid user id", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " +  VALID_TOKEN_ADMIN);
            request = new HttpEntity<>(httpHeaders);
            getAllResponse = restTemplate.exchange( getBaseUrl() + "/Users?limit=" + 10 + "&offset=" + 0, HttpMethod.GET, request, String.class);
            status = getAllResponse.getStatusCode().value();
            userList = objectMapper.readValue(getAllResponse.getBody(), objectMapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            id = String.valueOf(userList.get(0).getUserid());
        });

        And("^I should receive the updated user information", () -> {
            postUserDTO = objectMapper.readValue(response.getBody(), PostUserDTO.class);
            assertNotNull(postUserDTO.getUsername());
            assertNotNull(postUserDTO.getPassword());
            assertNotNull(postUserDTO.getFirstName());
            assertNotNull(postUserDTO.getLastName());
            assertNotNull(postUserDTO.getEmail());
            assertNotNull(postUserDTO.getStreet());
            assertNotNull(postUserDTO.getCity());
            assertNotNull(postUserDTO.getZipcode());
            assertNotNull(postUserDTO.getDayLimit());
            assertNotNull(postUserDTO.getTransactionLimit());
            assertNotNull(postUserDTO.getRoles());
            assertNotNull(postUserDTO.getUserstatus());
        });

        And("^I should receive a message stating \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  objectMapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });
    }
}
