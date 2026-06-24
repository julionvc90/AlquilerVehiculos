package com.example.vendedor.service;

import com.example.vendedor.dto.VendedorRequestDTO;
import com.example.vendedor.dto.VendedorResponseDTO;
import com.example.vendedor.exception.ResourceNotFoundException;
import com.example.vendedor.model.Vendedor;
import com.example.vendedor.repository.VendedorRepository;
import com.example.vendedor.webclient.UsuarioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendedorService {

    private static final Logger logger = LoggerFactory.getLogger(VendedorService.class);

    private final VendedorRepository repository;
    private final UsuarioClient usuarioClient;

    public VendedorService(VendedorRepository repository, UsuarioClient usuarioClient) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
    }

    public List<VendedorResponseDTO> listar() {
        logger.info("Listando todos los vendedores");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VendedorResponseDTO buscarPorId(Long id) {
        logger.info("Buscando vendedor con ID: {}", id);
        Vendedor vendedor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor", id));
        return toResponseDTO(vendedor);
    }

    public VendedorResponseDTO crear(VendedorRequestDTO dto) {
        logger.info("Creando vendedor con RUT: {}", dto.getRut());
        if (repository.existsByRut(dto.getRut())) {
            logger.warn("Conflicto: RUT {} ya existe", dto.getRut());
            throw new IllegalArgumentException("Ya existe un vendedor con el RUT: " + dto.getRut());
        }
        if (!usuarioClient.existeUsuario(dto.getUsuarioId())) {
            logger.error("Usuario ID {} no existe en el sistema", dto.getUsuarioId());
            throw new IllegalArgumentException("El usuario especificado no existe en el sistema");
        }
        Vendedor vendedor = toEntity(dto);
        Vendedor guardado = repository.save(vendedor);
        logger.info("Vendedor creado exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public VendedorResponseDTO actualizar(Long id, VendedorRequestDTO dto) {
        logger.info("Actualizando vendedor con ID: {}", id);
        Vendedor vendedor = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor", id));

        if (dto.getRut() != null && !dto.getRut().equals(vendedor.getRut())
                && repository.existsByRut(dto.getRut())) {
            logger.warn("Conflicto: RUT {} ya existe en otro vendedor", dto.getRut());
            throw new IllegalArgumentException("Ya existe otro vendedor con el RUT: " + dto.getRut());
        }

        if (!usuarioClient.existeUsuario(dto.getUsuarioId())) {
            logger.error("Usuario ID {} no existe en el sistema", dto.getUsuarioId());
            throw new IllegalArgumentException("El usuario especificado no existe en el sistema");
        }

        vendedor.setRut(dto.getRut());
        vendedor.setNombre(dto.getNombre());
        vendedor.setApellido(dto.getApellido());
        vendedor.setEmail(dto.getEmail());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setUsuarioId(dto.getUsuarioId());

        Vendedor actualizado = repository.save(vendedor);
        logger.info("Vendedor con ID {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando vendedor con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Vendedor", id);
        }
        repository.deleteById(id);
        logger.info("Vendedor con ID {} eliminado exitosamente", id);
    }

    public VendedorResponseDTO buscarPorRut(String rut) {
        logger.info("Buscando vendedor por RUT: {}", rut);
        Vendedor vendedor = repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor con RUT: " + rut));
        return toResponseDTO(vendedor);
    }

    private VendedorResponseDTO toResponseDTO(Vendedor vendedor) {
        return VendedorResponseDTO.builder()
                .id(vendedor.getId())
                .rut(vendedor.getRut())
                .nombre(vendedor.getNombre())
                .apellido(vendedor.getApellido())
                .email(vendedor.getEmail())
                .telefono(vendedor.getTelefono())
                .usuarioId(vendedor.getUsuarioId())
                .activo(vendedor.isActivo())
                .build();
    }

    private Vendedor toEntity(VendedorRequestDTO dto) {
        Vendedor vendedor = new Vendedor();
        vendedor.setRut(dto.getRut());
        vendedor.setNombre(dto.getNombre());
        vendedor.setApellido(dto.getApellido());
        vendedor.setEmail(dto.getEmail());
        vendedor.setTelefono(dto.getTelefono());
        vendedor.setUsuarioId(dto.getUsuarioId());
        return vendedor;
    }
}
