package com.example.vendedor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Vendedor")
                        .version("1.0")
                        .description("API de gestión de vendedores del Sistema de Alquiler de Vehículos. " +
                                "Operaciones CRUD, búsqueda por RUT y validación con Usuario."))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:9000")
                                .description("API Gateway")
                ));
    }
}
