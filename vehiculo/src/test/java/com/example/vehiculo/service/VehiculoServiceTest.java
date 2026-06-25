package com.example.vehiculo.service;

import com.example.vehiculo.dto.VehiculoRequestDTO;
import com.example.vehiculo.dto.VehiculoResponseDTO;
import com.example.vehiculo.dto.VendedorResponseDTO;
import com.example.vehiculo.exception.ResourceNotFoundException;
import com.example.vehiculo.model.Vehiculo;
import com.example.vehiculo.repository.VehiculoRepository;
import com.example.vehiculo.webclient.VendedorClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehiculoServiceTest {

    @Mock private VehiculoRepository repository;
    @Mock private VendedorClient vendedorClient;
    @InjectMocks private VehiculoService service;

    private Vehiculo vehiculo;
    private VehiculoRequestDTO dto;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
        vehiculo.setId(1L);
        vehiculo.setPatente("AB1234");
        vehiculo.setMarca("Toyota");
        vehiculo.setModelo("Corolla");
        vehiculo.setAnio(2024);
        vehiculo.setVendedorId(100L);
        vehiculo.setTarifaDiaria(25000.0);

        dto = new VehiculoRequestDTO();
        dto.setPatente("AB1234");
        dto.setMarca("Toyota");
        dto.setModelo("Corolla");
        dto.setAnio(2024);
        dto.setCategoria("Sedan");
        dto.setCapacidadPasajeros("5");
        dto.setColor("Rojo");
        dto.setVendedorId(100L);
        dto.setTarifaDiaria(25000.0);
        dto.setUbicacion("Santiago");
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(vehiculo));
        assertEquals(1, service.listar().size());
    }

    @Test @DisplayName("buscarPorId")
    void buscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculo));
        assertEquals("AB1234", service.buscarPorId(1L).getPatente());
    }

    @Test @DisplayName("guardar - exitoso")
    void guardar_exitoso() {
        when(vendedorClient.obtenerVendedor(100L)).thenReturn(new VendedorResponseDTO());
        when(repository.save(any())).thenReturn(vehiculo);
        assertNotNull(service.guardar(dto));
    }

    @Test @DisplayName("guardar - vendedor no existe")
    void guardar_vendedorNoExiste() {
        when(vendedorClient.obtenerVendedor(100L)).thenThrow(new RuntimeException("No existe"));
        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test @DisplayName("actualizar")
    void actualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculo));
        when(vendedorClient.obtenerVendedor(100L)).thenReturn(new VendedorResponseDTO());
        when(repository.save(any())).thenReturn(vehiculo);
        assertNotNull(service.actualizar(1L, dto));
    }

    @Test @DisplayName("existePorId")
    void existePorId() {
        when(repository.existsById(1L)).thenReturn(true);
        assertTrue(service.existePorId(1L));
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(vehiculo));
        service.eliminar(1L);
        verify(repository).delete(vehiculo);
    }
}
