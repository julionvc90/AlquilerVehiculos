package com.example.alquiler.controller;

import com.example.alquiler.dto.AlquilerRequestDTO;
import com.example.alquiler.dto.AlquilerResponseDTO;
import com.example.alquiler.service.AlquilerService;
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
public class AlquilerController {

    private static final Logger logger = LoggerFactory.getLogger(AlquilerController.class);

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlquilerResponseDTO>> listar() {
        logger.info("GET /api/alquiler - Listar todos los alquileres");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/alquiler/{} - Buscar alquiler por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlquilerResponseDTO> guardar(@Valid @RequestBody AlquilerRequestDTO dto) {
        logger.info("POST /api/alquiler - Crear nuevo alquiler para cliente ID: {}, vehiculo ID: {}",
                dto.getClienteId(), dto.getVehiculoId());
        return new ResponseEntity<>(service.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/alquiler/{} - Eliminar alquiler", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
