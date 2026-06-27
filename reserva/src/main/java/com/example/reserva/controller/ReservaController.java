package com.example.reserva.controller;

import com.example.reserva.dto.ReservaRequestDTO;
import com.example.reserva.dto.ReservaResponseDTO;
import com.example.reserva.dto.RespuestaExitosa;
import com.example.reserva.exception.ErrorResponse;
import com.example.reserva.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reserva")
@Tag(name = "Reservas", description = "Gestión de reservas de vehículos con validación cruzada")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService reservaService;

    @PostMapping
    @Operation(summary = "Crear nueva reserva", description = "Valida cliente, vehículo y disponibilidad")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
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
    @Operation(summary = "Buscar reserva por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Reserva encontrada"),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ReservaResponseDTO obtenerReserva(
            @PathVariable Long id) {
        logger.info("GET /api/reserva/{} - Buscar reserva por ID", id);
        return reservaService.obtenerPorId(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas las reservas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida")
    public List<ReservaResponseDTO> listarReserva() {
        logger.info("GET /api/reserva - Listar todas las reservas");
        return reservaService.listarTodas();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<ReservaResponseDTO>> actualizarReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDTO dto) {
        logger.info("PUT /api/reserva/{} - Actualizar reserva", id);
        ReservaResponseDTO actualizado = reservaService.actualizarReserva(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Reserva actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Reserva eliminada correctamente"),
                   @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<Void>> eliminarReserva(
            @PathVariable Long id) {
        logger.info("DELETE /api/reserva/{} - Eliminar reserva", id);
        reservaService.eliminarReserva(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Reserva con ID " + id + " eliminada correctamente", null));
    }
}
