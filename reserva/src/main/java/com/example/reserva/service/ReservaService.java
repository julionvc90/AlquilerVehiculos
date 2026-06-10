package com.example.reserva.service;

import com.example.reserva.dto.ReservaRequestDTO;
import com.example.reserva.dto.ReservaResponseDTO;
import com.example.reserva.dto.VehiculoResponseDTO;
import com.example.reserva.exception.ReservaNotFoundException;
import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;
import com.example.reserva.webclient.ClienteClient;
import com.example.reserva.webclient.DisponibilidadClient;
import com.example.reserva.webclient.VehiculoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository repository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;
    private final DisponibilidadClient disponibilidadClient;

    public ReservaService(ReservaRepository repository, ClienteClient clienteClient,
                          VehiculoClient vehiculoClient, DisponibilidadClient disponibilidadClient) {
        this.repository = repository;
        this.clienteClient = clienteClient;
        this.vehiculoClient = vehiculoClient;
        this.disponibilidadClient = disponibilidadClient;
    }

    public ReservaResponseDTO crearReserva(ReservaRequestDTO dto) {
        logger.info("Creando reserva para cliente ID: {}, vehiculo ID: {}",
                dto.getIdCliente(), dto.getIdVehiculo());

        // Validar que el cliente existe
        clienteClient.obtenerCliente(dto.getIdCliente());

        // Validar que el vehiculo existe y obtener su tarifa
        VehiculoResponseDTO vehiculo = vehiculoClient.obtenerVehiculo(dto.getIdVehiculo());

        // Validar disponibilidad en el rango de fechas
        Boolean disponible = disponibilidadClient.validarDisponibilidad(
                dto.getIdVehiculo(), dto.getFechaInicio(), dto.getFechaTermino());
        if (!Boolean.TRUE.equals(disponible)) {
            logger.warn("Vehiculo ID {} no disponible para el periodo solicitado", dto.getIdVehiculo());
            throw new RuntimeException("Vehiculo no disponible para el periodo solicitado");
        }

        // Validar que la fecha de termino no sea anterior a la de inicio
        if (dto.getFechaTermino().isBefore(dto.getFechaInicio())) {
            logger.warn("Fecha de termino anterior a fecha de inicio");
            throw new IllegalArgumentException("La fecha de termino no puede ser anterior a la fecha de inicio");
        }

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

        // Validar cliente si cambio
        if (!dto.getIdCliente().equals(reserva.getIdCliente())) {
            clienteClient.obtenerCliente(dto.getIdCliente());
        }
        // Validar vehiculo si cambio
        if (!dto.getIdVehiculo().equals(reserva.getIdVehiculo())) {
            vehiculoClient.obtenerVehiculo(dto.getIdVehiculo());
        }

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
