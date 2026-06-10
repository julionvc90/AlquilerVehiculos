// Claudio Carril 2026-06
package com.example.pago.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@Table(name = "pago")
@NoArgsConstructor
@AllArgsConstructor

public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Numero de Pago, no puede estar vacia")
    @Column(nullable = false, length = 14)
    private Long idPago;

    @NotNull(message = "Numero de la reserva, no puede estar vacia")
    @Column(nullable = false, length = 14)
    private Long idReserva;

    @NotNull(message = "Numero del vehiculo, no puede estar vacia")
    @Column(nullable = false, length = 14)
    private Long idVehiculo;

    @NotNull(message = "La Fecha de Pago, no puede estar vacia")
    @Column(nullable = false)
    private LocalDate fechaPago;

    @NotNull(message = "El Monto del Pago, no puede estar vacia")
    @Column(nullable = false, length = 14)
    private BigDecimal montoPago;

    @NotBlank(message = "El metodo de pago, no puede estar vacio(Efectivo, Tarjeta, Transferencia, WebPay")
    @Column(nullable = false, length = 14)
    private String metodoPago;

    @NotBlank(message = "El estado de Pago, no puede estar vacia(Ingresada, Anulada, Cancelada, Pendiente, EnCobranza")
    @Column(nullable = false, length = 20)
    private String estadoPago;

    @NotBlank(message = "Numero de Transaccion, no puede estar vacia")
    @Column(nullable = false, length = 14)
    private String transaccionPago;

    // Getters y Setters
}