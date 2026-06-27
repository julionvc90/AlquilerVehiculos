package com.example.vehiculo.model;

import jakarta.persistence.*;
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

    private Long vendedorId;

    @Column(unique = true, length = 6, nullable = false)
    private String patente;

    @Column(length = 10, nullable = false)
    private String marca;

    @Column(length = 20, nullable = false)
    private String modelo;

    private Integer anio;

    private String categoria;

    @Column(length = 2, nullable = false)
    private String capacidadPasajeros;

    private String color;

    private Double tarifaDiaria;
    private String ubicacion;
    private boolean activo;
}
