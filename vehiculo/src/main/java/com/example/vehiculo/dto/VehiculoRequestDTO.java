package com.example.vehiculo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequestDTO {


    @NotBlank(message = "El Vehiculo Debe Tener Una Patente")
    @Size(max = 6, message = "La Patente no puede exceder los 6 caracteres")
    private String patente;

    @NotBlank(message = "El Vehiculo Debe Tener Una Marca")
    @Size(min = 3, max = 20, message = "La marca Debe estar entre los 3 y 20 caracteres")
    private String marca;

    @NotBlank(message = "El Vehiculo Debe Tener Un Modelo")
    @Size(max = 20, message = "El Modelo no puede exceder los 20 caracteres")
    private String modelo;

    @NotNull(message = "El Vehiculo Debe Tener Un Año")
    @Min(value = 1900, message = "El Año debe ser mayor o igual a 1900")
    @Max(value = 2026, message = "El Año debe ser menor o igual a 2026")
    private Integer anio;

    @NotBlank(message = "El Vehiculo Debe Tener Una Categoria")
    @Size(max = 20, message = "La Categoria no puede exceder los 20 caracteres")
    private String categoria;

    @NotBlank(message = "El Vehiculo Debe Tener Una Capacidad De Asientos")
    @Size(max = 2, message = "La Capacidad de pasajeros no puede exceder los 2 caracteres")
    private String capacidadPasajeros;

    @NotBlank(message = "El Vehiculo Debe Tener Un Color")
    @Size(max = 20, message = "El Color no puede exceder los 20 caracteres")
    private String color;

    @NotNull(message = "El Vehiculo Debe Tener Un Vendedor")
    private Long vendedorId;

    private Double tarifaDiaria;
    private String ubicacion;
    private boolean activo;
}
