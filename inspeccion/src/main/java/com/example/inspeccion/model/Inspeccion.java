package com.example.inspeccion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "inspeccion")
@NoArgsConstructor
@AllArgsConstructor
public class Inspeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alquiler_id")
    private Long alquilerId;

    @Column(name = "vehiculo_id")
    private Long vehiculoId;

    @Column(name = "fecha_inspeccion", nullable = false)
    private LocalDateTime fechaInspeccion;

    @Column(name = "tipo_inspeccion", nullable = false, length = 20)
    private String tipoInspeccion;

    @Column(nullable = false, length = 20)
    private String resultado;

    @Column(length = 500)
    private String observaciones;

    @Column(length = 100)
    private String inspector;

    @Column(nullable = false)
    private boolean activo = true;
}
