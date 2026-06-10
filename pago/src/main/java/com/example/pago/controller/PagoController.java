package com.example.pago.controller;

import com.example.pago.dto.PagoRequestDTO;
import com.example.pago.dto.PagoResponseDTO;
import com.example.pago.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pago")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    private final PagoService pagoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO crear(
            @Valid @RequestBody PagoRequestDTO dto) {
        logger.info("POST /api/pago - Crear nuevo pago para reserva ID: {}", dto.getIdReserva());
        return pagoService.crearPago(dto);
    }

    @GetMapping("/{id}")
    public PagoResponseDTO obtener(
            @PathVariable Long id) {
        logger.info("GET /api/pago/{} - Buscar pago por ID", id);
        return pagoService.obtenerPorId(id);
    }

    @GetMapping
    public List<PagoResponseDTO> listar() {
        logger.info("GET /api/pago - Listar todos los pagos");
        return pagoService.listarTodas();
    }

    @PutMapping("/{id}")
    public PagoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO dto) {
        logger.info("PUT /api/pago/{} - Actualizar pago", id);
        return pagoService.actualizarPago(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {
        logger.info("DELETE /api/pago/{} - Eliminar pago", id);
        pagoService.eliminarPago(id);
    }
}
