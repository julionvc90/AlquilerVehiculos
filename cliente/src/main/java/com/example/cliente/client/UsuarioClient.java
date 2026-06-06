package com.example.cliente.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UsuarioClient {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioClient.class);

    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public boolean existeUsuario(Long usuarioId) {
        logger.info("Verificando existencia de usuario con ID: {}", usuarioId);
        try {
            return Boolean.TRUE.equals(
                    webClient.get()
                            .uri("/api/usuarios/{id}/existe", usuarioId)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()
            );
        } catch (Exception e) {
            logger.error("Error al verificar usuario {}: {}", usuarioId, e.getMessage());
            return false;
        }
    }
}
