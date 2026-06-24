package com.example.vehiculo.webclient;

import com.example.vehiculo.dto.VendedorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class VendedorClient {

    private static final Logger logger = LoggerFactory.getLogger(VendedorClient.class);

    private final WebClient webClient;

    public VendedorClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:9093")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
