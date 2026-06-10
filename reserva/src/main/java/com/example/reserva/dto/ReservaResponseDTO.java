package com.example.reserva.dto;

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
    private Long idReserva;
    private Long idVehiculo;
    private Long idCliente;

    private LocalDate fechaReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;

    private Integer totalDias;
    private BigDecimal valorDia;
    private BigDecimal totalReserva;

    private String estadoReserva;
    private String observacionesReserva;

}