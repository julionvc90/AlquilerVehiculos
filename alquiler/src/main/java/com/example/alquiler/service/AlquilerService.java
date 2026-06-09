package com.example.alquiler.service;

import com.example.alquiler.dto.AlquilerRequestDTO;
import com.example.alquiler.dto.AlquilerResponseDTO;
import com.example.alquiler.dto.VehiculoResponseDTO;
import com.example.alquiler.exception.ResourceNotFoundException;
import com.example.alquiler.model.Alquiler;
import com.example.alquiler.model.EstadoAlquiler;
import com.example.alquiler.repository.AlquilerRepository;
import com.example.alquiler.webclient.ClienteClient;
import com.example.alquiler.webclient.DisponibilidadClient;
import com.example.alquiler.webclient.VehiculoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlquilerService {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerService.class);

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private DisponibilidadClient disponibilidadClient;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private VehiculoClient vehiculoClient;

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
        logger.info("Creando alquiler para cliente ID: {}, vehiculo ID: {}",
                dto.getClienteId(), dto.getVehiculoId());

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
        alquiler.setFechaInicio(dto.getFechaInicio());
        alquiler.setFechaFin(dto.getFechaFin());
        return alquiler;
    }
}
