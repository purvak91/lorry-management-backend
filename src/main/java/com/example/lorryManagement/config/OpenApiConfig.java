package com.example.lorryManagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Lorry Management API",
                version = "1.0.0",
                description = "Backend API for managing LR (Lorry Receipt) entries, including creation, update, deletion, and paginated search with filters."
        )
)
@Configuration
public class OpenApiConfig {
}
