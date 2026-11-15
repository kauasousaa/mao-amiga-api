package com.maoamiga.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI maoAmigaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Instituto Mão Amiga API")
                .description("CRUD de pontos de coleta e distribuição em Goiânia.")
                .version("v1"));
    }
}
