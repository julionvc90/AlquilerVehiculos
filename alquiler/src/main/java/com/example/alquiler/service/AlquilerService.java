package com.example.alquiler.service;

import com.example.alquiler.dto.AlquilerRequestDTO;
import com.example.alquiler.dto.AlquilerResponseDTO;
import com.example.alquiler.dto.PagoResponseDTO;
import com.example.alquiler.dto.ReservaResponseDTO;
import com.example.alquiler.dto.VehiculoResponseDTO;
import com.example.alquiler.exception.ResourceNotFoundException;
import com.example.alquiler.model.Alquiler;
import com.example.alquiler.model.EstadoAlquiler;
import com.example.alquiler.repository.AlquilerRepository;
import com.example.alquiler.webclient.ClienteClient;
import com.example.alquiler.webclient.DisponibilidadClient;
import com.example.alquiler.webclient.PagoClient;
import com.example.alquiler.webclient.ReservaClient;
import com.example.alquiler.webclient.VehiculoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlquilerService {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerService.class);

    private final AlquilerRepository alquilerRepository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;
    private final DisponibilidadClient disponibilidadClient;
    private final ReservaClient reservaClient;
    private final PagoClient pagoClient;

    public AlquilerService(AlquilerRepository alquilerRepository, ClienteClient clienteClient,
                           VehiculoClient vehiculoClient, DisponibilidadClient disponibilidadClient,
                           ReservaClient reservaClient, PagoClient pagoClient) {
        this.alquilerRepository = alquilerRepository;
        this.clienteClient = clienteClient;
        this.vehiculoClient = vehiculoClient;
        this.disponibilidadClient = disponibilidadClient;
        this.reservaClient = reservaClient;
        this.pagoClient = pagoClient;
    }

    public List<AlquilerResponseDTO> listar() {
        logger.info("Listando todos los alquileres");
        return alquilerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public AlquilerResponseDTO buscarPorId(Long id) {
        logger.info("Buscando alquiler con ID: {}", id);
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alquiler no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Alquiler", id);
                });
        return toResponseDTO(alquiler);
    }

    public AlquilerResponseDTO guardar(AlquilerRequestDTO dto) {
        logger.info("Creando alquiler para reserva ID: {}, cliente ID: {}, vehiculo ID: {}",
                dto.getReservaId(), dto.getClienteId(), dto.getVehiculoId());

        // Validar que la reserva existe y esta confirmada
        ReservaResponseDTO reserva = reservaClient.obtenerReserva(dto.getReservaId());
        if (!"Confirmada".equalsIgnoreCase(reserva.getEstadoReserva())) {
            logger.warn("Reserva ID {} no esta confirmada. Estado actual: {}", dto.getReservaId(), reserva.getEstadoReserva());
            throw new IllegalArgumentException("La reserva debe estar confirmada para crear un alquiler");
        }

        // Validar que existe al menos un pago asociado a la reserva
        List<PagoResponseDTO> pagos = pagoClient.buscarPagosPorReserva(dto.getReservaId());
        boolean pagoRealizado = pagos.stream()
                .anyMatch(p -> "Ingresada".equalsIgnoreCase(p.getEstadoPago())
                        || "Pagada".equalsIgnoreCase(p.getEstadoPago()));
        if (!pagoRealizado) {
            logger.warn("Reserva ID {} no tiene pagos registrados", dto.getReservaId());
            throw new IllegalArgumentException("La reserva debe tener un pago registrado para crear un alquiler");
        }

        clienteClient.obtenerCliente(dto.getClienteId());

        VehiculoResponseDTO vehiculo = vehiculoClient.obtenerVehiculo(dto.getVehiculoId());

        Boolean disponible = disponibilidadClient.validarDisponibilidad(
                dto.getVehiculoId(), dto.getFechaInicio(), dto.getFechaFin());

        if (!Boolean.TRUE.equals(disponible)) {
            logger.warn("Vehiculo ID {} no disponible para el periodo solicitado", dto.getVehiculoId());
            throw new RuntimeException("Vehículo no disponible para el periodo solicitado");
        }

        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            logger.warn("Fecha de fin anterior a fecha de inicio");
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        long dias = ChronoUnit.DAYS.between(dto.getFechaInicio(), dto.getFechaFin());

        Alquiler alquiler = toEntity(dto);
        alquiler.setDias((int) dias);
        alquiler.setTarifaDiaria(vehiculo.getTarifaDiaria());
        alquiler.setMontoTotal((int) dias * vehiculo.getTarifaDiaria());
        alquiler.setEstado(EstadoAlquiler.Reservado);

        Alquiler guardado = alquilerRepository.save(alquiler);
        logger.info("Alquiler creado exitosamente con ID: {}, monto total: {}",
                guardado.getId(), guardado.getMontoTotal());
        return toResponseDTO(guardado);
    }

    public AlquilerResponseDTO actualizar(Long id, AlquilerRequestDTO dto) {
        logger.info("Actualizando alquiler con ID: {}", id);

        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alquiler no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Alquiler", id);
                });

        clienteClient.obtenerCliente(dto.getClienteId());
        vehiculoClient.obtenerVehiculo(dto.getVehiculoId());
        reservaClient.obtenerReserva(dto.getReservaId());

        alquiler.setClienteId(dto.getClienteId());
        alquiler.setVehiculoId(dto.getVehiculoId());
        alquiler.setFechaInicio(dto.getFechaInicio());
        alquiler.setFechaFin(dto.getFechaFin());

        long dias = ChronoUnit.DAYS.between(dto.getFechaInicio(), dto.getFechaFin());
        alquiler.setDias((int) dias);
        alquiler.setMontoTotal((int) dias * alquiler.getTarifaDiaria());

        Alquiler actualizado = alquilerRepository.save(alquiler);
        logger.info("Alquiler con ID {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public AlquilerResponseDTO iniciarAlquiler(Long id) {
        logger.info("Iniciando alquiler con ID: {}", id);
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alquiler no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Alquiler", id);
                });

        if (alquiler.getEstado() != EstadoAlquiler.Reservado) {
            logger.warn("Alquiler ID {} no se puede iniciar porque su estado es {}", id, alquiler.getEstado());
            throw new IllegalArgumentException("Solo se pueden iniciar alquileres en estado Reservado");
        }

        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        Alquiler actualizado = alquilerRepository.save(alquiler);
        logger.info("Alquiler con ID {} iniciado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public AlquilerResponseDTO finalizarAlquiler(Long id) {
        logger.info("Finalizando alquiler con ID: {}", id);
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alquiler no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Alquiler", id);
                });

        if (alquiler.getEstado() != EstadoAlquiler.ACTIVO) {
            logger.warn("Alquiler ID {} no se puede finalizar porque su estado es {}", id, alquiler.getEstado());
            throw new IllegalArgumentException("Solo se pueden finalizar alquileres en estado ACTIVO");
        }

        alquiler.setEstado(EstadoAlquiler.FINALIZADO);
        Alquiler actualizado = alquilerRepository.save(alquiler);
        logger.info("Alquiler con ID {} finalizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public boolean existePorId(Long id) {
        return alquilerRepository.existsById(id);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando alquiler con ID: {}", id);
        Alquiler alquiler = alquilerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Alquiler no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Alquiler", id);
                });
        alquilerRepository.delete(alquiler);
        logger.info("Alquiler con ID {} eliminado exitosamente", id);
    }

    private AlquilerResponseDTO toResponseDTO(Alquiler alquiler) {
        return AlquilerResponseDTO.builder()
                .id(alquiler.getId())
                .clienteId(alquiler.getClienteId())
                .vehiculoId(alquiler.getVehiculoId())
                .reservaId(alquiler.getReservaId())
                .fechaInicio(alquiler.getFechaInicio())
                .fechaFin(alquiler.getFechaFin())
                .dias(alquiler.getDias())
                .tarifaDiaria(alquiler.getTarifaDiaria())
                .montoTotal(alquiler.getMontoTotal())
                .estado(alquiler.getEstado())
                .build();
    }

    private Alquiler toEntity(AlquilerRequestDTO dto) {
        Alquiler alquiler = new Alquiler();
        alquiler.setClienteId(dto.getClienteId());
        alquiler.setVehiculoId(dto.getVehiculoId());
        alquiler.setReservaId(dto.getReservaId());
        alquiler.setFechaInicio(dto.getFechaInicio());
        alquiler.setFechaFin(dto.getFechaFin());
        return alquiler;
    }
}
