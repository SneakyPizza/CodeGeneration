package io.swagger.cucumber.steps.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.JWT_DTO;
import io.swagger.model.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Slf4j
public class LoginStepDefinitions extends BaseStepDefinitions implements En {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<JWT_DTO> response;
    private LoginDTO dto;

    public LoginStepDefinitions() {
        When("^I call the login endpoint$", () -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/login", request, JWT_DTO.class);
        });

        Then("^I receive a status of (\\d+)$", (Integer status) -> {
            Assertions.assertEquals(status.intValue(), response.getStatusCodeValue());
        });

        And("^I get a JWT-token$", () -> {
            String token = response.getBody().getJwTtoken();
            Assertions.assertTrue(token.startsWith("ey"));
        });
        Given("^I have a valid user object$", () -> {
            dto = new LoginDTO("test", "test");
        });
        Given("^I have an invalid user object$", () -> {
            dto = new LoginDTO("c", "c");
        });
    }


}
