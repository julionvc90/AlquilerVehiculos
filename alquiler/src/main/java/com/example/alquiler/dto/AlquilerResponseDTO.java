package com.example.alquiler.dto;

import com.example.alquiler.enums.EstadoAlquiler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerResponseDTO {
    private long id;
    private long clienteId;
    private long vehiculoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer dias;
    private Double tarifaDiaria;
    private Double montoTotal;
    private EstadoAlquiler estado;
}
