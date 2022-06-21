package io.swagger.cucumber.steps.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.JwtDTO;
import io.swagger.model.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
public class LoginStepDefinitions extends BaseStepDefinitions implements En {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<JwtDTO> response;
    private LoginDTO dto;

    public LoginStepDefinitions() {
        When("^I call the login endpoint$", () -> {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/login", request, JwtDTO.class);
        });

        Then("^I receive a status of (\\d+)$", (Integer status) -> {
            Assertions.assertEquals(status.intValue(), response.getStatusCodeValue());
        });

        And("^I get a JWT-token$", () -> {
            String token = response.getBody().getJwtToken();
            Assertions.assertTrue(token.startsWith("ey"));
        });
        Given("^I have a valid user object$", () -> {
            dto = new LoginDTO("test2", "test2");
        });
        Given("^I have an invalid user object$", () -> {
            dto = new LoginDTO("c", "c");
        });

        Given("^My password is invalid$", () -> {
            dto = new LoginDTO("test", "c");
        });
    }


}
