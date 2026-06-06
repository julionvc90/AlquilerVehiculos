package com.example.disponibilidad.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadResponseDTO {
    private Long id;
    private Long vehiculoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean Disponible;
}
