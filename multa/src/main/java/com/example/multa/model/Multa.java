// Claudio Carril 2026-06
package com.example.multa.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "multas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMulta;

    @Column(nullable = false, length = 14)
    @NotNull(message = "El IdReserva de Multa debe ser Ingresado")
    private Long idReserva;

    @Column(nullable = false, length = 14)
    @NotNull(message = "El IdVehiculo de Multa debe ser Ingresado")
    private Long idVehiculo;

    @Column(nullable = false, length = 250)
    @NotBlank(message = "El motivo de multa debe ser Ingresado (maximo 250 caracteres)")
    private String motivoMulta;

    @Column(nullable = false)
    @NotNull(message = "La Fecha de la Multa debe ser Ingresada")
    private LocalDate fechaMulta;

    @Column(nullable = false, length = 14)
    @NotNull(message = "La Monto de la Multa debe ser Ingresada")
    private BigDecimal montoMulta;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El Estado de la multa debe ser Ingresada")
    private String estadoMulta; // Pendiente, Pagada, Anulada

}
