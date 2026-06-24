package com.example.pago.controller;

import com.example.pago.dto.PagoRequestDTO;
import com.example.pago.dto.PagoResponseDTO;
import com.example.pago.dto.RespuestaExitosa;
import com.example.pago.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/pago")
@Tag(name = "Pagos", description = "Operaciones de gestión de pagos asociados a reservas")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    private final PagoService pagoService;

    @PostMapping
    @Operation(summary = "Crear nuevo pago")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<PagoResponseDTO>> crear(
            @Valid @RequestBody PagoRequestDTO dto) {
        logger.info("POST /api/pago - Crear nuevo pago para reserva ID: {}", dto.getIdReserva());
        PagoResponseDTO creado = pagoService.crearPago(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Pago creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pago por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pago encontrado"),
                   @ApiResponse(responseCode = "404", description = "Pago no encontrado")})
    public PagoResponseDTO obtener(
            @PathVariable Long id) {
        logger.info("GET /api/pago/{} - Buscar pago por ID", id);
        return pagoService.obtenerPorId(id);
    }

    @GetMapping("/reserva/{reservaId}")
    @Operation(summary = "Buscar pagos por reserva")
    @ApiResponse(responseCode = "200", description = "Lista de pagos de la reserva")
    public List<PagoResponseDTO> buscarPorReserva(@PathVariable Long reservaId) {
        logger.info("GET /api/pago/reserva/{} - Buscar pagos por reserva", reservaId);
        return pagoService.buscarPorReservaId(reservaId);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida")
    public List<PagoResponseDTO> listar() {
        logger.info("GET /api/pago - Listar todos los pagos");
        return pagoService.listarTodas();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pago")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Pago no encontrado")})
    public ResponseEntity<RespuestaExitosa<PagoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO dto) {
        logger.info("PUT /api/pago/{} - Actualizar pago", id);
        PagoResponseDTO actualizado = pagoService.actualizarPago(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Pago actualizado correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pago eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Pago no encontrado")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(
            @PathVariable Long id) {
        logger.info("DELETE /api/pago/{} - Eliminar pago", id);
        pagoService.eliminarPago(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Pago con ID " + id + " eliminado correctamente", null));
    }
}
