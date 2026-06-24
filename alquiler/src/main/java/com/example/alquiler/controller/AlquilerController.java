package com.example.alquiler.controller;

import com.example.alquiler.dto.AlquilerRequestDTO;
import com.example.alquiler.dto.AlquilerResponseDTO;
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
@Tag(name= "Alquiler", description = "Operaciones relacionadas con el alquiler")
public class AlquilerController {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerController.class);

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "obtener todos los estados de disponibilidad",
            description = "opbtiene una lista de estados de disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operación exitosa"),
            @ApiResponse(responseCode = "204", description = "opertación exitosa pero no existen disponibilidades registradas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<AlquilerResponseDTO>> listar() {
        logger.info("GET /api/alquiler - Listar todos los alquileres");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener disponibilidad por ID",
            description = "Obtiene el registro de disponibilidad correspondiente al ID ingresado. Incluye el estado disponible del vehículo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Error en el servidor"),
            @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una disponibilidad con el ID ingresado"),
            @ApiResponse(responseCode = "400", description = "El ID enviado no es válido")
    })
    public ResponseEntity<AlquilerResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/alquiler/{} - Buscar alquiler por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(
            summary = "Validar disponibilidad de un vehículo",
            description = "Verifica si un vehículo está disponible en un rango de fechas usando el ID del vehículo, la fecha de inicio y la fecha de fin. Retorna true si está disponible y false si no lo está."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación realizada correctamente. Retorna true o false"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos, fechas incorrectas o parámetros incompletos"),
            @ApiResponse(responseCode = "404", description = "No existe un vehículo con el ID ingresado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AlquilerResponseDTO> guardar(@Valid @RequestBody AlquilerRequestDTO dto) {
        logger.info("POST /api/alquiler - Crear nuevo alquiler para cliente ID: {}, vehiculo ID: {}",
                dto.getClienteId(), dto.getVehiculoId());
        return new ResponseEntity<>(service.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    @Operation(
            summary = "Actualizar disponibilidad por ID",
            description = "Actualiza el registro de disponibilidad correspondiente al ID ingresado, modificando los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido o datos enviados incorrectos"),
            @ApiResponse(responseCode = "404", description = "No existe una disponibilidad con el ID ingresado"),
            @ApiResponse(responseCode = "409", description = "No se puede actualizar porque existe un conflicto con otros datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AlquilerResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlquilerRequestDTO dto) {
        logger.info("PUT /api/alquiler/actualizar/{} - Actualizar alquiler", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @PutMapping("/iniciar/{id}")
    public ResponseEntity<AlquilerResponseDTO> iniciarAlquiler(@PathVariable Long id) {
        logger.info("PUT /api/alquiler/iniciar/{} - Iniciar alquiler", id);
        return ResponseEntity.ok(service.iniciarAlquiler(id));
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<AlquilerResponseDTO> finalizarAlquiler(@PathVariable Long id) {
        logger.info("PUT /api/alquiler/finalizar/{} - Finalizar alquiler", id);
        return ResponseEntity.ok(service.finalizarAlquiler(id));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/alquiler/{}/existe - Verificar existencia", id);
        return ResponseEntity.ok(service.existePorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar alquiler por ID",
            description = "Elimina un registro de alquiler del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disponibilidad eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID enviado no es válido"),
            @ApiResponse(responseCode = "404", description = "No existe una disponibilidad con el ID ingresado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque está relacionada con otros datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/alquiler/{} - Eliminar alquiler", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
