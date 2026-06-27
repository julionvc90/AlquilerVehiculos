package com.example.alquiler.webclient;

import com.example.alquiler.dto.PagoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
public class PagoClient {

    private static final Logger logger = LoggerFactory.getLogger(PagoClient.class);

    private final WebClient webClient;

    public PagoClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://pago")
                .build();
    }

    public List<PagoResponseDTO> buscarPagosPorReserva(Long reservaId) {
        logger.info("Buscando pagos de la reserva ID: {}", reservaId);
        try {
            PagoResponseDTO[] pagos = webClient
                    .get()
                    .uri("/api/pago/reserva/{reservaId}", reservaId)
                    .retrieve()
                    .bodyToMono(PagoResponseDTO[].class)
                    .block();
            return Arrays.asList(pagos != null ? pagos : new PagoResponseDTO[0]);
        } catch (Exception e) {
            logger.error("Error al buscar pagos de reserva {}: {}", reservaId, e.getMessage());
            throw new RuntimeException("No se pudieron obtener los pagos de la reserva con ID: " + reservaId, e);
        }
    }
}
