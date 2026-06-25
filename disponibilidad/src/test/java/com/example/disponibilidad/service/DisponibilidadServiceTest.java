package com.example.disponibilidad.service;

import com.example.disponibilidad.dto.DisponibilidadRequestDTO;
import com.example.disponibilidad.dto.VehiculoResponseDTO;
import com.example.disponibilidad.exception.ResourceNotFoundException;
import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.repository.DisponibilidadRepository;
import com.example.disponibilidad.webclient.VehiculoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisponibilidadServiceTest {

    @Mock private DisponibilidadRepository repository;
    @Mock private VehiculoClient vehiculoClient;
    @InjectMocks private DisponibilidadService service;

    private Disponibilidad disp;
    private DisponibilidadRequestDTO dto;

    @BeforeEach
    void setUp() {
        disp = new Disponibilidad();
        disp.setId(1L);
        disp.setVehiculoId(1L);
        disp.setFechaInicio(LocalDate.of(2025, 7, 1));
        disp.setFechaFin(LocalDate.of(2025, 7, 10));
        disp.setDisponible(true);

        dto = new DisponibilidadRequestDTO();
        dto.setVehiculoId(1L);
        dto.setFechaInicio(LocalDate.of(2025, 7, 1));
        dto.setFechaFin(LocalDate.of(2025, 7, 10));
        dto.setDisponible(true);
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(disp));
        assertEquals(1, service.listar().size());
    }

    @Test @DisplayName("buscarPorId")
    void buscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(disp));
        assertEquals(1L, service.buscarPorId(1L).getVehiculoId());
    }

    @Test @DisplayName("guardar - exitoso")
    void guardar_exitoso() {
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(new VehiculoResponseDTO());
        when(repository.save(any())).thenReturn(disp);
        assertNotNull(service.guardar(dto));
    }

    @Test @DisplayName("actualizar")
    void actualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(disp));
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(new VehiculoResponseDTO());
        when(repository.save(any())).thenReturn(disp);
        assertNotNull(service.actualizar(1L, dto));
    }

    @Test @DisplayName("validarDisponibilidad - disponible")
    void validarDisponibilidad_disponible() {
        when(repository.findByVehiculoIdAndDisponibleFalseAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
                anyLong(), any(), any())).thenReturn(List.of());
        assertTrue(service.validarDisponibilidad(1L, LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    @Test @DisplayName("validarDisponibilidad - no disponible")
    void validarDisponibilidad_noDisponible() {
        when(repository.findByVehiculoIdAndDisponibleFalseAndFechaFinGreaterThanEqualAndFechaInicioLessThanEqual(
                anyLong(), any(), any())).thenReturn(List.of(new Disponibilidad()));
        assertFalse(service.validarDisponibilidad(1L, LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(disp));
        service.eliminar(1L);
        verify(repository).delete(disp);
    }
}
