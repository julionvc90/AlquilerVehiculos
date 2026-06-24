package com.example.inspeccion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionResponseDTO {

    private Long id;
    private Long alquilerId;
    private Long vehiculoId;
    private LocalDateTime fechaInspeccion;
    private String tipoInspeccion;
    private String resultado;
    private String observaciones;
    private String inspector;
    private boolean activo;
}
