package com.example.alquiler.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
public class DisponibilidadClient {

    private final WebClient webClient;

    public DisponibilidadClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Boolean validarDisponibilidad(
            Long vehiculoId,
            LocalDate inicio,
            LocalDate fin) {

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8085)
                        .path("/api/disponibilidad/validar")
                        .queryParam("vehiculoId", vehiculoId)
                        .queryParam("inicio", inicio)
                        .queryParam("fin", fin)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}