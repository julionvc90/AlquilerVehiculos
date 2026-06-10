package com.example.multa.controller;

import com.example.multa.dto.MultaRequestDTO;
import com.example.multa.dto.MultaResponseDTO;
import com.example.multa.service.MultaService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/multa")
public class MultaController {

    private final MultaService multaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MultaResponseDTO crearMulta(
            @Valid @RequestBody MultaRequestDTO dto) {

        return multaService.crearMulta(dto);
    }

    @GetMapping("/{id}")
    public MultaResponseDTO obtenerMulta(
            @PathVariable Long id) {

        return multaService.obtenerPorId(id);
    }

    @GetMapping
    public List<MultaResponseDTO> listarMulta() {

        return multaService.listarTodas();
    }

    @PutMapping("/{id}")
    public MultaResponseDTO actualizarMulta(
            @PathVariable Long id,
            @Valid @RequestBody MultaRequestDTO dto) {

        return multaService.actualizarMulta(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarMulta(
            @PathVariable Long id) {

        multaService.eliminarMulta(id);
    }

}
