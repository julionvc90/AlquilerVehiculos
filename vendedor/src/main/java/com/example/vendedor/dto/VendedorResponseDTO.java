package com.example.vendedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendedorResponseDTO {

    @Schema(description = "ID único del vendedor", example = "1")
    private Long id;
    @Schema(description = "RUT chileno", example = "10.123.456-7")
    private String rut;
    @Schema(description = "Nombre del vendedor", example = "María")
    private String nombre;
    @Schema(description = "Apellido del vendedor", example = "Gómez")
    private String apellido;
    @Schema(description = "Correo electrónico", example = "maria@empresa.com")
    private String email;
    @Schema(description = "Teléfono de contacto", example = "+56987654321")
    private String telefono;
    @Schema(description = "ID del usuario asociado", example = "300002")
    private Long usuarioId;
    @Schema(description = "Indica si el vendedor está activo", example = "true")
    private boolean activo;
}
