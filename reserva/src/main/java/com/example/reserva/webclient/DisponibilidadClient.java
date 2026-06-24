package com.example.reserva.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDate;

@Component
public class DisponibilidadClient {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadClient.class);

    private final WebClient webClient;

    public DisponibilidadClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10));
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:9095")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Boolean validarDisponibilidad(Long vehiculoId, LocalDate inicio, LocalDate fin) {
        logger.info("Validando disponibilidad del vehiculo ID: {} desde {} hasta {}", vehiculoId, inicio, fin);
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/disponibilidad/validar")
                            .queryParam("vehiculoId", vehiculoId)
                            .queryParam("inicio", inicio)
                            .queryParam("fin", fin)
                            .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            logger.error("Error al validar disponibilidad: {}", e.getMessage());
            throw new RuntimeException("No se pudo validar la disponibilidad del vehiculo", e);
        }
    }
}
