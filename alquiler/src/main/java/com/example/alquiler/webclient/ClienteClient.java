package com.example.alquiler.webclient;

import com.example.alquiler.dto.ClienteResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class ClienteClient {

    private static final Logger logger = LoggerFactory.getLogger(ClienteClient.class);

    private final WebClient webClient;

    public ClienteClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://cliente")
                .build();
    }

    public ClienteResponseDTO obtenerCliente(Long id) {
        logger.info("Obteniendo cliente con ID: {}", id);
        try {
            return webClient
                    .get()
                    .uri("/api/clientes/{id}", id)
                    .retrieve()
                    .bodyToMono(ClienteResponseDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al obtener cliente {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo obtener el cliente con ID: " + id, e);
        }
    }
}
