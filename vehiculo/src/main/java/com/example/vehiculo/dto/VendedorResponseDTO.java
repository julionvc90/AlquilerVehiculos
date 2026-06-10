package com.example.vehiculo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendedorResponseDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Long usuarioId;
    private boolean activo;
}