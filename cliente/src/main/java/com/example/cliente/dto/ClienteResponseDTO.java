package com.example.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    @Schema(description = "ID único del cliente", example = "200001")
    private Long id;
    @Schema(description = "RUT chileno", example = "12.345.678-9")
    private String rut;
    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombre;
    @Schema(description = "Apellido del cliente", example = "Pérez")
    private String apellido;
    @Schema(description = "Correo electrónico", example = "juan@mail.com")
    private String email;
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;
    @Schema(description = "Dirección física", example = "Av. Siempre Viva 742")
    private String direccion;
    @Schema(description = "ID del usuario asociado en el sistema", example = "300001")
    private Long usuarioId;
    @Schema(description = "Indica si el cliente está activo", example = "true")
    private boolean activo;
}
