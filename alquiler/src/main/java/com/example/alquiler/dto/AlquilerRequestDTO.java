package com.example.alquiler.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerRequestDTO {

    @NotNull(message = "El Alquiler Debe Tener Un Cliente")
    @Positive(message = "El clienteId debe ser un numero positivo")
    private Long clienteId;

    @NotNull(message = "El Alquiler Debe Tener Un Vehiculo")
    @Positive(message = "El vehiculoId debe ser un numero positivo")
    private Long vehiculoId;

    @NotNull(message = "El Alquiler Debe Tener Una Reserva Asociada")
    @Positive(message = "El reservaId debe ser un numero positivo")
    private Long reservaId;

    @NotNull(message = "El Alquiler Debe Tener Una Fecha De Inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "El Alquiler Debe Tener Una Fecha De Fin")
    private LocalDate fechaFin;
}
