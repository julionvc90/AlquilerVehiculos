package com.example.inspeccion.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class AlquilerClient {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerClient.class);

    private final WebClient webClient;

    public AlquilerClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8086")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public boolean existeAlquiler(Long alquilerId) {
        logger.info("Verificando existencia de alquiler con ID: {}", alquilerId);
        try {
            return Boolean.TRUE.equals(
                    webClient.get()
                            .uri("/api/alquileres/{id}/existe", alquilerId)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()
            );
        } catch (Exception e) {
            logger.error("Error al verificar alquiler {}: {}", alquilerId, e.getMessage());
            return false;
        }
    }
}
