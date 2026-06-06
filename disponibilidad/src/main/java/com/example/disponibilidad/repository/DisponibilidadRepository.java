package com.example.disponibilidad.repository;

import com.example.disponibilidad.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    List<Disponibilidad> findByVehiculoIdAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
            Long vehiculoId,
            LocalDate inicio,
            LocalDate fin
    );
}