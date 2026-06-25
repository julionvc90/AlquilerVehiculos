package com.example.cliente.service;

import com.example.cliente.dto.ClienteRequestDTO;
import com.example.cliente.dto.ClienteResponseDTO;
import com.example.cliente.exception.ResourceNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.repository.ClienteRepository;
import com.example.cliente.webclient.UsuarioClient;
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
class ClienteServiceTest {

    @Mock private ClienteRepository repository;
    @Mock private UsuarioClient usuarioClient;
    @InjectMocks private ClienteService service;

    private Cliente cliente;
    private ClienteRequestDTO dto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRut("12.345.678-9");
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setEmail("juan@mail.com");
        cliente.setUsuarioId(100L);

        dto = new ClienteRequestDTO();
        dto.setRut("12.345.678-9");
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setEmail("juan@mail.com");
        dto.setUsuarioId(100L);
    }

    @Test @DisplayName("listar - retorna lista")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(cliente));
        List<ClienteResponseDTO> result = service.listar();
        assertEquals(1, result.size());
    }

    @Test @DisplayName("buscarPorId - encontrado")
    void buscarPorId_encontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        ClienteResponseDTO r = service.buscarPorId(1L);
        assertEquals("12.345.678-9", r.getRut());
    }

    @Test @DisplayName("buscarPorId - no encontrado")
    void buscarPorId_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test @DisplayName("crear - exitoso")
    void crear_exitoso() {
        when(repository.existsByRut("12.345.678-9")).thenReturn(false);
        when(usuarioClient.existeUsuario(100L)).thenReturn(true);
        when(repository.save(any())).thenReturn(cliente);

        ClienteResponseDTO r = service.crear(dto);
        assertNotNull(r);
        verify(repository).save(any());
    }

    @Test @DisplayName("crear - RUT duplicado")
    void crear_rutDuplicado() {
        when(repository.existsByRut("12.345.678-9")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("crear - usuario no existe")
    void crear_usuarioNoExiste() {
        when(repository.existsByRut("12.345.678-9")).thenReturn(false);
        when(usuarioClient.existeUsuario(100L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.crear(dto));
    }

    @Test @DisplayName("actualizar - exitoso")
    void actualizar_exitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        when(usuarioClient.existeUsuario(100L)).thenReturn(true);
        when(repository.save(any())).thenReturn(cliente);

        ClienteResponseDTO r = service.actualizar(1L, dto);
        assertNotNull(r);
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
    void buscarPorRut_encontrado() {
        when(repository.findByRut("12.345.678-9")).thenReturn(Optional.of(cliente));
        ClienteResponseDTO r = service.buscarPorRut("12.345.678-9");
        assertEquals("12.345.678-9", r.getRut());
    }

    @Test @DisplayName("buscarPorRut - no encontrado")
    void buscarPorRut_noEncontrado() {
        when(repository.findByRut("00.000.000-0")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorRut("00.000.000-0"));
    }
}
