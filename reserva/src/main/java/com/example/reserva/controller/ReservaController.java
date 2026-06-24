package com.example.reserva.controller;

import com.example.reserva.dto.ReservaRequestDTO;
import com.example.reserva.dto.ReservaResponseDTO;
import com.example.reserva.dto.RespuestaExitosa;
import org.springframework.http.ResponseEntity;
import com.example.reserva.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<RespuestaExitosa<ReservaResponseDTO>> crearReserva(
            @Valid @RequestBody ReservaRequestDTO dto) {
        logger.info("POST /api/reserva - Crear nueva reserva para cliente ID: {}, vehiculo ID: {}",
                dto.getIdCliente(), dto.getIdVehiculo());
        ReservaResponseDTO creado = reservaService.crearReserva(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Reserva creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ReservaResponseDTO obtenerReserva(
            @PathVariable Long id) {
        logger.info("GET /api/reserva/{} - Buscar reserva por ID", id);
        return reservaService.obtenerPorId(id);
    }

    @GetMapping
    public List<ReservaResponseDTO> listarReserva() {
        logger.info("GET /api/reserva - Listar todas las reservas");
        return reservaService.listarTodas();
    }

    @PutMapping("/{id}")
    public ReservaResponseDTO actualizarReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDTO dto) {
        logger.info("PUT /api/reserva/{} - Actualizar reserva", id);
        ReservaResponseDTO actualizado = reservaService.actualizarReserva(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Reserva actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExitosa<Void>> eliminarReserva(
            @PathVariable Long id) {
        logger.info("DELETE /api/reserva/{} - Eliminar reserva", id);
        reservaService.eliminarReserva(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Reserva con ID " + id + " eliminada correctamente", null));
    }
}
