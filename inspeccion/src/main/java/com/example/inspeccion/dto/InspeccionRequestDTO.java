package com.example.inspeccion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionRequestDTO {

    @NotNull(message = "El alquilerId es obligatorio")
    private Long alquilerId;

    @NotNull(message = "El vehiculoId es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "La fecha de inspeccion es obligatoria")
    private LocalDateTime fechaInspeccion;

    @NotBlank(message = "El tipo de inspeccion es obligatorio")
    @Size(max = 20, message = "El tipo de inspeccion no puede exceder los 20 caracteres")
    private String tipoInspeccion;

    @NotBlank(message = "El resultado es obligatorio")
    @Size(max = 20, message = "El resultado no puede exceder los 20 caracteres")
    private String resultado;

    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres")
    private String observaciones;

    @Size(max = 100, message = "El nombre del inspector no puede exceder los 100 caracteres")
    private String inspector;
}
