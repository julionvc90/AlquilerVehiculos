package com.example.multa.dto;

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
    private Long idMulta;
    private Long idReserva;
    private Long idVehiculo;
    private String motivoMulta;
    private BigDecimal montoMulta;
    private LocalDate fechaMulta;
    private String estadoMulta;

}
