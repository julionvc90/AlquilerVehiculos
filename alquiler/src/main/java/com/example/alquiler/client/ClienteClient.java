package com.example.alquiler.client;

import com.example.alquiler.dto.ClienteResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ClienteClient {

    private final WebClient webClient;

    public ClienteClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ClienteResponseDTO obtenerCliente(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8081/api/clientes/" + id)
                .retrieve()
                .bodyToMono(ClienteResponseDTO.class)
                .block();
    }
}