package com.example.inspeccion.controller;

import com.example.inspeccion.dto.InspeccionRequestDTO;
import com.example.inspeccion.dto.InspeccionResponseDTO;
import com.example.inspeccion.dto.RespuestaExitosa;
import com.example.inspeccion.exception.ErrorResponse;
import com.example.inspeccion.service.InspeccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inspecciones")
@Tag(name = "Inspecciones", description = "Gestión de inspecciones de entrega y devolución")
public class InspeccionController {

    private static final Logger logger = LoggerFactory.getLogger(InspeccionController.class);

    private final InspeccionService service;

    public InspeccionController(InspeccionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas las inspecciones")
    @ApiResponse(responseCode = "200", description = "Lista de inspecciones obtenida")
    public ResponseEntity<List<InspeccionResponseDTO>> listar() {
        logger.info("GET /api/inspecciones - Listar todas las inspecciones");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inspección por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Inspección encontrada"),
                   @ApiResponse(responseCode = "404", description = "Inspección no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<InspeccionResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/inspecciones/{} - Buscar inspeccion por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/alquiler/{alquilerId}")
    @Operation(summary = "Buscar inspecciones por alquiler")
    @ApiResponse(responseCode = "200", description = "Lista de inspecciones del alquiler")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorAlquiler(@PathVariable Long alquilerId) {
        logger.info("GET /api/inspecciones/alquiler/{} - Buscar por alquiler", alquilerId);
        return ResponseEntity.ok(service.buscarPorAlquilerId(alquilerId));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    @Operation(summary = "Buscar inspecciones por vehículo")
    @ApiResponse(responseCode = "200", description = "Lista de inspecciones del vehículo")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorVehiculo(@PathVariable Long vehiculoId) {
        logger.info("GET /api/inspecciones/vehiculo/{} - Buscar por vehiculo", vehiculoId);
        return ResponseEntity.ok(service.buscarPorVehiculoId(vehiculoId));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar inspecciones por tipo")
    @ApiResponse(responseCode = "200", description = "Lista de inspecciones filtrada por tipo")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorTipo(
            @Parameter(description = "Tipo de inspección (Entrega, Devolucion)") @PathVariable String tipo) {
        logger.info("GET /api/inspecciones/tipo/{} - Buscar por tipo de inspeccion", tipo);
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping
    @Operation(summary = "Crear nueva inspección")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Inspección creada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<InspeccionResponseDTO>> crear(@Valid @RequestBody InspeccionRequestDTO dto) {
        logger.info("POST /api/inspecciones - Crear nueva inspeccion");
        InspeccionResponseDTO creado = service.crear(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Inspección creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar inspección")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Inspección actualizada correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                   @ApiResponse(responseCode = "404", description = "Inspección no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<InspeccionResponseDTO>> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody InspeccionRequestDTO dto) {
        logger.info("PUT /api/inspecciones/{} - Actualizar inspeccion", id);
        InspeccionResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Inspección actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inspección")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Inspección eliminada correctamente"),
                   @ApiResponse(responseCode = "404", description = "Inspección no encontrada",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/inspecciones/{} - Eliminar inspeccion", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Inspección con ID " + id + " eliminada correctamente", null));
    }
}
