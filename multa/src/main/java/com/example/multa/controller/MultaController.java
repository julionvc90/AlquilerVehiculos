package com.example.multa.controller;

import com.example.multa.dto.MultaRequestDTO;
import com.example.multa.dto.MultaResponseDTO;
import com.example.multa.dto.RespuestaExitosa;
import org.springframework.http.ResponseEntity;
import com.example.multa.service.MultaService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/multa")
public class MultaController {

    private static final Logger logger = LoggerFactory.getLogger(MultaController.class);

    private final MultaService multaService;

    @PostMapping
    public ResponseEntity<RespuestaExitosa<MultaResponseDTO>> crearMulta(
            @Valid @RequestBody MultaRequestDTO dto) {
        logger.info("POST /api/multa - Crear nueva multa para reserva ID: {}", dto.getIdReserva());
        MultaResponseDTO creado = multaService.crearMulta(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Multa creada correctamente", creado),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public MultaResponseDTO obtenerMulta(
            @PathVariable Long id) {
        logger.info("GET /api/multa/{} - Buscar multa por ID", id);
        return multaService.obtenerPorId(id);
    }

    @GetMapping
    public List<MultaResponseDTO> listarMulta() {
        logger.info("GET /api/multa - Listar todas las multas");
        return multaService.listarTodas();
    }

    @PutMapping("/{id}")
    public MultaResponseDTO actualizarMulta(
            @PathVariable Long id,
            @Valid @RequestBody MultaRequestDTO dto) {
        logger.info("PUT /api/multa/{} - Actualizar multa", id);
        MultaResponseDTO actualizado = multaService.actualizarMulta(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Multa actualizada correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaExitosa<Void>> eliminarMulta(
            @PathVariable Long id) {
        logger.info("DELETE /api/multa/{} - Eliminar multa", id);
        multaService.eliminarMulta(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Multa con ID " + id + " eliminada correctamente", null));
    }

}
