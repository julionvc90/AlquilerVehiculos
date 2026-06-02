package com.example.disponibilidad.controller;

import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.service.DisponibilidadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// JULIO NAVARRO
@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService service;

    @GetMapping
    public List<Disponibilidad> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Disponibilidad buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Disponibilidad guardar(@Valid @RequestBody Disponibilidad disponibilidad) {
        return service.guardar(disponibilidad);
    }

    @PutMapping("/{id}")
    public Disponibilidad actualizar(
            @PathVariable Long id,
            @RequestBody Disponibilidad disponibilidad) {

        return service.actualizar(id, disponibilidad);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}