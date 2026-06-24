package com.example.reserva.webclient;

import com.example.reserva.dto.VehiculoResponseDTO;
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
                .baseUrl("http://localhost:9094")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
