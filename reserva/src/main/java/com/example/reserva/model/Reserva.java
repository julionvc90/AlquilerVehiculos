// Claudio Carril 2026-06
package com.example.reserva.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "El Id de Reserva no puede estar vacio")
    @Column(nullable = false, length = 14)
    private Long idReserva;

    @NotNull(message = "El Codigo del Cliente, no puede estar vacio")
    @Column(nullable = false, length = 14)
    private Long idCliente;

    @NotNull(message = "El Codigo del Vehiculo, no puede estar vacio")
    @Column(nullable = false, length = 14)
    private Long idVehiculo;

    @Column(nullable = false)
    @NotNull(message = "La Fecha y Hora de Reserva, no puede estar vacio")
    private LocalDate fechaReserva;

    @Column(nullable = false)
    @NotNull(message = "La Fecha de Inicio Servicio, no puede estar vacio")
    private LocalDate fechaInicio;

    @Column(nullable = false)
    @NotNull(message = "La Fecha de Termino Servicio, no puede estar vacio")
    private LocalDate fechaTermino;

    @Column(nullable = false)
    @NotNull(message = "Total dias, no puede estar vacio")
    private Integer totalDias;

    @Column(nullable = false)
    @NotNull(message = "Valor dia, no puede estar vacio")
    private BigDecimal valorDia;      //BigDecimal

    @Column(nullable = false)
    @NotNull(message = "Total de Reserva, no puede estar vacio")
    private BigDecimal totalReserva;  //BigDecimal

    @Column(nullable = false)
    @NotBlank(message = "Estado, no puede estar vacio (Pendiente, Confirmada, Cancelada, Anulada)")
    private String estadoReserva;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Observacion, no puede estar vacio")
    private String observacionesReserva;

}
