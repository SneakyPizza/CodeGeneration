package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.AccountDTO;
import io.swagger.model.GetUserDTO;
import io.swagger.model.UserDTO;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class AddAccountDefinitions extends BaseStepDefinitions implements En {
    
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    private static final String INVALID_TOKEN = "invalid";
    private static final String VALID_HEADER = "Content-Type";
    private static final String INVALID_HEADER = "Invalid";
    private static final String VALID_ACCOUNT = "{\n  \"accountType\": \"savings\" ,\n  \"userid\": \"35500de4-cc69-4c09-a4a6-c08b320d0692\",\n  \"active\": \"active\",\n  \"absoluteLimit\": \"10000\"\n}";
    private static final String INVALID_ACCOUNT = "{\n  \"accountType\": \"Test\" ,\n \"userid\": \"35500de4-cc69-4c09-a4a6-c08b320d0692\",\n  \"active\": \"active\",\n  \"absoluteLimit\": \"10000\"\n}";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final ObjectMapper mapper = new ObjectMapper();

    private String token;
    private String account;
    private String header;

    private AccountDTO dto;

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;
    private List<GetUserDTO> users;
    
    public AddAccountDefinitions(){

        Given("^'add-account' I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^'add-account' My account object is valid", () -> {
            
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + VALID_TOKEN_ADMIN);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
            users = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            String id = String.valueOf(users.get(0).getUserid());
            
            account = "{\n  \"accountType\": \"savings\" ,\n  \"userid\": " + "\"" + id + "\"" + ",\n  \"active\": \"active\",\n  \"absoluteLimit\": \"10000\"\n}";
        });

        And("^'add-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'add-account' I perform a add account operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            dto = mapper.readValue(account, AccountDTO.class);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(mapper.writeValueAsString(dto), httpHeaders);
            response = restTemplate.postForEntity(getBaseUrl() + "/Accounts" , request, String.class);
            status = response.getStatusCode().value();
        });
        
        Then("^I should see a add account status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^'add-account' My account object is invalid", () -> {
            account = INVALID_ACCOUNT;
        });

        And("^'add-account' My token object is invalid", () -> {
            token  = INVALID_TOKEN;
        });

        And("^'add-account' My accept header is invalid", () -> {
            header = INVALID_HEADER;
        });
    }
}
