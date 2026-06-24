package com.example.vehiculo.controller;

import com.example.vehiculo.dto.VehiculoRequestDTO;
import com.example.vehiculo.dto.VehiculoResponseDTO;
import com.example.vehiculo.service.VehiculoService;
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
@RequestMapping("/api/vehiculos")
@Tag(name= "Vehiculo", description = "Operaciones relacionadas con vehiculo")
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "obtener todos los vehiculos",
            description = "opbtiene una lista de todos los vehiculos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. Retorna la lista de vehículos"),
            @ApiResponse(responseCode = "204", description = "Operación exitosa, pero no existen vehículos registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<VehiculoResponseDTO>> listar() {
        logger.info("GET /api/vehiculos - Listar todos los vehiculos");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "obtener el vehiculo del id indicado",
            description = "opbtiene el vehiculo con el id ingresado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "204", description = "Operacion exitosa pero no envió  confirmacion"),
            @ApiResponse(responseCode = "404", description = "No existe el recuerso que se requiere eliminar"),
            @ApiResponse(responseCode = "409", description = "no se puede eliminar porque está relacionado con otros datos"),
            @ApiResponse(responseCode = "400", description = "el id enviado no es valido")
    })
    public ResponseEntity<VehiculoResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{} - Buscar vehiculo por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "crear nuevos vehiculos",
            description = "crea un nuevo vehiculo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "No se puede crear el vehículo porque ya existe un registro relacionado o repetido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<VehiculoResponseDTO> guardar(@Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("POST /api/vehiculos - Crear nuevo vehiculo con patente: {}", dto.getPatente());
        return new ResponseEntity<>(service.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "actualizar un vehiculo",
            description = "actualiza un vehiculos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido o datos enviados incorrectos"),
            @ApiResponse(responseCode = "404", description = "No existe el vehículo que se requiere actualizar"),
            @ApiResponse(responseCode = "409", description = "No se puede actualizar porque está relacionado con otros datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<VehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("PUT /api/vehiculos/{} - Actualizar vehiculo", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar si existe un vehículo",
            description = "Valida la existencia de un vehículo mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. Retorna true o false"),
            @ApiResponse(responseCode = "400", description = "El ID enviado no es válido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{}/existe - Verificar existencia", id);
        return ResponseEntity.ok(service.existePorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "elimina un vehiculo apartir del ID",
            description = "eliminar vehiculo usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehículo eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "El ID enviado no es válido"),
            @ApiResponse(responseCode = "404", description = "No existe el vehículo que se requiere eliminar"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque está relacionado con otros datos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/vehiculos/{} - Eliminar vehiculo", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
