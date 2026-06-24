package com.example.disponibilidad.controller;

import com.example.disponibilidad.dto.DisponibilidadRequestDTO;
import com.example.disponibilidad.dto.DisponibilidadResponseDTO;
import com.example.disponibilidad.service.DisponibilidadService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@Tag(name= "Disponibilidad", description = "Operaciones relacionadas con las Disponibilidades")
public class DisponibilidadController {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadController.class);

    private final DisponibilidadService service;

    public DisponibilidadController(DisponibilidadService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "obtener todos los estados de disponibilidad",
            description = "opbtiene una lista de estados de disponibilidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operación exitosa"),
            @ApiResponse(responseCode = "204", description = "opertación exitosa pero no existen disponibilidades registradas"),
            @ApiResponse(responseCode = "500", description = "error interno del servidor")
    })
    public ResponseEntity<List<DisponibilidadResponseDTO>> listar() {
        logger.info("GET /api/disponibilidad - Listar todas las disponibilidades");
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
    @PostMapping
    public ResponseEntity<DisponibilidadResponseDTO> guardar(@Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("POST /api/disponibilidad - Crear nueva disponibilidad para vehiculo ID: {}", dto.getVehiculoId());
        return new ResponseEntity<>(service.guardar(dto), HttpStatus.CREATED);
    }

    @GetMapping("/validar")
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
    public ResponseEntity<Boolean> validarDisponibilidad(
            @RequestParam Long vehiculoId,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {

        logger.info("GET /api/disponibilidad/validar - Validar vehiculo ID: {} desde {} hasta {}", vehiculoId, inicio, fin);
        return ResponseEntity.ok(service.validarDisponibilidad(vehiculoId, inicio, fin));
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<DisponibilidadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("PUT /api/disponibilidad/{} - Actualizar disponibilidad", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar disponibilidad por ID",
            description = "Elimina un registro de disponibilidad del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disponibilidad eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID enviado no es válido"),
            @ApiResponse(responseCode = "404", description = "No existe una disponibilidad con el ID ingresado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque está relacionada con otros datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/disponibilidad/{} - Eliminar disponibilidad", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
