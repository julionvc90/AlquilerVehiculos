package com.example.pago.webclient;

import com.example.pago.dto.ReservaResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class ReservaClient {

    private static final Logger logger = LoggerFactory.getLogger(ReservaClient.class);

    private final WebClient webClient;

    public ReservaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://reserva")
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
