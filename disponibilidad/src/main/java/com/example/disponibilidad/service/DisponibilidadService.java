package com.example.disponibilidad.service;

import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.repository.DisponibilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository repository;

    public List<Disponibilidad> listar() {
        return repository.findAll();
    }

    public Disponibilidad buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Disponibilidad no encontrada"));
    }

    public Disponibilidad guardar(Disponibilidad disponibilidad) {
        return repository.save(disponibilidad);
    }

    public Disponibilidad actualizar(Long id, Disponibilidad datos) {

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidad.setVehiculoId(datos.getVehiculoId());
        disponibilidad.setFechaInicio(datos.getFechaInicio());
        disponibilidad.setFechaFin(datos.getFechaFin());
        disponibilidad.setDisponible(datos.getDisponible());

        return repository.save(disponibilidad);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}