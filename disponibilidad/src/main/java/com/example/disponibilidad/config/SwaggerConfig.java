package com.example.disponibilidad.config;

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
                        .title("Microservicio Disponibilidad")
                        .version("1.0")
                        .description("API de gestión de disponibilidad de vehículos del Sistema de Alquiler. " +
                                "Permite registrar periodos y validar disponibilidad por fechas."));
    }
}
