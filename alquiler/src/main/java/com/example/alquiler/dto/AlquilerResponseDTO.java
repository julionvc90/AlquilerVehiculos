package com.example.alquiler.dto;

import com.example.alquiler.model.EstadoAlquiler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerResponseDTO {
    @Schema(description = "ID único del alquiler", example = "1")
    private long id;
    @Schema(description = "ID del cliente", example = "200001")
    private long clienteId;
    @Schema(description = "ID del vehículo", example = "1")
    private long vehiculoId;
    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long reservaId;
    @Schema(description = "Fecha de inicio del alquiler", example = "2025-07-01")
    private LocalDate fechaInicio;
    @Schema(description = "Fecha de fin del alquiler", example = "2025-07-05")
    private LocalDate fechaFin;
    @Schema(description = "Cantidad de días del alquiler", example = "4")
    private Integer dias;
    @Schema(description = "Tarifa diaria en pesos", example = "25000.0")
    private Double tarifaDiaria;
    @Schema(description = "Monto total calculado", example = "100000.0")
    private Double montoTotal;
    @Schema(description = "Estado actual (Reservado, ACTIVO, FINALIZADO, CANCELADO)", example = "Reservado")
    private EstadoAlquiler estado;
}
