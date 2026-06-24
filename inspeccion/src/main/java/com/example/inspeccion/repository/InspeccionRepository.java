package com.example.inspeccion.repository;

import com.example.inspeccion.model.Inspeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspeccionRepository extends JpaRepository<Inspeccion, Long> {

    List<Inspeccion> findByAlquilerId(Long alquilerId);

    List<Inspeccion> findByVehiculoId(Long vehiculoId);

    List<Inspeccion> findByTipoInspeccion(String tipoInspeccion);
}
