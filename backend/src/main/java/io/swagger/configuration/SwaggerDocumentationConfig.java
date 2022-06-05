package io.swagger.configuration;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.model.dto.ErrorDTO;
import io.swagger.model.dto.GetTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import java.util.Collections;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-04T11:04:07.506Z[GMT]")
@Configuration
public class SwaggerDocumentationConfig {

    @Autowired
    TypeResolver typeResolver;

    @Bean
    public Docket customImplementation(){
        HttpAuthenticationScheme bearerAuth = HttpAuthenticationScheme
                .JWT_BEARER_BUILDER
                .name("bearerAuth")
                .build();

        return new Docket(DocumentationType.OAS_30)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("io.swagger.api"))
                    .build().additionalModels(typeResolver.resolve(ErrorDTO.class), typeResolver.resolve(GetTransactionDTO.class))
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .securitySchemes(Collections.singletonList(bearerAuth))
                .apiInfo(apiInfo());
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("BankAPI")
            .description("a bank api")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("4.0")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("BankAPI")
                .description("a bank api")
                .termsOfService("")
                .version("4.0")
                .license(new License()
                    .name("")
                    .url("http://unlicense.org"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("")));
    }

//    @Bean
//    public Docket jwtSecuredDocket() {
//        HttpAuthenticationScheme authenticationScheme = HttpAuthenticationScheme
//                .JWT_BEARER_BUILDER
//                .name("JWT_Token")
//                .build();
//
//        return new Docket(DocumentationType.OAS_30)
//                // <...>
//                .securitySchemes(Collections.singletonList(authenticationScheme));
//    }

}
