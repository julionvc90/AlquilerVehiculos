package com.example.reserva.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Wrapper de respuesta exitosa para POST, PUT y DELETE")
public class RespuestaExitosa<T> {
    @Schema(description = "Mensaje descriptivo del resultado")
    private String mensaje;
    @Schema(description = "Datos de la entidad (nulo en DELETE)")
    private T data;
}
