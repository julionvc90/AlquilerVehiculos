package com.example.alquiler.service;

import com.example.alquiler.enums.EstadoAlquiler;
import com.example.alquiler.model.*;
import com.example.alquiler.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    public List<Alquiler> listar() {
        return alquilerRepository.findAll();
    }

    public Alquiler buscarPorId(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Alquiler no encontrado"));
    }

    public Alquiler guardar(Alquiler alquiler) {

        if (alquiler.getFechaFin().isBefore(alquiler.getFechaInicio())) {
            throw new RuntimeException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        long dias = ChronoUnit.DAYS.between(
                alquiler.getFechaInicio(),
                alquiler.getFechaFin());

        alquiler.setDias((int) dias);

        alquiler.setMontoTotal(
                alquiler.getDias() * alquiler.getTarifaDiaria());

        alquiler.setEstado(EstadoAlquiler.Reservado);

        return alquilerRepository.save(alquiler);
    }

    public Alquiler actualizar(Long id, Alquiler datos) {

        Alquiler alquiler = buscarPorId(id);

        alquiler.setClienteId(datos.getClienteId());
        alquiler.setVehiculoId(datos.getVehiculoId());
        alquiler.setFechaInicio(datos.getFechaInicio());
        alquiler.setFechaFin(datos.getFechaFin());
        alquiler.setTarifaDiaria(datos.getTarifaDiaria());

        long dias = ChronoUnit.DAYS.between(
                datos.getFechaInicio(),
                datos.getFechaFin());

        alquiler.setDias((int) dias);

        alquiler.setMontoTotal(
                alquiler.getDias() * alquiler.getTarifaDiaria());

        return alquilerRepository.save(alquiler);
    }

    public Alquiler iniciarAlquiler(Long id) {

        Alquiler alquiler = buscarPorId(id);

        alquiler.setEstado(EstadoAlquiler.ACTIVO);

        return alquilerRepository.save(alquiler);
    }

    public Alquiler finalizarAlquiler(Long id) {

        Alquiler alquiler = buscarPorId(id);

        alquiler.setEstado(EstadoAlquiler.FINALIZADO);

        return alquilerRepository.save(alquiler);
    }

    public void eliminar(Long id) {

        Alquiler alquiler = buscarPorId(id);

        alquilerRepository.delete(alquiler);
    }
}