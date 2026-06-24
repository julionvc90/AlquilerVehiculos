package com.example.pago.service;

import com.example.pago.dto.PagoRequestDTO;
import com.example.pago.dto.PagoResponseDTO;
import com.example.pago.exception.PagoNotFoundException;
import com.example.pago.model.Pago;
import com.example.pago.repository.PagoRepository;
import com.example.pago.webclient.ReservaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository repository;
    private final ReservaClient reservaClient;

    public PagoService(PagoRepository repository, ReservaClient reservaClient) {
        this.repository = repository;
        this.reservaClient = reservaClient;
    }

    public PagoResponseDTO crearPago(PagoRequestDTO dto) {
        logger.info("Creando pago para reserva ID: {}, vehiculo ID: {}",
                dto.getIdReserva(), dto.getIdVehiculo());

        // Validar que la reserva existe
        reservaClient.obtenerReserva(dto.getIdReserva());

        Pago pago = Pago.builder()
                .idPago(dto.getIdPago())
                .idReserva(dto.getIdReserva())
                .idVehiculo(dto.getIdVehiculo())
                .metodoPago(dto.getMetodoPago())
                .montoPago(dto.getMontoPago())
                .estadoPago(dto.getEstadoPago())
                .fechaPago(dto.getFechaPago())
                .transaccionPago(dto.getTransaccionPago())
                .build();

        Pago guardado = repository.save(pago);
        logger.info("Pago creado exitosamente con ID: {}", guardado.getIdPago());
        return convertirDTO(guardado);
    }

    public PagoResponseDTO obtenerPorId(Long id) {
        logger.info("Buscando pago con ID: {}", id);
        Pago pago = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pago no encontrado con ID: {}", id);
                    return new PagoNotFoundException("Pago no encontrado con ID: " + id);
                });
        return convertirDTO(pago);
    }

    public List<PagoResponseDTO> listarTodas() {
        logger.info("Listando todos los pagos");
        return repository.findAll()
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO actualizarPago(Long id, PagoRequestDTO dto) {
        logger.info("Actualizando pago con ID: {}", id);

        Pago pago = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pago no encontrado con ID: {}", id);
                    return new PagoNotFoundException("Pago no encontrado con ID: " + id);
                });

        // Validar reserva si cambio
        if (!dto.getIdReserva().equals(pago.getIdReserva())) {
            reservaClient.obtenerReserva(dto.getIdReserva());
        }

        pago.setIdPago(dto.getIdPago());
        pago.setIdReserva(dto.getIdReserva());
        pago.setIdVehiculo(dto.getIdVehiculo());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setMontoPago(dto.getMontoPago());
        pago.setEstadoPago(dto.getEstadoPago());
        pago.setFechaPago(dto.getFechaPago());
        pago.setTransaccionPago(dto.getTransaccionPago());

        Pago actualizado = repository.save(pago);
        logger.info("Pago con ID {} actualizado exitosamente", id);
        return convertirDTO(actualizado);
    }

    public List<PagoResponseDTO> buscarPorReservaId(Long reservaId) {
        logger.info("Buscando pagos de la reserva ID: {}", reservaId);
        return repository.findByIdReserva(reservaId)
                .stream()
                .map(this::convertirDTO)
                .collect(Collectors.toList());
    }

    public void eliminarPago(Long id) {
        logger.info("Eliminando pago con ID: {}", id);
        Pago pago = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pago no encontrado con ID: {}", id);
                    return new PagoNotFoundException("Pago no encontrado con ID: " + id);
                });
        repository.delete(pago);
        logger.info("Pago con ID {} eliminado exitosamente", id);
    }

    private PagoResponseDTO convertirDTO(Pago pago) {
        return PagoResponseDTO.builder()
                .idPago(pago.getIdPago())
                .idReserva(pago.getIdReserva())
                .idVehiculo(pago.getIdVehiculo())
                .metodoPago(pago.getMetodoPago())
                .montoPago(pago.getMontoPago())
                .fechaPago(pago.getFechaPago())
                .estadoPago(pago.getEstadoPago())
                .transaccionPago(pago.getTransaccionPago())
                .build();
    }
}
