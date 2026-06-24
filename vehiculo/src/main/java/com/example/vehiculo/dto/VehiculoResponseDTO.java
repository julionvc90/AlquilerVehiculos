package com.example.vehiculo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoResponseDTO {

    private Long id;
    private String patente;
    private String marca;
    private String modelo;
    private Integer anio;
    private String categoria;
    private String capacidadPasajeros;
    private String color;
    private Long vendedorId;
    private Double tarifaDiaria;
    private String ubicacion;
    private boolean activo;

}
