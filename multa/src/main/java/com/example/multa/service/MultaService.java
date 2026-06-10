package com.example.multa.service;

import com.example.multa.dto.MultaRequestDTO;
import com.example.multa.dto.MultaResponseDTO;
import com.example.multa.exception.MultaNotFoundException;
import com.example.multa.model.Multa;
import com.example.multa.repository.MultaRepository;
import com.example.multa.webclient.ReservaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultaService {

    private static final Logger logger = LoggerFactory.getLogger(MultaService.class);

    private final MultaRepository repository;
    private final ReservaClient reservaClient;

    public MultaService(MultaRepository repository, ReservaClient reservaClient) {
        this.repository = repository;
        this.reservaClient = reservaClient;
    }

    public MultaResponseDTO crearMulta(MultaRequestDTO dto) {
        logger.info("Creando multa para reserva ID: {}, vehiculo ID: {}",
                dto.getIdReserva(), dto.getIdVehiculo());

        // Validar que la reserva existe
        reservaClient.obtenerReserva(dto.getIdReserva());

        Multa multa = Multa.builder()
                .idReserva(dto.getIdReserva())
                .idVehiculo(dto.getIdVehiculo())
                .motivoMulta(dto.getMotivoMulta())
                .montoMulta(dto.getMontoMulta())
                .estadoMulta(dto.getEstadoMulta())
                .fechaMulta(LocalDate.now())
                .build();

        Multa guardado = repository.save(multa);
        logger.info("Multa creada exitosamente con ID: {}", guardado.getIdMulta());
        return convertirDTO(guardado);
    }

    public MultaResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando multa con ID: {}", id);
        Multa multa = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Multa no encontrada con ID: {}", id);
                    return new MultaNotFoundException("Multa no encontrada con ID: " + id);
                });
        return convertirDTO(multa);
    }

    public List<MultaResponseDTO> listarTodas() {
        logger.info("Listando todas las multas");
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public MultaResponseDTO actualizarMulta(Long id, MultaRequestDTO dto) {
        logger.info("Actualizando multa con ID: {}", id);

        Multa multa = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Multa no encontrada con ID: {}", id);
                    return new MultaNotFoundException("Multa no encontrada con ID: " + id);
                });

        // Validar reserva si cambio
        if (!dto.getIdReserva().equals(multa.getIdReserva())) {
            reservaClient.obtenerReserva(dto.getIdReserva());
        }

        multa.setIdReserva(dto.getIdReserva());
        multa.setIdVehiculo(dto.getIdVehiculo());
        multa.setMotivoMulta(dto.getMotivoMulta());
        multa.setMontoMulta(dto.getMontoMulta());
        multa.setEstadoMulta(dto.getEstadoMulta());

        Multa actualizado = repository.save(multa);
        logger.info("Multa con ID {} actualizada exitosamente", id);
        return convertirDTO(actualizado);
    }

    public void eliminarMulta(Long id) {
        logger.info("Eliminando multa con ID: {}", id);
        Multa multa = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Multa no encontrada con ID: {}", id);
                    return new MultaNotFoundException("Multa no encontrada con ID: " + id);
                });
        repository.delete(multa);
        logger.info("Multa con ID {} eliminada exitosamente", id);
    }

    private MultaResponseDTO convertirDTO(Multa multa) {
        return MultaResponseDTO.builder()
                .idMulta(multa.getIdMulta())
                .idReserva(multa.getIdReserva())
                .idVehiculo(multa.getIdVehiculo())
                .motivoMulta(multa.getMotivoMulta())
                .montoMulta(multa.getMontoMulta())
                .fechaMulta(multa.getFechaMulta())
                .estadoMulta(multa.getEstadoMulta())
                .build();
    }
}
