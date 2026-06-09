package com.example.inspeccion.controller;

import com.example.inspeccion.dto.InspeccionRequestDTO;
import com.example.inspeccion.dto.InspeccionResponseDTO;
import com.example.inspeccion.service.InspeccionService;
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
@RequestMapping("/api/inspecciones")
public class InspeccionController {

    private static final Logger logger = LoggerFactory.getLogger(InspeccionController.class);

    private final InspeccionService service;

    public InspeccionController(InspeccionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InspeccionResponseDTO>> listar() {
        logger.info("GET /api/inspecciones - Listar todas las inspecciones");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspeccionResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/inspecciones/{} - Buscar inspeccion por ID", id);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/alquiler/{alquilerId}")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorAlquiler(@PathVariable Long alquilerId) {
        logger.info("GET /api/inspecciones/alquiler/{} - Buscar por alquiler", alquilerId);
        return ResponseEntity.ok(service.buscarPorAlquilerId(alquilerId));
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorVehiculo(@PathVariable Long vehiculoId) {
        logger.info("GET /api/inspecciones/vehiculo/{} - Buscar por vehiculo", vehiculoId);
        return ResponseEntity.ok(service.buscarPorVehiculoId(vehiculoId));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<InspeccionResponseDTO>> buscarPorTipo(@PathVariable String tipo) {
        logger.info("GET /api/inspecciones/tipo/{} - Buscar por tipo de inspeccion", tipo);
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<InspeccionResponseDTO> crear(@Valid @RequestBody InspeccionRequestDTO dto) {
        logger.info("POST /api/inspecciones - Crear nueva inspeccion");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InspeccionResponseDTO> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody InspeccionRequestDTO dto) {
        logger.info("PUT /api/inspecciones/{} - Actualizar inspeccion", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/inspecciones/{} - Eliminar inspeccion", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
