package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.AccountDTO;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetUserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class UpdateAccountStatusDefinitions extends BaseStepDefinitions implements En {

    @Value("${io.swagger.api.token_ADMIN}")
    private String VALID_TOKEN_ADMIN;

    @Value("${io.swagger.api.token_USER}")
    private String INVALID_TOKEN;

    private static final String VALID_HEADER = "Add";
    private static final String INVALID_HEADER = "Accept";

    private static final String VALID_IBAN = "NL01INHO0000000001";
    private static final String INVALID_IBAN = "DE01INHO0000000001";

    //private static final String VALID_ACCOUNT;
    private static final String INVALID_ACCOUNT = "{\n  \"accountType\": \"Test\" ,\n \"userid\": \"35500de4-cc69-4c09-a4a6-c08b320d0692\",\n  \"active\": \"active\",\n  \"absoluteLimit\": \"10000\"\n}";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;
    private String iban;
    private String account;
    private List<GetUserDTO> users;
    private ErrorDTO errorDTO;
    private AccountDTO accountDTO;

    public UpdateAccountStatusDefinitions(){
        Given("^'update-account' I provide valid admin credentials", () -> {
            token = VALID_TOKEN_ADMIN;
        });

        And("^'update-account' My iban is valid", () -> {
            iban = VALID_IBAN;
        });

        And("^'update-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        And("^'update-account' My account is valid", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + VALID_TOKEN_ADMIN);
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Users?limit=1&offset=2", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
            users = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, GetUserDTO.class));

            String id = String.valueOf(users.get(0).getUserid());
            
            account = "{\n  \"accountType\": \"savings\" ,\n  \"userid\": " + "\"" + id + "\"" + ",\n  \"IBAN\": \"NL01INHO0000000001\",\n \"balance\": \"100\",\n \"active\": \"active\",\n \"absoluteLimit\": \"1337\"\n}";
        });

        When("^'update-account' I perform a update account operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(account, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts/" + iban, HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a update account status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^I should receive an update account error message with \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        And("^I should receive the updated account added to the database", () -> {
            accountDTO = mapper.readValue(response.getBody(), AccountDTO.class);
            assertNotNull(accountDTO.getAbsoluteLimit());
            assertNotNull(accountDTO.getAccountType());
            assertNotNull(accountDTO.getActive());
            assertNotNull(accountDTO.getBalance());
            assertNotNull(accountDTO.getIBAN());
            assertNotNull(accountDTO.getUserid());
        });

        And("^'update-account' My account is invalid", () -> {
            account = INVALID_ACCOUNT;
        });

        And("^'update-account' My header is invalid", () -> {
            header = INVALID_HEADER;
        });

        And("^'update-account' I provide invalid admin credentials", () -> {
            token = INVALID_TOKEN;
        });

        And("^'update-account' My iban is invalid", () -> {
            iban = INVALID_IBAN;
        });
    }
}
