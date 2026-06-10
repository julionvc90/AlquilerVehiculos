package com.example.pago.dto;

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
    private Long idPago;
    private Long idReserva;
    private Long idVehiculo;
    private LocalDate fechaPago;
    private BigDecimal montoPago;
    private String metodoPago;
    private String estadoPago;
    private String transaccionPago;

}
