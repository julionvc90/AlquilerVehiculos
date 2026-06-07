package com.example.alquiler.dto;

import com.example.alquiler.enums.EstadoAlquiler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerRequestDTO {

    @NotNull(message = "El Alquiler Debe Tener Un Cliente")
    private Long clienteId;

    @NotBlank(message = "El Alquiler Debe Tener Un Vehiculo")
    private Long vehiculoId;

    @NotBlank(message = "El Alquiler Debe Tener Una Fecha De Inicio")
    private LocalDate fechaInicio;

    @NotBlank
    private LocalDate fechaFin;

    @NotBlank(message = "El Alquiler Debe Tener Una Cantidad De Dias")
    private Integer dias;

    @NotBlank(message = "El Alquiler Debe Tener Una Tarifa Diaria")
    private Double tarifaDiaria;

    @NotBlank(message = "El Alquiler Debe Tener Un Monto Total")
    private Double montoTotal;


    @Enumerated(EnumType.STRING)
    @NotBlank(message = "El Alquiler Debe Tener Un Estado")
    private EstadoAlquiler estado;
}
