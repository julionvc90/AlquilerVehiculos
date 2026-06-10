package com.example.alquiler.webclient;

import com.example.alquiler.dto.ReservaResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class ReservaClient {

    private static final Logger logger = LoggerFactory.getLogger(ReservaClient.class);

    private final WebClient webClient;

    public ReservaClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8089")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public ReservaResponseDTO obtenerReserva(Long id) {
        logger.info("Obteniendo reserva con ID: {}", id);
        try {
            return webClient
                    .get()
                    .uri("/api/reserva/{id}", id)
                    .retrieve()
                    .bodyToMono(ReservaResponseDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al obtener reserva {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo obtener la reserva con ID: " + id, e);
        }
    }
}
