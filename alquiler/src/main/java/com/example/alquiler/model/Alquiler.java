package com.example.alquiler.model;

import com.example.alquiler.model.EstadoAlquiler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Alquiler")
@NoArgsConstructor
@AllArgsConstructor
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private Long vehiculoId;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @Column(nullable = false)
    private Integer dias;

    @Column(nullable = false)
    private Double tarifaDiaria;

    @Column(nullable = false)
    private Double montoTotal;

    @Enumerated(EnumType.STRING)
    private EstadoAlquiler estado;
}