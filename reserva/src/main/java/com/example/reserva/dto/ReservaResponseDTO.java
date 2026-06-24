package com.example.reserva.dto;

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

public class ReservaResponseDTO {
    @Schema(description = "ID único de la reserva", example = "1")
    private Long idReserva;
    @Schema(description = "ID del vehículo reservado", example = "1")
    private Long idVehiculo;
    @Schema(description = "ID del cliente que reserva", example = "200001")
    private Long idCliente;

    @Schema(description = "Fecha en que se realizó la reserva", example = "2025-06-20")
    private LocalDate fechaReserva;
    @Schema(description = "Fecha de inicio del arriendo", example = "2025-07-01")
    private LocalDate fechaInicio;
    @Schema(description = "Fecha de término del arriendo", example = "2025-07-05")
    private LocalDate fechaTermino;

    @Schema(description = "Total de días de la reserva", example = "4")
    private Integer totalDias;
    @Schema(description = "Valor por día en pesos", example = "25000")
    private BigDecimal valorDia;
    @Schema(description = "Total a pagar", example = "100000")
    private BigDecimal totalReserva;

    @Schema(description = "Estado (Pendiente, Confirmada, Cancelada, Anulada)", example = "Confirmada")
    private String estadoReserva;
    @Schema(description = "Observaciones adicionales", example = "Sin novedades")
    private String observacionesReserva;

}