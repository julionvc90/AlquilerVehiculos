package com.example.cliente.controller;

import com.example.cliente.dto.ClienteRequestDTO;
import com.example.cliente.dto.ClienteResponseDTO;
import com.example.cliente.dto.RespuestaExitosa;
import com.example.cliente.service.ClienteService;
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
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        logger.info("GET /api/clientes - Listar todos los clientes");
        List<ClienteResponseDTO> clientes = service.listar();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/clientes/{} - Buscar cliente por ID", id);
        ClienteResponseDTO cliente = service.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<ClienteResponseDTO> buscarPorRut(@PathVariable String rut) {
        logger.info("GET /api/clientes/rut/{} - Buscar cliente por RUT", rut);
        ClienteResponseDTO cliente = service.buscarPorRut(rut);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        logger.info("POST /api/clientes - Crear nuevo cliente");
        ClienteResponseDTO creado = service.crear(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Cliente creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ClienteRequestDTO dto) {
        logger.info("PUT /api/clientes/{} - Actualizar cliente", id);
        ClienteResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Cliente actualizado correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/clientes/{} - Eliminar cliente", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Cliente con ID " + id + " eliminado correctamente", null));
    }
}
