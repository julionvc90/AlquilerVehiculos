package com.example.vehiculo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// JULIO NAVARRO
@Entity
@Data
@Table(name = "Vehiculo")
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El Vehiculo Debe Tener Una Patente")
    @Column(unique = true, length = 13, nullable = false)
    private String patente;

    @NotBlank(message = "El Vehiculo Debe Tener Una Marca")
    @Column(length = 13, nullable = false)
    private String marca;

    @NotBlank(message = "El Vehiculo Debe Tener Un Modelo")
    @Column(length = 13, nullable = false)
    private String modelo;

    private Integer anio;

    @NotBlank(message = "El Vehiculo Debe Tener Una Categoria")
    private String categoria;

    @Column(length = 2, nullable = false)
    private String capacidadPasajeros;

    private String color;

    private Double tarifaDiaria;
    private String ubicacion;
    private boolean activo;
}
