package com.example.disponibilidad.repository;

import com.example.disponibilidad.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadRepository
        extends JpaRepository<Disponibilidad, Long> {
}