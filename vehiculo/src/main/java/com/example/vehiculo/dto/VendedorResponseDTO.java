package com.example.vehiculo.dto;

import lombok.Data;

@Data
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