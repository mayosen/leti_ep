package com.mayosen.letipractice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Practice API")
                .description("Бэкенд проекта \"Распознавание документов\" по учебной практике.")
                .version("v1.0.0"));
    }
}
