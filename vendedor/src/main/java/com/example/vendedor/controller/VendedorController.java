package com.example.vendedor.controller;

import com.example.vendedor.dto.RespuestaExitosa;
import com.example.vendedor.dto.VendedorRequestDTO;
import com.example.vendedor.dto.VendedorResponseDTO;
import com.example.vendedor.service.VendedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/vendedores")
@Tag(name = "Vendedores", description = "Operaciones CRUD de gestión de vendedores")
public class VendedorController {

    private static final Logger logger = LoggerFactory.getLogger(VendedorController.class);

    private final VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los vendedores")
    @ApiResponse(responseCode = "200", description = "Lista de vendedores obtenida")
    public ResponseEntity<List<VendedorResponseDTO>> listar() {
        logger.info("GET /api/vendedores - Listar todos los vendedores");
        List<VendedorResponseDTO> vendedores = service.listar();
        return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vendedor por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
                   @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")})
    public ResponseEntity<VendedorResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/vendedores/{} - Buscar vendedor por ID", id);
        VendedorResponseDTO vendedor = service.buscarPorId(id);
        return ResponseEntity.ok(vendedor);
    }

    @GetMapping("/rut/{rut}")
    @Operation(summary = "Buscar vendedor por RUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vendedor encontrado"),
                   @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")})
    public ResponseEntity<VendedorResponseDTO> buscarPorRut(@PathVariable String rut) {
        logger.info("GET /api/vendedores/rut/{} - Buscar vendedor por RUT", rut);
        VendedorResponseDTO vendedor = service.buscarPorRut(rut);
        return ResponseEntity.ok(vendedor);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo vendedor")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Vendedor creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<VendedorResponseDTO>> crear(@Valid @RequestBody VendedorRequestDTO dto) {
        logger.info("POST /api/vendedores - Crear nuevo vendedor");
        VendedorResponseDTO creado = service.crear(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Vendedor creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vendedor")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vendedor actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                   @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")})
    public ResponseEntity<RespuestaExitosa<VendedorResponseDTO>> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody VendedorRequestDTO dto) {
        logger.info("PUT /api/vendedores/{} - Actualizar vendedor", id);
        VendedorResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Vendedor actualizado correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vendedor")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vendedor eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/vendedores/{} - Eliminar vendedor", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Vendedor con ID " + id + " eliminado correctamente", null));
    }
}
