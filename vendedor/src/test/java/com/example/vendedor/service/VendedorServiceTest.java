package com.example.vendedor.service;

import com.example.vendedor.dto.VendedorRequestDTO;
import com.example.vendedor.dto.VendedorResponseDTO;
import com.example.vendedor.exception.ResourceNotFoundException;
import com.example.vendedor.model.Vendedor;
import com.example.vendedor.repository.VendedorRepository;
import com.example.vendedor.webclient.UsuarioClient;
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
class VendedorServiceTest {

    @Mock private VendedorRepository repository;
    @Mock private UsuarioClient usuarioClient;
    @InjectMocks private VendedorService service;

    private Vendedor vendedor;
    private VendedorRequestDTO dto;

    @BeforeEach
    void setUp() {
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setRut("10.123.456-7");
        vendedor.setNombre("Maria");
        vendedor.setApellido("Gomez");
        vendedor.setEmail("maria@mail.com");
        vendedor.setUsuarioId(100L);

        dto = new VendedorRequestDTO();
        dto.setRut("10.123.456-7");
        dto.setNombre("Maria");
        dto.setApellido("Gomez");
        dto.setEmail("maria@mail.com");
        dto.setUsuarioId(100L);
    }

    @Test @DisplayName("listar - retorna lista")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(vendedor));
        assertEquals(1, service.listar().size());
    }

    @Test @DisplayName("buscarPorId - encontrado")
    void buscarPorId_encontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(vendedor));
        assertEquals("10.123.456-7", service.buscarPorId(1L).getRut());
    }

    @Test @DisplayName("buscarPorId - no encontrado")
    void buscarPorId_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test @DisplayName("crear - exitoso")
    void crear_exitoso() {
        when(repository.existsByRut("10.123.456-7")).thenReturn(false);
        when(usuarioClient.existeUsuario(100L)).thenReturn(true);
        when(repository.save(any())).thenReturn(vendedor);

        VendedorResponseDTO r = service.crear(dto);
        assertNotNull(r);
        verify(repository).save(any());
    }

    @Test @DisplayName("crear - RUT duplicado")
    void crear_rutDuplicado() {
        when(repository.existsByRut("10.123.456-7")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("crear - usuario no existe")
    void crear_usuarioNoExiste() {
        when(repository.existsByRut("10.123.456-7")).thenReturn(false);
        when(usuarioClient.existeUsuario(100L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("actualizar - exitoso")
    void actualizar_exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(usuarioClient.existeUsuario(100L)).thenReturn(true);
        when(repository.save(any())).thenReturn(vendedor);
        assertNotNull(service.actualizar(1L, dto));
    }

    @Test @DisplayName("actualizar - no encontrado")
    void actualizar_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dto));
    }

    @Test @DisplayName("eliminar - exitoso")
    void eliminar_exitoso() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test @DisplayName("eliminar - no encontrado")
    void eliminar_noEncontrado() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }

    @Test @DisplayName("buscarPorRut - encontrado")
    void buscarPorRut() {
        when(repository.findByRut("10.123.456-7")).thenReturn(Optional.of(vendedor));
        assertEquals("10.123.456-7", service.buscarPorRut("10.123.456-7").getRut());
    }
}
