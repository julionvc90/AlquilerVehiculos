package com.example.alquiler.webclient;

import com.example.alquiler.dto.VehiculoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class VehiculoClient {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoClient.class);

    private final WebClient webClient;

    public VehiculoClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://vehiculo")
                .build();
    }

    public VehiculoResponseDTO obtenerVehiculo(Long id) {
        logger.info("Obteniendo vehiculo con ID: {}", id);
        try {
            return webClient
                    .get()
                    .uri("/api/vehiculos/{id}", id)
                    .retrieve()
                    .bodyToMono(VehiculoResponseDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al obtener vehiculo {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo obtener el vehiculo con ID: " + id, e);
        }
    }
}
