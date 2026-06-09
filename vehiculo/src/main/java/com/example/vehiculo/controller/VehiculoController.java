package com.example.vehiculo.controller;

import com.example.vehiculo.dto.VehiculoRequestDTO;
import com.example.vehiculo.dto.VehiculoResponseDTO;
import com.example.vehiculo.service.VehiculoService;
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
public class VehiculoController {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponseDTO>> listar() {
        logger.info("GET /api/vehiculos - Listar todos los vehiculos");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{} - Buscar vehiculo por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<VehiculoResponseDTO> guardar(@Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("POST /api/vehiculos - Crear nuevo vehiculo con patente: {}", dto.getPatente());
        return new ResponseEntity<>(service.guardar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO dto) {
        logger.info("PUT /api/vehiculos/{} - Actualizar vehiculo", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/vehiculos/{}/existe - Verificar existencia", id);
        return ResponseEntity.ok(service.existePorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/vehiculos/{} - Eliminar vehiculo", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
