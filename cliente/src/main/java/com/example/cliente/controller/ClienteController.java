package com.example.cliente.controller;

import com.example.cliente.dto.ClienteRequestDTO;
import com.example.cliente.dto.ClienteResponseDTO;
import com.example.cliente.dto.RespuestaExitosa;
import com.example.cliente.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones CRUD de gestión de clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida")
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        logger.info("GET /api/clientes - Listar todos los clientes");
        List<ClienteResponseDTO> clientes = service.listar();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                   @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/clientes/{} - Buscar cliente por ID", id);
        ClienteResponseDTO cliente = service.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar cliente por RUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                   @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    public ResponseEntity<ClienteResponseDTO> buscarPorRut(@PathVariable String rut) {
        logger.info("GET /api/clientes/rut/{} - Buscar cliente por RUT", rut);
        ClienteResponseDTO cliente = service.buscarPorRut(rut);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<ClienteResponseDTO>> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        logger.info("POST /api/clientes - Crear nuevo cliente");
        ClienteResponseDTO creado = service.crear(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Cliente creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    public ResponseEntity<RespuestaExitosa<ClienteResponseDTO>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ClienteRequestDTO dto) {
        logger.info("PUT /api/clientes/{} - Actualizar cliente", id);
        ClienteResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Cliente actualizado correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/clientes/{} - Eliminar cliente", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Cliente con ID " + id + " eliminado correctamente", null));
    }
}
