package com.example.vehiculo.webclient;

import com.example.vehiculo.dto.VendedorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class VendedorClient {

    private static final Logger logger = LoggerFactory.getLogger(VendedorClient.class);

    private final WebClient webClient;

    public VendedorClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://vendedor")
                .build();
    }

    public VendedorResponseDTO obtenerVendedor(Long id) {
        logger.info("Obteniendo vendedor con ID: {}", id);
        try {
            return webClient
                    .get()
                    .uri("/api/vendedores/{id}", id)
                    .retrieve()
                    .bodyToMono(VendedorResponseDTO.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al obtener vendedor {}: {}", id, e.getMessage());
            throw new RuntimeException("No se pudo obtener el vendedor con ID: " + id, e);
        }
    }
}
