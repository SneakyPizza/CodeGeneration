package io.swagger.cucumber.steps.Account;

import io.cucumber.java8.En;
import io.swagger.cucumber.steps.BaseStepDefinitions;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.NameSearchAccountDTO;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class SearchAccountDefinitions extends BaseStepDefinitions implements En {
    
    @Value("${io.swagger.api.token_USER}")
    private String VALID_TOKEN_USER;
    private static final String INVALID_TOKEN_USER = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCYW5rIiwiYXV0aCI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwiaWF0IjoxNjU1ODUyNTI2LCJleHAiOjE2NTU4NTYxMjZ9.0jeTgH3wBYZ1lhmUnmBrT2N2VdO4ZO5V2tU7PoDvVdq";

    private static final String VALID_HEADER = "Search";
    private static final String INVALID_HEADER = "Invalid";

    private static final String VALID_FULLNAME = "test2-test2";
    private static final String INVALID_FULLNAME = "Invalid";

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private HttpEntity<String> request;
    private ResponseEntity<String> response;
    private Integer status;

    private String token;
    private String header;
    private String fullname;
    private ErrorDTO errorDTO;
    private List<NameSearchAccountDTO> listNameSearchDTOs;

    public SearchAccountDefinitions(){
        Given("^'search-account' I provide valid user credentials", () -> {
            token = VALID_TOKEN_USER;
        });

        And("^'search-account' My fullname is valid", () -> {
            fullname = VALID_FULLNAME;
        });

        And("^'search-account' My accept header is valid", () -> {
            header = VALID_HEADER;
        });

        When("^'search-account' I perform a search account operation", () -> {
            httpHeaders.clear();
            httpHeaders.add("Authorization", "Bearer " + token);
            httpHeaders.add(header, "application/json");
            request = new HttpEntity<>(null, httpHeaders);
            response = restTemplate.exchange(getBaseUrl() + "/Accounts/Search?fullName=" + fullname + "&offset=1&limit=20", HttpMethod.GET, request, String.class);
            status = response.getStatusCode().value();
        });

        Then("^I should see a search account status code of (\\d+)", (Integer statusCode) -> {
            Assertions.assertEquals(statusCode, status);
            System.out.println("\u001B[32m" +"Status code: " + response.getStatusCode() + "\u001B[0m");
            System.out.println("\u001B[32m" +"Response: " + response.getBody() + "\u001B[0m");
        });

        And("^I should receive an search account error message with \"([^\"]*)\"$", (String arg0) -> {
            errorDTO =  mapper.readValue(response.getBody(), ErrorDTO.class);
            Assertions.assertEquals(arg0, errorDTO.getMessage());
            Assertions.assertNotNull(errorDTO.getTimestamp());
            Assertions.assertNotNull(errorDTO.getStatus());
            Assertions.assertNotNull(errorDTO.getError());
        });

        And("^I should receive a list of namesearchdtos from the database", () -> {
            listNameSearchDTOs = mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, NameSearchAccountDTO.class));

            for (NameSearchAccountDTO dto : listNameSearchDTOs) {
                Assertions.assertNotNull(dto.getFirstName());
                Assertions.assertNotNull(dto.getIBAN());
                Assertions.assertNotNull(dto.getLastName());
            }
        });

        Given("^'search-account' I provide invalid user credentials", () -> {
            token = INVALID_TOKEN_USER;
        });

        And("^'search-account' My fullname is invalid", () -> {
            fullname = INVALID_FULLNAME;
        });

        And("^'search-account' My accept header is invalid", () -> {
            header = INVALID_HEADER;
        });
    }
}
