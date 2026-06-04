package com.example.cliente.service;

import com.example.cliente.dto.ClienteRequestDTO;
import com.example.cliente.dto.ClienteResponseDTO;
import com.example.cliente.exception.ResourceNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<ClienteResponseDTO> listar() {
        logger.info("Listando todos los clientes");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        logger.info("Buscando cliente con ID: {}", id);
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
        return toResponseDTO(cliente);
    }

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        logger.info("Creando cliente con RUT: {}", dto.getRut());
        if (repository.existsByRut(dto.getRut())) {
            logger.warn("Conflicto: RUT {} ya existe", dto.getRut());
            throw new IllegalArgumentException("Ya existe un cliente con el RUT: " + dto.getRut());
        }
        Cliente cliente = toEntity(dto);
        Cliente guardado = repository.save(cliente);
        logger.info("Cliente creado exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        logger.info("Actualizando cliente con ID: {}", id);
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));

        if (dto.getRut() != null && !dto.getRut().equals(cliente.getRut())
                && repository.existsByRut(dto.getRut())) {
            logger.warn("Conflicto: RUT {} ya existe en otro cliente", dto.getRut());
            throw new IllegalArgumentException("Ya existe otro cliente con el RUT: " + dto.getRut());
        }

        cliente.setRut(dto.getRut());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        if (dto.getUsuarioId() != null) {
            cliente.setUsuarioId(dto.getUsuarioId());
        }

        Cliente actualizado = repository.save(cliente);
        logger.info("Cliente con ID {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando cliente con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        repository.deleteById(id);
        logger.info("Cliente con ID {} eliminado exitosamente", id);
    }

    public ClienteResponseDTO buscarPorRut(String rut) {
        logger.info("Buscando cliente por RUT: {}", rut);
        Cliente cliente = repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con RUT: " + rut));
        return toResponseDTO(cliente);
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .rut(cliente.getRut())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .usuarioId(cliente.getUsuarioId())
                .activo(cliente.isActivo())
                .build();
    }

    private Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setRut(dto.getRut());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente.setUsuarioId(dto.getUsuarioId());
        return cliente;
    }
}
