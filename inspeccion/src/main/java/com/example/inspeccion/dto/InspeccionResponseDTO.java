package com.example.inspeccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "ID único de la inspección", example = "1")
    private Long id;
    @Schema(description = "ID del alquiler asociado", example = "1")
    private Long alquilerId;
    @Schema(description = "ID del vehículo inspeccionado", example = "1")
    private Long vehiculoId;
    @Schema(description = "Fecha y hora de la inspección")
    private LocalDateTime fechaInspeccion;
    @Schema(description = "Tipo de inspección (Entrega, Devolucion)", example = "Devolucion")
    private String tipoInspeccion;
    @Schema(description = "Resultado (Aprobado, Rechazado)", example = "Aprobado")
    private String resultado;
    @Schema(description = "Observaciones adicionales", example = "Sin daños visibles")
    private String observaciones;
    @Schema(description = "Nombre del inspector", example = "Carlos Rodríguez")
    private String inspector;
    @Schema(description = "Indica si la inspección está activa", example = "true")
    private boolean activo;
}
