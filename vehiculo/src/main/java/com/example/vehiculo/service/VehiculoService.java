package com.example.vehiculo.service;

import com.example.vehiculo.dto.VehiculoRequestDTO;
import com.example.vehiculo.dto.VehiculoResponseDTO;
import com.example.vehiculo.exception.ResourceNotFoundException;
import com.example.vehiculo.model.Vehiculo;
import com.example.vehiculo.repository.VehiculoRepository;
import com.example.vehiculo.webclient.VendedorClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {

    private static final Logger logger = LoggerFactory.getLogger(VehiculoService.class);

    @Autowired
    private VehiculoRepository repository;

    @Autowired
    private VendedorClient vendedorClient;

    public List<VehiculoResponseDTO> listar() {
        logger.info("Listando todos los vehiculos");
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public VehiculoResponseDTO buscarPorId(Long id) {
        logger.info("Buscando vehiculo con ID: {}", id);
        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Vehiculo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Vehiculo", id);
                });
        return toResponseDTO(vehiculo);
    }

    public VehiculoResponseDTO guardar(VehiculoRequestDTO dto) {
        logger.info("Guardando vehiculo con patente: {}", dto.getPatente());

        vendedorClient.obtenerVendedor(dto.getVendedorId());

        Vehiculo vehiculo = toEntity(dto);
        Vehiculo guardado = repository.save(vehiculo);
        logger.info("Vehiculo guardado exitosamente con ID: {}", guardado.getId());
        return toResponseDTO(guardado);
    }

    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto) {
        logger.info("Actualizando vehiculo con ID: {}", id);

        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Vehiculo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Vehiculo", id);
                });

        vendedorClient.obtenerVendedor(dto.getVendedorId());

        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCategoria(dto.getCategoria());
        vehiculo.setCapacidadPasajeros(dto.getCapacidadPasajeros());
        vehiculo.setColor(dto.getColor());
        vehiculo.setVendedorId(dto.getVendedorId());
        vehiculo.setTarifaDiaria(dto.getTarifaDiaria());
        vehiculo.setUbicacion(dto.getUbicacion());

        Vehiculo actualizado = repository.save(vehiculo);
        logger.info("Vehiculo con ID {} actualizado exitosamente", id);
        return toResponseDTO(actualizado);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public void eliminar(Long id) {
        logger.info("Eliminando vehiculo con ID: {}", id);
        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Vehiculo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Vehiculo", id);
                });
        repository.delete(vehiculo);
        logger.info("Vehiculo con ID {} eliminado exitosamente", id);
    }

    private VehiculoResponseDTO toResponseDTO(Vehiculo vehiculo) {
        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .patente(vehiculo.getPatente())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .anio(vehiculo.getAnio())
                .categoria(vehiculo.getCategoria())
                .capacidadPasajeros(vehiculo.getCapacidadPasajeros())
                .color(vehiculo.getColor())
                .vendedorId(vehiculo.getVendedorId())
                .tarifaDiaria(vehiculo.getTarifaDiaria())
                .ubicacion(vehiculo.getUbicacion())
                .activo(vehiculo.isActivo())
                .build();
    }

    private Vehiculo toEntity(VehiculoRequestDTO dto) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCategoria(dto.getCategoria());
        vehiculo.setCapacidadPasajeros(dto.getCapacidadPasajeros());
        vehiculo.setColor(dto.getColor());
        vehiculo.setVendedorId(dto.getVendedorId());
        vehiculo.setTarifaDiaria(dto.getTarifaDiaria());
        vehiculo.setUbicacion(dto.getUbicacion());
        vehiculo.setActivo(true);
        return vehiculo;
    }
}
