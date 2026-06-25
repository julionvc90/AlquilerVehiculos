package com.example.disponibilidad.controller;

import com.example.disponibilidad.dto.DisponibilidadRequestDTO;
import com.example.disponibilidad.dto.DisponibilidadResponseDTO;
import com.example.disponibilidad.dto.RespuestaExitosa;
import com.example.disponibilidad.service.DisponibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@Tag(name = "Disponibilidad", description = "Gestión de disponibilidad de vehículos por fechas")
public class DisponibilidadController {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadController.class);

    private final DisponibilidadService service;

    public DisponibilidadController(DisponibilidadService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar disponibilidades")
    @ApiResponse(responseCode = "200", description = "Lista de disponibilidades obtenida")
    public ResponseEntity<List<DisponibilidadResponseDTO>> listar() {
        logger.info("GET /api/disponibilidad - Listar todas las disponibilidades");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar disponibilidad por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
                   @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")})
    public ResponseEntity<DisponibilidadResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/disponibilidad/{} - Buscar disponibilidad por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva disponibilidad")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Disponibilidad creada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<DisponibilidadResponseDTO>> guardar(@Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("POST /api/disponibilidad - Crear nueva disponibilidad para vehiculo ID: {}", dto.getVehiculoId());
        DisponibilidadResponseDTO creado = service.guardar(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Disponibilidad creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/validar")
    @Operation(summary = "Validar disponibilidad por fechas")
    @ApiResponse(responseCode = "200", description = "Resultado de la validación")
    public ResponseEntity<Boolean> validarDisponibilidad(
            @Parameter(description = "ID del vehículo a validar") @RequestParam Long vehiculoId,
            @Parameter(description = "Fecha de inicio del período") @RequestParam LocalDate inicio,
            @Parameter(description = "Fecha de fin del período") @RequestParam LocalDate fin) {
        logger.info("GET /api/disponibilidad/validar - Validar vehiculo ID: {} desde {} hasta {}", vehiculoId, inicio, fin);
        return ResponseEntity.ok(service.validarDisponibilidad(vehiculoId, inicio, fin));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar disponibilidad")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")})
    public ResponseEntity<RespuestaExitosa<DisponibilidadResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("PUT /api/disponibilidad/{} - Actualizar disponibilidad", id);
        DisponibilidadResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Disponibilidad actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar disponibilidad")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Disponibilidad eliminada correctamente"),
                   @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/disponibilidad/{} - Eliminar disponibilidad", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Disponibilidad con ID " + id + " eliminada correctamente", null));
    }
}
