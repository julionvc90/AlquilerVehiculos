package com.example.disponibilidad.client;

import com.example.disponibilidad.dto.VehiculoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VehiculoClient {

    private final WebClient webClient;

    public VehiculoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public VehiculoResponseDTO obtenerVehiculo(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8084/api/vehiculos/{id}", id)
                .retrieve()
                .bodyToMono(VehiculoResponseDTO.class)
                .block();
    }
}