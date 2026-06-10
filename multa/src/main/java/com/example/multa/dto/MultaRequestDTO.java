package com.example.multa.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultaRequestDTO {

    @NotNull
    private Long idReserva;

    @NotNull
    private Long idVehiculo;

    @NotNull
    @Size(min = 3, max = 250)
    private String motivoMulta;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal montoMulta;

    @NotNull
    private String estadoMulta; // Pendiente, Pagada, Anulada

}
