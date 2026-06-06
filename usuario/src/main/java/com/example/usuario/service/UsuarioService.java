package com.example.usuario.service;

import com.example.usuario.dto.UsuarioRequestDTO;
import com.example.usuario.dto.UsuarioResponseDTO;
import com.example.usuario.exception.ResourceNotFoundException;
import com.example.usuario.model.Usuario;
import com.example.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioResponseDTO> listar() {
        logger.info("Listando todos los usuarios");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return toResponseDTO(usuario);
    }

    public boolean existePorId(Long id) {
        logger.info("Verificando existencia de usuario con ID: {}", id);
        return repository.existsById(id);
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        logger.info("Creando usuario con username: {}", dto.getUsername());
        if (repository.existsByUsername(dto.getUsername())) {
            logger.warn("Conflicto: username {} ya existe", dto.getUsername());
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + dto.getUsername());
        }
        if (repository.existsByEmail(dto.getEmail())) {
            logger.warn("Conflicto: email {} ya existe", dto.getEmail());
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        Usuario usuario = toEntity(dto);
        Usuario guardado = repository.save(usuario);
        logger.info("Usuario creado exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        logger.info("Actualizando usuario con ID: {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        if (dto.getUsername() != null && !dto.getUsername().equals(usuario.getUsername())
                && repository.existsByUsername(dto.getUsername())) {
            logger.warn("Conflicto: username {} ya existe en otro usuario", dto.getUsername());
            throw new IllegalArgumentException("Ya existe otro usuario con el username: " + dto.getUsername());
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())
                && repository.existsByEmail(dto.getEmail())) {
            logger.warn("Conflicto: email {} ya existe en otro usuario", dto.getEmail());
            throw new IllegalArgumentException("Ya existe otro usuario con el email: " + dto.getEmail());
        }

        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());

        Usuario actualizado = repository.save(usuario);
        logger.info("Usuario con ID {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        repository.deleteById(id);
        logger.info("Usuario con ID {} eliminado exitosamente", id);
    }

    public UsuarioResponseDTO buscarPorUsername(String username) {
        logger.info("Buscando usuario por username: {}", username);
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con username: " + username));
        return toResponseDTO(usuario);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .build();
    }

    private Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol());
        return usuario;
    }
}
