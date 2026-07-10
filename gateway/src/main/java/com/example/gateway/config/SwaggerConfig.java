package com.example.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway - Sistema de Alquiler de Vehículos")
                        .version("1.0")
                        .description("API Gateway que agrupa los 10 microservicios del sistema. " +
                                "Utiliza el selector 'Explore' para cambiar entre servicios."));
    }
}
