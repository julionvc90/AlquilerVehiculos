package com.example.pago.controller;

import com.example.pago.dto.PagoRequestDTO;
import com.example.pago.dto.PagoResponseDTO;

import com.example.pago.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pago")
@Valid

public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO crear(
            @Valid @RequestBody PagoRequestDTO dto) {

        return pagoService.crearPago(dto);
    }

    @GetMapping("/{id}")
    public PagoResponseDTO obtener(
            @PathVariable Long id) {

        return pagoService.obtenerPorId(id);
    }

    @GetMapping
    public List<PagoResponseDTO> listar() {

        return pagoService.listarTodas();
    }

    @PutMapping("/{id}")
    public PagoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO dto) {

        return pagoService.actualizarPago(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id) {

        pagoService.eliminarPago(id);
    }
}
