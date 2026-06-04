package com.example.alquiler.model;

import com.example.alquiler.enums.EstadoAlquiler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
// JULIO NAVARRO
@Entity
@Data
@Table(name = "Alquiler")
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El Alquiler Debe Tener Un Estado")
    @Column(nullable = false)
    private Long clienteId;

    @NotBlank(message = "El Alquiler Debe Tener Un Estado")
    @Column(nullable = false)
    private Long vehiculoId;


    @Column(nullable = false)
    @NotBlank(message = "El Alquiler Debe Tener Una Fecha De Inicio")
    private LocalDate fechaInicio;

    @NotBlank
    private LocalDate fechaFin;

    @NotBlank(message = "El Alquiler Debe Tener Una Cantidad De Dias")
    @Column(nullable = false)
    private Integer dias;

    @NotBlank(message = "El Alquiler Debe Tener Una Tarifa Diaria")
    @Column(nullable = false)
    private Double tarifaDiaria;

    @NotBlank(message = "El Alquiler Debe Tener Un Monto Total")
    @Column(nullable = false)
    private Double montoTotal;

    @NotBlank(message = "El Alquiler Debe Tener Un Estado")
    @Enumerated(EnumType.STRING)
    private EstadoAlquiler estado;
}