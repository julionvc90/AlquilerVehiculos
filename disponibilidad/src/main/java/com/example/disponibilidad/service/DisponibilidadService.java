package com.example.disponibilidad.service;

import com.example.disponibilidad.dto.DisponibilidadRequestDTO;
import com.example.disponibilidad.dto.DisponibilidadResponseDTO;
import com.example.disponibilidad.exception.ResourceNotFoundException;
import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.repository.DisponibilidadRepository;
import com.example.disponibilidad.webclient.VehiculoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadService.class);

    @Autowired
    private DisponibilidadRepository repository;

    @Autowired
    private VehiculoClient vehiculoClient;

    public List<DisponibilidadResponseDTO> listar() {
        logger.info("Listando todas las disponibilidades");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DisponibilidadResponseDTO buscarPorId(Long id) {
        logger.info("Buscando disponibilidad con ID: {}", id);
        Disponibilidad disponibilidad = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Disponibilidad no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Disponibilidad", id);
                });
        return toResponseDTO(disponibilidad);
    }

    public DisponibilidadResponseDTO guardar(DisponibilidadRequestDTO dto) {
        logger.info("Guardando disponibilidad para vehiculo ID: {}", dto.getVehiculoId());

        vehiculoClient.obtenerVehiculo(dto.getVehiculoId());

        Disponibilidad disponibilidad = toEntity(dto);
        Disponibilidad guardado = repository.save(disponibilidad);
        logger.info("Disponibilidad guardada exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public DisponibilidadResponseDTO actualizar(Long id, DisponibilidadRequestDTO dto) {
        logger.info("Actualizando disponibilidad con ID: {}", id);

        Disponibilidad disponibilidad = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Disponibilidad no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Disponibilidad", id);
                });

        vehiculoClient.obtenerVehiculo(dto.getVehiculoId());

        disponibilidad.setVehiculoId(dto.getVehiculoId());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());
        disponibilidad.setDisponible(dto.getDisponible());

        Disponibilidad actualizado = repository.save(disponibilidad);
        logger.info("Disponibilidad con ID {} actualizada exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public Boolean validarDisponibilidad(Long vehiculoId, LocalDate inicio, LocalDate fin) {
        logger.info("Validando disponibilidad del vehiculo ID: {} desde {} hasta {}", vehiculoId, inicio, fin);

        boolean disponible = repository
                .findByVehiculoIdAndDisponibleFalseAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
                        vehiculoId, inicio, fin)
                .isEmpty();

        logger.info("Vehiculo ID {} {} disponible para el periodo solicitado",
                vehiculoId, disponible ? "SI" : "NO");
        return disponible;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando disponibilidad con ID: {}", id);
        Disponibilidad disponibilidad = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Disponibilidad no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Disponibilidad", id);
                });
        repository.delete(disponibilidad);
        logger.info("Disponibilidad con ID {} eliminada exitosamente", id);
    }

    private DisponibilidadResponseDTO toResponseDTO(Disponibilidad disponibilidad) {
        DisponibilidadResponseDTO dto = new DisponibilidadResponseDTO();
        dto.setId(disponibilidad.getId());
        dto.setVehiculoId(disponibilidad.getVehiculoId());
        dto.setFechaInicio(disponibilidad.getFechaInicio());
        dto.setFechaFin(disponibilidad.getFechaFin());
        dto.setDisponible(disponibilidad.getDisponible());
        return dto;
    }

    private Disponibilidad toEntity(DisponibilidadRequestDTO dto) {
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setVehiculoId(dto.getVehiculoId());
        disponibilidad.setFechaInicio(dto.getFechaInicio());
        disponibilidad.setFechaFin(dto.getFechaFin());
        disponibilidad.setDisponible(dto.getDisponible());
        return disponibilidad;
    }
}
