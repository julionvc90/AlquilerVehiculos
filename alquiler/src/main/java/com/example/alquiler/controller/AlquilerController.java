package com.example.alquiler.controller;

import com.example.alquiler.model.Alquiler;
import com.example.alquiler.service.AlquilerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/alquiler")
public class AlquilerController {

    private final AlquilerService service;

    public AlquilerController(AlquilerService service) {
        this.service = service;
    }
    @GetMapping
    public List<Alquiler> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Alquiler buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Alquiler guardar(@Valid @RequestBody Alquiler alquiler) {
        return service.guardar(alquiler);
    }

    @PutMapping("/actualizar/{id}")
    public Alquiler actualizar(
            @PathVariable Long id,
            @RequestBody Alquiler alquiler) {

        return service.actualizar(id, alquiler);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PutMapping("/finalizar/{id}")
    public void finalizarAlquiler(@PathVariable Long id) {
        service.finalizarAlquiler(id);
    }

    @PutMapping("/iniciar/{id}")
    public void iniciarAlquiler(@PathVariable Long id) {
        service.iniciarAlquiler(id);
    }
}
