package com.example.inspeccion.service;

import com.example.inspeccion.dto.InspeccionRequestDTO;
import com.example.inspeccion.dto.InspeccionResponseDTO;
import com.example.inspeccion.exception.ResourceNotFoundException;
import com.example.inspeccion.model.Inspeccion;
import com.example.inspeccion.repository.InspeccionRepository;
import com.example.inspeccion.webclient.AlquilerClient;
import com.example.inspeccion.webclient.VehiculoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InspeccionServiceTest {

    @Mock private InspeccionRepository repository;
    @Mock private AlquilerClient alquilerClient;
    @Mock private VehiculoClient vehiculoClient;
    @InjectMocks private InspeccionService service;

    private Inspeccion inspeccion;
    private InspeccionRequestDTO dto;

    @BeforeEach
    void setUp() {
        inspeccion = new Inspeccion();
        inspeccion.setId(1L);
        inspeccion.setAlquilerId(1L);
        inspeccion.setVehiculoId(1L);
        inspeccion.setTipoInspeccion("Devolucion");
        inspeccion.setResultado("Aprobado");

        dto = new InspeccionRequestDTO();
        dto.setAlquilerId(1L);
        dto.setVehiculoId(1L);
        dto.setFechaInspeccion(LocalDateTime.now());
        dto.setTipoInspeccion("Devolucion");
        dto.setResultado("Aprobado");
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(inspeccion));
        assertEquals(1, service.listar().size());
    }

    @Test @DisplayName("buscarPorId")
    void buscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(inspeccion));
        assertEquals("Aprobado", service.buscarPorId(1L).getResultado());
    }

    @Test @DisplayName("crear - exitoso")
    void crear_exitoso() {
        when(alquilerClient.existeAlquiler(1L)).thenReturn(true);
        when(vehiculoClient.existeVehiculo(1L)).thenReturn(true);
        when(repository.save(any())).thenReturn(inspeccion);
        assertNotNull(service.crear(dto));
    }

    @Test @DisplayName("crear - alquiler no existe")
    void crear_alquilerNoExiste() {
        when(alquilerClient.existeAlquiler(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("crear - vehiculo no existe")
    void crear_vehiculoNoExiste() {
        when(alquilerClient.existeAlquiler(1L)).thenReturn(true);
        when(vehiculoClient.existeVehiculo(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("actualizar")
    void actualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(inspeccion));
        when(alquilerClient.existeAlquiler(1L)).thenReturn(true);
        when(vehiculoClient.existeVehiculo(1L)).thenReturn(true);
        when(repository.save(any())).thenReturn(inspeccion);
        assertNotNull(service.actualizar(1L, dto));
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
