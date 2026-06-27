package com.example.pago.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequestDTO {

    private Long idPago;

    @NotNull(message = "El idReserva es obligatorio")
    @Positive(message = "El idReserva debe ser un numero positivo")
    private Long idReserva;

    @NotNull(message = "El idVehiculo es obligatorio")
    @Positive(message = "El idVehiculo debe ser un numero positivo")
    private Long idVehiculo;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    @NotNull(message = "El monto pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoPago;

    @NotBlank(message = "El metodo de pago es obligatorio (Efectivo, Tarjeta, Transferencia, WebPay)")
    private String metodoPago;

    @NotBlank(message = "El estado de pago es obligatorio (Ingresada, Anulada, Cancelada, Pendiente, EnCobranza)")
    private String estadoPago;

    @NotBlank(message = "El numero de transaccion es obligatorio")
    private String transaccionPago;
}
