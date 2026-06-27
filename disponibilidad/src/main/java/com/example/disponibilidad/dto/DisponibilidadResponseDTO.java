package com.example.disponibilidad.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponseDTO {
    @Schema(description = "ID único de la disponibilidad", example = "1")
    private Long id;
    @Schema(description = "ID del vehículo", example = "1")
    private Long vehiculoId;
    @Schema(description = "Fecha de inicio de disponibilidad", example = "2025-07-01")
    private LocalDate fechaInicio;
    @Schema(description = "Fecha de fin de disponibilidad", example = "2025-07-10")
    private LocalDate fechaFin;
    @Schema(description = "Indica si el vehículo está disponible", example = "true")
    private Boolean disponible;
}
