package com.example.vehiculo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoResponseDTO {

    @Schema(description = "ID único del vehículo", example = "1")
    private Long id;
    @Schema(description = "Patente del vehículo", example = "AB1234")
    private String patente;
    @Schema(description = "Marca del vehículo", example = "Toyota")
    private String marca;
    @Schema(description = "Modelo del vehículo", example = "Corolla")
    private String modelo;
    @Schema(description = "Año de fabricación", example = "2024")
    private Integer anio;
    @Schema(description = "Categoría del vehículo", example = "Sedan")
    private String categoria;
    @Schema(description = "Capacidad de pasajeros", example = "5")
    private String capacidadPasajeros;
    @Schema(description = "Color del vehículo", example = "Rojo")
    private String color;
    @Schema(description = "ID del vendedor asociado", example = "100001")
    private Long vendedorId;
    @Schema(description = "Tarifa diaria en pesos", example = "25000.0")
    private Double tarifaDiaria;
    @Schema(description = "Ubicación del vehículo", example = "Santiago")
    private String ubicacion;
    @Schema(description = "Indica si el vehículo está activo", example = "true")
    private boolean activo;

}
