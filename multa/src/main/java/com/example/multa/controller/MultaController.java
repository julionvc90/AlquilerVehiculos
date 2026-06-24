package com.example.multa.controller;

import com.example.multa.dto.MultaRequestDTO;
import com.example.multa.dto.MultaResponseDTO;
import com.example.multa.dto.RespuestaExitosa;
import com.example.multa.service.MultaService;
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
@RequestMapping("/api/multa")
@Tag(name = "Multas", description = "Gestión de multas por devolución tardía o daños")
public class MultaController {

    private static final Logger logger = LoggerFactory.getLogger(MultaController.class);

    private final MultaService multaService;

    @PostMapping
    @Operation(summary = "Crear nueva multa")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Multa creada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<MultaResponseDTO>> crearMulta(
            @Valid @RequestBody MultaRequestDTO dto) {
        logger.info("POST /api/multa - Crear nueva multa para reserva ID: {}", dto.getIdReserva());
        MultaResponseDTO creado = multaService.crearMulta(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Multa creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar multa por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Multa encontrada"),
                   @ApiResponse(responseCode = "404", description = "Multa no encontrada")})
    public MultaResponseDTO obtenerMulta(
            @PathVariable Long id) {
        logger.info("GET /api/multa/{} - Buscar multa por ID", id);
        return multaService.obtenerPorId(id);
    }

    @GetMapping
    @Operation(summary = "Listar todas las multas")
    @ApiResponse(responseCode = "200", description = "Lista de multas obtenida")
    public List<MultaResponseDTO> listarMulta() {
        logger.info("GET /api/multa - Listar todas las multas");
        return multaService.listarTodas();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar multa")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Multa actualizada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Multa no encontrada")})
    public ResponseEntity<RespuestaExitosa<MultaResponseDTO>> actualizarMulta(
            @PathVariable Long id,
            @Valid @RequestBody MultaRequestDTO dto) {
        logger.info("PUT /api/multa/{} - Actualizar multa", id);
        MultaResponseDTO actualizado = multaService.actualizarMulta(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Multa actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar multa")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Multa eliminada correctamente"),
                   @ApiResponse(responseCode = "404", description = "Multa no encontrada")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminarMulta(
            @PathVariable Long id) {
        logger.info("DELETE /api/multa/{} - Eliminar multa", id);
        multaService.eliminarMulta(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Multa con ID " + id + " eliminada correctamente", null));
    }

}
