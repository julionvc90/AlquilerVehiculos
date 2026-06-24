package com.example.pago.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {
    @Schema(description = "ID único del pago", example = "1")
    private Long idPago;
    @Schema(description = "ID de la reserva asociada", example = "1")
    private Long idReserva;
    @Schema(description = "ID del vehículo", example = "1")
    private Long idVehiculo;
    @Schema(description = "Fecha en que se realizó el pago", example = "2025-06-21")
    private LocalDate fechaPago;
    @Schema(description = "Monto pagado en pesos", example = "100000")
    private BigDecimal montoPago;
    @Schema(description = "Método de pago (Efectivo, Tarjeta, Transferencia, WebPay)", example = "Transferencia")
    private String metodoPago;
    @Schema(description = "Estado del pago (Ingresada, Pagada, Anulada, Pendiente)", example = "Pagada")
    private String estadoPago;
    @Schema(description = "Número de transacción", example = "TXN001")
    private String transaccionPago;

}
