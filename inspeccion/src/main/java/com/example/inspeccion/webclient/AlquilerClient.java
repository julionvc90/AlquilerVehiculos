package com.example.inspeccion.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class AlquilerClient {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerClient.class);

    private final WebClient webClient;

    public AlquilerClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://alquiler")
                .build();
    }

    public boolean existeAlquiler(Long alquilerId) {
        logger.info("Verificando existencia de alquiler con ID: {}", alquilerId);
        try {
            return Boolean.TRUE.equals(
                    webClient.get()
                            .uri("/api/alquiler/{id}/existe", alquilerId)
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
