package com.example.inspeccion.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class VehiculoClient {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoClient.class);

    private final WebClient webClient;

    public VehiculoClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public boolean existeVehiculo(Long vehiculoId) {
        logger.info("Verificando existencia de vehiculo con ID: {}", vehiculoId);
        try {
            return Boolean.TRUE.equals(
                    webClient.get()
                            .uri("/api/vehiculos/{id}/existe", vehiculoId)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()
            );
        } catch (Exception e) {
            logger.error("Error al verificar vehiculo {}: {}", vehiculoId, e.getMessage());
            return false;
        }
    }
}
