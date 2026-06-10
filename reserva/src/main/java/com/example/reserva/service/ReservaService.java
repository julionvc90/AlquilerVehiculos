package com.example.reserva.service;

import com.example.reserva.dto.ReservaRequestDTO;
import com.example.reserva.dto.ReservaResponseDTO;
import com.example.reserva.exception.ReservaNotFoundException;
import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository repository;

    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        logger.info("Creando reserva para cliente ID: {}, vehiculo ID: {}",
                dto.getIdCliente(), dto.getIdVehiculo());

        Reserva reserva = Reserva.builder()
                .idReserva(dto.getIdReserva())
                .idCliente(dto.getIdCliente())
                .idVehiculo(dto.getIdVehiculo())
                .fechaReserva(dto.getFechaReserva())
                .fechaInicio(dto.getFechaInicio())
                .fechaTermino(dto.getFechaTermino())
                .totalDias(dto.getTotalDias())
                .valorDia(dto.getValorDia())
                .totalReserva(dto.getTotalReserva())
                .estadoReserva(dto.getEstadoReserva())
                .observacionesReserva(dto.getObservacionesReserva())
                .build();

        Reserva guardado = repository.save(reserva);
        logger.info("Reserva creada exitosamente con ID: {}", guardado.getIdReserva());
        return convertirDTO(guardado);
    }

    public ReservaResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando reserva con ID: {}", id);
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reserva no encontrada con ID: {}", id);
                    return new ReservaNotFoundException("Reserva no encontrada con ID: " + id);
                });
        return convertirDTO(reserva);
    }

    public List<ReservaResponseDTO> listarTodas() {
        logger.info("Listando todas las reservas");
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public ReservaResponseDTO actualizarReserva(Long id, ReservaRequestDTO dto) {
        logger.info("Actualizando reserva con ID: {}", id);

        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reserva no encontrada con ID: {}", id);
                    return new ReservaNotFoundException("Reserva no encontrada con ID: " + id);
                });

        reserva.setIdReserva(dto.getIdReserva());
        reserva.setIdVehiculo(dto.getIdVehiculo());
        reserva.setIdCliente(dto.getIdCliente());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaTermino(dto.getFechaTermino());
        reserva.setTotalDias(dto.getTotalDias());
        reserva.setValorDia(dto.getValorDia());
        reserva.setTotalReserva(dto.getTotalReserva());
        reserva.setEstadoReserva(dto.getEstadoReserva());
        reserva.setObservacionesReserva(dto.getObservacionesReserva());

        Reserva actualizado = repository.save(reserva);
        logger.info("Reserva con ID {} actualizada exitosamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminarReserva(Long id) {
        logger.info("Eliminando reserva con ID: {}", id);
        Reserva reserva = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reserva no encontrada con ID: {}", id);
                    return new ReservaNotFoundException("Reserva no encontrada con ID: " + id);
                });
        repository.delete(reserva);
        logger.info("Reserva con ID {} eliminada exitosamente", id);
    }

    private ReservaResponseDTO convertirDTO(Reserva reserva) {
        return ReservaResponseDTO.builder()
                .idReserva(reserva.getIdReserva())
                .idCliente(reserva.getIdCliente())
                .idVehiculo(reserva.getIdVehiculo())
                .fechaReserva(reserva.getFechaReserva())
                .fechaInicio(reserva.getFechaInicio())
                .fechaTermino(reserva.getFechaTermino())
                .totalDias(reserva.getTotalDias())
                .valorDia(reserva.getValorDia())
                .totalReserva(reserva.getTotalReserva())
                .estadoReserva(reserva.getEstadoReserva())
                .observacionesReserva(reserva.getObservacionesReserva())
                .build();
    }
}
