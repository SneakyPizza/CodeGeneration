package io.swagger.cucumber.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = CucumberContextConfig.class)
@Slf4j
public class BaseStepDefinitions {

    @LocalServerPort
    private int port;

    @Value("${io.swagger.api.baseurl}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl + port;
    }
}
