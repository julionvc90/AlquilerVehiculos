package com.example.vehiculo.client;

import com.example.vehiculo.dto.VendedorResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VendedorClient {

    private final WebClient webClient;

    public VendedorClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public VendedorResponseDTO obtenerVendedor(Long id) {

        return webClient
                .get()
                .uri("http://localhost:8083/api/vendedores/{id}", id)
                .retrieve()
                .bodyToMono(VendedorResponseDTO.class)
                .block();
    }
}