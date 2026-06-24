package com.example.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    @Schema(description = "ID único del usuario", example = "300001")
    private Long id;
    @Schema(description = "Nombre de usuario", example = "jperez")
    private String username;
    @Schema(description = "Correo electrónico", example = "jperez@mail.com")
    private String email;
    @Schema(description = "Rol del usuario (ADMIN, CLIENTE, VENDEDOR)", example = "CLIENTE")
    private String rol;
    @Schema(description = "Indica si el usuario está activo", example = "true")
    private boolean activo;
}
