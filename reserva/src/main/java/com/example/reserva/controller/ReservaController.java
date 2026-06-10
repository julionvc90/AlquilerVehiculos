package com.example.reserva.controller;

import com.example.reserva.dto.ReservaRequestDTO;
import com.example.reserva.dto.ReservaResponseDTO;
import com.example.reserva.service.ReservaService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponseDTO crearReserva(
            @Valid @RequestBody ReservaRequestDTO dto) {

        return reservaService.crearReserva(dto);
    }

    @GetMapping("/{id}")
    public ReservaResponseDTO obtenerReserva(
            @PathVariable Long id) {

        return reservaService.obtenerPorId(id);
    }

    @GetMapping
    public List<ReservaResponseDTO> listarReserva() {

        return reservaService.listarTodas();
    }

    @PutMapping("/{id}")
    public ReservaResponseDTO actualizarReserva(
            @PathVariable Long id,
            @Valid @RequestBody ReservaRequestDTO dto) {

        return reservaService.actualizarReserva(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarReserva(
            @PathVariable Long id) {

        reservaService.eliminarReserva(id);
    }
}
