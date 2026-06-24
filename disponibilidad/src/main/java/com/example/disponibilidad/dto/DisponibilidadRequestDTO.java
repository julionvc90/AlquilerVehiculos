package com.example.disponibilidad.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadRequestDTO {

    @NotNull(message = "El ID Del Vehiculo Es Obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La Fecha De Inicio Es Obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La Fecha Del De Fin Es Obligatoria")
    private LocalDate fechaFin;

    @NotNull(message = "Debe Indicar Si El Vehiculo Esta Disponible")
    private Boolean disponible;
}
