package com.example.alquiler.controller;

import com.example.alquiler.dto.AlquilerRequestDTO;
import com.example.alquiler.dto.AlquilerResponseDTO;
import com.example.alquiler.dto.RespuestaExitosa;
import com.example.alquiler.service.AlquilerService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/alquiler")
@Tag(name = "Alquileres", description = "Orquestación de alquileres: reserva → pago → activo → finalizado")
public class AlquilerController {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerController.class);

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los alquileres")
    @ApiResponse(responseCode = "200", description = "Lista de alquileres obtenida")
    public ResponseEntity<List<AlquilerResponseDTO>> listar() {
        logger.info("GET /api/alquiler - Listar todos los alquileres");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alquiler por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alquiler encontrado"),
                   @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")})
    public ResponseEntity<AlquilerResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/alquiler/{} - Buscar alquiler por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo alquiler", description = "Valida reserva confirmada + pago + cliente + vehículo + disponibilidad")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Alquiler creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<AlquilerResponseDTO>> guardar(@Valid @RequestBody AlquilerRequestDTO dto) {
        logger.info("POST /api/alquiler - Crear nuevo alquiler para cliente ID: {}, vehiculo ID: {}",
                dto.getClienteId(), dto.getVehiculoId());
        AlquilerResponseDTO creado = service.guardar(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Alquiler creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar alquiler")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alquiler actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")})
    public ResponseEntity<RespuestaExitosa<AlquilerResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlquilerRequestDTO dto) {
        logger.info("PUT /api/alquiler/actualizar/{} - Actualizar alquiler", id);
        AlquilerResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Alquiler actualizado correctamente", actualizado));
    }

    @PutMapping("/iniciar/{id}")
    @Operation(summary = "Iniciar alquiler", description = "Cambia el estado de Reservado a ACTIVO")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alquiler iniciado correctamente"),
                   @ApiResponse(responseCode = "400", description = "El alquiler no está en estado Reservado"),
                   @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")})
    public ResponseEntity<AlquilerResponseDTO> iniciarAlquiler(@PathVariable Long id) {
        logger.info("PUT /api/alquiler/iniciar/{} - Iniciar alquiler", id);
        return ResponseEntity.ok(service.iniciarAlquiler(id));
    }

    @PutMapping("/finalizar/{id}")
    @Operation(summary = "Finalizar alquiler", description = "Cambia el estado de ACTIVO a FINALIZADO")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alquiler finalizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "El alquiler no está en estado ACTIVO"),
                   @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")})
    public ResponseEntity<AlquilerResponseDTO> finalizarAlquiler(@PathVariable Long id) {
        logger.info("PUT /api/alquiler/finalizar/{} - Finalizar alquiler", id);
        return ResponseEntity.ok(service.finalizarAlquiler(id));
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar existencia de alquiler")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/alquiler/{}/existe - Verificar existencia", id);
        return ResponseEntity.ok(service.existePorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar alquiler")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alquiler eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/alquiler/{} - Eliminar alquiler", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Alquiler con ID " + id + " eliminado correctamente", null));
    }
}
