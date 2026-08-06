package com.yogesh.employee_management_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()

                .info(new Info()

                        .title("Employee Management System API")

                        .version("1.0")

                        .description(
                                "REST API for Employee Management System built with Spring Boot, JWT Authentication, and MySQL."
                        )

                        .contact(new Contact()
                                .name("Y")
                                .email("your-email@example.com")
                        )

                        .license(new License()
                                .name("MIT License")
                        )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                )

                .schemaRequirement(
                        SECURITY_SCHEME_NAME,

                        new SecurityScheme()

                                .name(SECURITY_SCHEME_NAME)

                                .type(SecurityScheme.Type.HTTP)

                                .scheme("bearer")

                                .bearerFormat("JWT")
                );
    }
}
