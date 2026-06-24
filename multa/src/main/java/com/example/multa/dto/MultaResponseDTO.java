package com.example.multa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class MultaResponseDTO {
    @Schema(description = "ID único de la multa", example = "1")
    private Long idMulta;
    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long idReserva;
    @Schema(description = "ID del vehículo", example = "1")
    private Long idVehiculo;
    @Schema(description = "Motivo de la multa", example = "Devolución tardía 2 días")
    private String motivoMulta;
    @Schema(description = "Monto de la multa en pesos", example = "50000")
    private BigDecimal montoMulta;
    @Schema(description = "Fecha en que se generó la multa")
    private LocalDate fechaMulta;
    @Schema(description = "Estado (Pendiente, Pagada, Anulada)", example = "Pendiente")
    private String estadoMulta;

}
