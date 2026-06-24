package com.example.multa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {

    private Long idReserva;
    private Long idCliente;
    private Long idVehiculo;
    private BigDecimal totalReserva;
    private String estadoReserva;
}
