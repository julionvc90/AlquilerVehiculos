// Claudio Carril 2026-06
package com.example.multa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @NotNull(message = "El IdReserva de Multa debe ser Ingresado")
    private Long idReserva;

    @NotNull(message = "El IdVehiculo de Multa debe ser Ingresado")
    private Long idVehiculo;

    @Column(nullable = false, length = 250)
    @NotBlank(message = "El motivo de multa debe ser Ingresado (maximo 250 caracteres)")
    private String motivoMulta;

    @Column(nullable = false)
    @NotNull(message = "La Fecha de la Multa debe ser Ingresada")
    private LocalDate fechaMulta;

    @NotNull(message = "La Monto de la Multa debe ser Ingresada")
    private BigDecimal montoMulta;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "El Estado de la multa debe ser Ingresada")
    private String estadoMulta; // Pendiente, Pagada, Anulada

}
