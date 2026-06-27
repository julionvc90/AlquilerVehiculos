package com.example.vehiculo.controller;

import com.example.vehiculo.dto.RespuestaExitosa;
import com.example.vehiculo.exception.ErrorResponse;
import com.example.vehiculo.dto.VehiculoRequestDTO;
import com.example.vehiculo.dto.VehiculoResponseDTO;
import com.example.vehiculo.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehiculos", description = "Operaciones CRUD de gestión de vehículos")
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida")
    public ResponseEntity<List<VehiculoResponseDTO>> listar() {
        logger.info("GET /api/vehiculos - Listar todos los vehiculos");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehículo por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
                   @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<VehiculoResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{} - Buscar vehiculo por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo vehículo")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<VehiculoResponseDTO>> guardar(@Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("POST /api/vehiculos - Crear nuevo vehiculo con patente: {}", dto.getPatente());
        VehiculoResponseDTO creado = service.guardar(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Vehículo creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                   @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<VehiculoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("PUT /api/vehiculos/{} - Actualizar vehiculo", id);
        VehiculoResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Vehículo actualizado correctamente", actualizado));
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar existencia de vehículo")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{}/existe - Verificar existencia", id);
        return ResponseEntity.ok(service.existePorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehículo eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Vehículo no encontrado",
                                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/vehiculos/{} - Eliminar vehiculo", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Vehículo con ID " + id + " eliminado correctamente", null));
    }
}
