package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.model.AccountDTO;
import io.swagger.model.dto.ErrorDTO;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class GetAllAccountsDefinitions extends BaseStepDefinitions implements En{
    
    
    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    @Value("${io.swagger.api.token_USER}")
    private String INVALID_TOKEN;

    private static final String VALID_HEADER = "Content-Type";
    private static final String INVALID_HEADER = "Invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;
    private ErrorDTO errorDTO;
    private List<AccountDTO> dtos;

    public GetAllAccountsDefinitions(){
        Given("^'getall-account' I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^'getall-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'getall-account' I perform a get all accounts operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts?offset=1&limit=2", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a get all accounts status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^I should receive an get all accounts error message with \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        And("^I should receive a list of accountdtos from the database", () -> {
            dtos = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, AccountDTO.class));

            for (AccountDTO dto : dtos) {
                Assertions.assertNotNull(dto.getAbsoluteLimit());
                Assertions.assertNotNull(dto.getIBAN());
                Assertions.assertNotNull(dto.getAccountType());
                Assertions.assertNotNull(dto.getActive());
                Assertions.assertNotNull(dto.getBalance());
                Assertions.assertNotNull(dto.getUserid());
            }
        });

        And("^'getall-account' I provide invalid admin credentials", () -> {
            token = INVALID_TOKEN;
        });
    }
}
