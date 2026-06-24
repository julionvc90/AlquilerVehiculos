package com.example.usuario.controller;

import com.example.usuario.dto.RespuestaExitosa;
import com.example.usuario.dto.UsuarioRequestDTO;
import com.example.usuario.dto.UsuarioResponseDTO;
import com.example.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones CRUD de gestión de usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        logger.info("GET /api/usuarios - Listar todos los usuarios");
        List<UsuarioResponseDTO> usuarios = service.listar();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                   @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        logger.info("GET /api/usuarios/{} - Buscar usuario por ID", id);
        UsuarioResponseDTO usuario = service.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}/existe")
    @Operation(summary = "Verificar existencia de usuario")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        logger.info("GET /api/usuarios/{}/existe - Verificar existencia", id);
        boolean existe = service.existePorId(id);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Buscar usuario por username")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                   @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<UsuarioResponseDTO> buscarPorUsername(
            @Parameter(description = "Nombre de usuario a buscar") @PathVariable String username) {
        logger.info("GET /api/usuarios/username/{} - Buscar usuario por username", username);
        UsuarioResponseDTO usuario = service.buscarPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Registra un usuario con rol ADMIN, CLIENTE o VENDEDOR")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio")})
    public ResponseEntity<RespuestaExitosa<UsuarioResponseDTO>> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        logger.info("POST /api/usuarios - Crear nuevo usuario");
        UsuarioResponseDTO creado = service.crear(dto);
        return new ResponseEntity<>(
                new RespuestaExitosa<>("Usuario creado correctamente", creado),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
                   @ApiResponse(responseCode = "400", description = "Datos inválidos o regla de negocio"),
                   @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<RespuestaExitosa<UsuarioResponseDTO>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody UsuarioRequestDTO dto) {
        logger.info("PUT /api/usuarios/{} - Actualizar usuario", id);
        UsuarioResponseDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Usuario actualizado correctamente", actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
                   @ApiResponse(responseCode = "404", description = "Usuario no encontrado")})
    public ResponseEntity<RespuestaExitosa<Void>> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/usuarios/{} - Eliminar usuario", id);
        service.eliminar(id);
        return ResponseEntity.ok(
                new RespuestaExitosa<>("Usuario con ID " + id + " eliminado correctamente", null));
    }
}
