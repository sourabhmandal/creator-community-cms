package com.nxtweb.supareel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sourabh Mandal",
                        email = "19mandal97@gmail.com",
                        url = "https://bitsofmandal"
                ),
                description = "API documentation of supareel platform",
                license = @License(name = "MIT")
        ),
        servers = {
            @Server(
                url = "http://localhost:8088/api/v1",
                description = "LOCAL ENV: when your application in build and run locally"
            ),
            @Server(
                url = "https://backend.supareel.com/api/v1",
                description = "LOCAL ENV: when your application in build and run locally"
            ),
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "for JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}