package com.example.inspeccion.service;

import com.example.inspeccion.dto.InspeccionRequestDTO;
import com.example.inspeccion.dto.InspeccionResponseDTO;
import com.example.inspeccion.exception.ResourceNotFoundException;
import com.example.inspeccion.model.Inspeccion;
import com.example.inspeccion.repository.InspeccionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspeccionService {

    private static final Logger logger = LoggerFactory.getLogger(InspeccionService.class);

    private final InspeccionRepository repository;

    public InspeccionService(InspeccionRepository repository) {
        this.repository = repository;
    }

    public List<InspeccionResponseDTO> listar() {
        logger.info("Listando todas las inspecciones");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public InspeccionResponseDTO buscarPorId(Long id) {
        logger.info("Buscando inspeccion con ID: {}", id);
        Inspeccion inspeccion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspeccion", id));
        return toResponseDTO(inspeccion);
    }

    public List<InspeccionResponseDTO> buscarPorAlquilerId(Long alquilerId) {
        logger.info("Buscando inspecciones del alquiler ID: {}", alquilerId);
        return repository.findByAlquilerId(alquilerId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InspeccionResponseDTO> buscarPorVehiculoId(Long vehiculoId) {
        logger.info("Buscando inspecciones del vehiculo ID: {}", vehiculoId);
        return repository.findByVehiculoId(vehiculoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public InspeccionResponseDTO crear(InspeccionRequestDTO dto) {
        logger.info("Creando inspeccion para alquiler ID: {}, vehiculo ID: {}", dto.getAlquilerId(), dto.getVehiculoId());
        Inspeccion inspeccion = toEntity(dto);
        Inspeccion guardado = repository.save(inspeccion);
        logger.info("Inspeccion creada exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public InspeccionResponseDTO actualizar(Long id, InspeccionRequestDTO dto) {
        logger.info("Actualizando inspeccion con ID: {}", id);
        Inspeccion inspeccion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspeccion", id));

        if (dto.getAlquilerId() != null) {
            inspeccion.setAlquilerId(dto.getAlquilerId());
        }
        if (dto.getVehiculoId() != null) {
            inspeccion.setVehiculoId(dto.getVehiculoId());
        }
        inspeccion.setFechaInspeccion(dto.getFechaInspeccion());
        inspeccion.setTipoInspeccion(dto.getTipoInspeccion());
        inspeccion.setResultado(dto.getResultado());
        inspeccion.setObservaciones(dto.getObservaciones());
        inspeccion.setInspector(dto.getInspector());

        Inspeccion actualizado = repository.save(inspeccion);
        logger.info("Inspeccion con ID {} actualizada exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando inspeccion con ID: {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Inspeccion", id);
        }
        repository.deleteById(id);
        logger.info("Inspeccion con ID {} eliminada exitosamente", id);
    }

    public List<InspeccionResponseDTO> buscarPorTipo(String tipo) {
        logger.info("Buscando inspecciones de tipo: {}", tipo);
        return repository.findByTipoInspeccion(tipo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private InspeccionResponseDTO toResponseDTO(Inspeccion inspeccion) {
        return InspeccionResponseDTO.builder()
                .id(inspeccion.getId())
                .alquilerId(inspeccion.getAlquilerId())
                .vehiculoId(inspeccion.getVehiculoId())
                .fechaInspeccion(inspeccion.getFechaInspeccion())
                .tipoInspeccion(inspeccion.getTipoInspeccion())
                .resultado(inspeccion.getResultado())
                .observaciones(inspeccion.getObservaciones())
                .inspector(inspeccion.getInspector())
                .activo(inspeccion.isActivo())
                .build();
    }

    private Inspeccion toEntity(InspeccionRequestDTO dto) {
        Inspeccion inspeccion = new Inspeccion();
        inspeccion.setAlquilerId(dto.getAlquilerId());
        inspeccion.setVehiculoId(dto.getVehiculoId());
        inspeccion.setFechaInspeccion(dto.getFechaInspeccion());
        inspeccion.setTipoInspeccion(dto.getTipoInspeccion());
        inspeccion.setResultado(dto.getResultado());
        inspeccion.setObservaciones(dto.getObservaciones());
        inspeccion.setInspector(dto.getInspector());
        return inspeccion;
    }
}
