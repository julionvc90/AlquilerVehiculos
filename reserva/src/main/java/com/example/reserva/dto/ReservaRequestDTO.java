package com.example.reserva.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class ReservaRequestDTO {

    private Long idReserva;

    @NotNull
    private Long idVehiculo;

    @NotNull
    private Long idCliente;

    @NotNull(message = "La Fecha de Reserva Servicio, no puede estar vacio")
    private LocalDate fechaReserva;

    @NotNull(message = "La Fecha de Inicio Servicio, no puede estar vacio")
    private LocalDate fechaInicio;

    @NotNull(message = "La Fecha de Termino Servicio, no puede estar vacio")
    private LocalDate fechaTermino;

    @NotNull(message = "Total dias, no puede estar vacio")
    private Integer totalDias;

    @NotNull(message = "Valor dia, no puede estar vacio")
    @DecimalMin("0.01")
    private BigDecimal valorDia;

    @NotNull(message = "Total de Reserva, no puede estar vacio")
    private BigDecimal totalReserva;

    @NotBlank(message = "Estado no puede estar vacio (Pendiente, Confirmada, Cancelada, Anulada)")
    private String estadoReserva;

    @NotBlank(message = "Observacion no puede estar vacio")
    @Size(min = 3, max = 255)
    private String observacionesReserva;
}
