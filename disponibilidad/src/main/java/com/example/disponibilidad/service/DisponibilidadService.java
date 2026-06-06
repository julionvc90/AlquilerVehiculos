package com.example.disponibilidad.service;

import com.example.disponibilidad.client.VehiculoClient;
import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.repository.DisponibilidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DisponibilidadService {

    @Autowired
    private DisponibilidadRepository repository;

    @Autowired
    private VehiculoClient vehiculoClient;

    public List<Disponibilidad> listar() {
        return repository.findAll();
    }

    public Disponibilidad buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    }

    public Disponibilidad guardar(Disponibilidad disponibilidad) {

        vehiculoClient.obtenerVehiculo(
                disponibilidad.getVehiculoId());

        return repository.save(disponibilidad);
    }

    public Disponibilidad actualizar(Long id, Disponibilidad datos) {

        Disponibilidad disponibilidad = buscarPorId(id);

        vehiculoClient.obtenerVehiculo(
                datos.getVehiculoId());

        disponibilidad.setVehiculoId(datos.getVehiculoId());
        disponibilidad.setFechaInicio(datos.getFechaInicio());
        disponibilidad.setFechaFin(datos.getFechaFin());
        disponibilidad.setDisponible(datos.getDisponible());

        return repository.save(disponibilidad);
    }

    public Boolean validarDisponibilidad(
            Long vehiculoId,
            LocalDate inicio,
            LocalDate fin) {

        return repository
                .findByVehiculoIdAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
                        vehiculoId,
                        inicio,
                        fin)
                .isEmpty();
    }
    public void eliminar(Long id) {
        Disponibilidad disponibilidad = buscarPorId(id);
        repository.delete(disponibilidad);
    }
}