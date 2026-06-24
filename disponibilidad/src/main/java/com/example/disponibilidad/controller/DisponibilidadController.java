package com.example.disponibilidad.controller;

import com.example.disponibilidad.dto.DisponibilidadRequestDTO;
import com.example.disponibilidad.dto.DisponibilidadResponseDTO;
import com.example.disponibilidad.dto.RespuestaExitosa;
import com.example.disponibilidad.service.DisponibilidadService;
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
public class DisponibilidadController {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadController.class);

    private final DisponibilidadService service;

    public DisponibilidadController(DisponibilidadService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DisponibilidadResponseDTO>> listar() {
        logger.info("GET /api/disponibilidad - Listar todas las disponibilidades");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/disponibilidad/{} - Buscar disponibilidad por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DisponibilidadResponseDTO> guardar(@Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("POST /api/disponibilidad - Crear nueva disponibilidad para vehiculo ID: {}", dto.getVehiculoId());
        DisponibilidadResponseDTO creado = service.guardar(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Disponibilidad creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/validar")
    public ResponseEntity<Boolean> validarDisponibilidad(
            @RequestParam Long vehiculoId,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {
        logger.info("GET /api/disponibilidad/validar - Validar vehiculo ID: {} desde {} hasta {}", vehiculoId, inicio, fin);
        return ResponseEntity.ok(service.validarDisponibilidad(vehiculoId, inicio, fin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DisponibilidadRequestDTO dto) {
        logger.info("PUT /api/disponibilidad/{} - Actualizar disponibilidad", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/disponibilidad/{} - Eliminar disponibilidad", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Disponibilidad con ID " + id + " eliminada correctamente", null));
    }
}
