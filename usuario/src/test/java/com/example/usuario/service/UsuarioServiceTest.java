package com.example.usuario.service;

import com.example.usuario.dto.UsuarioRequestDTO;
import com.example.usuario.dto.UsuarioResponseDTO;
import com.example.usuario.exception.ResourceNotFoundException;
import com.example.usuario.model.Usuario;
import com.example.usuario.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("jperez");
        usuario.setPassword("secreto123");
        usuario.setEmail("jperez@mail.com");
        usuario.setRol("CLIENTE");
        usuario.setActivo(true);

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setUsername("jperez");
        requestDTO.setPassword("secreto123");
        requestDTO.setEmail("jperez@mail.com");
        requestDTO.setRol("CLIENTE");
    }

    // ============ LISTAR ============

    @Test
    @DisplayName("listar - deberia retornar lista de usuarios")
    void listar_deberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("jperez", resultado.get(0).getUsername());
        verify(repository).findAll();
    }

    // ============ BUSCAR POR ID ============

    @Test
    @DisplayName("buscarPorId - deberia retornar usuario cuando existe")
    void buscarPorId_deberiaRetornarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = service.buscarPorId(1L);

        assertEquals("jperez", resultado.getUsername());
    }

    @Test
    @DisplayName("buscarPorId - deberia lanzar excepcion cuando no existe")
    void buscarPorId_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    // ============ EXISTE POR ID ============

    @Test
    @DisplayName("existePorId - deberia retornar true si existe")
    void existePorId_deberiaRetornarTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existePorId(1L));
    }

    @Test
    @DisplayName("existePorId - deberia retornar false si no existe")
    void existePorId_deberiaRetornarFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        assertFalse(service.existePorId(99L));
    }

    // ============ CREAR ============

    @Test
    @DisplayName("crear - deberia crear usuario correctamente")
    void crear_deberiaCrearUsuario() {
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@mail.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("jperez", resultado.getUsername());
        assertEquals("CLIENTE", resultado.getRol());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crear - deberia lanzar excepcion si username ya existe")
    void crear_deberiaLanzarExcepcionUsernameDuplicado() {
        when(repository.existsByUsername("jperez")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crear(requestDTO));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crear - deberia lanzar excepcion si email ya existe")
    void crear_deberiaLanzarExcepcionEmailDuplicado() {
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@mail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crear(requestDTO));
        verify(repository, never()).save(any());
    }

    // ============ ACTUALIZAR ============

    @Test
    @DisplayName("actualizar - deberia actualizar usuario correctamente")
    void actualizar_deberiaActualizarUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.actualizar(1L, requestDTO);

        assertNotNull(resultado);
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("actualizar - deberia lanzar excepcion si no existe")
    void actualizar_deberiaLanzarExcepcionNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, requestDTO));
    }

    @Test
    @DisplayName("actualizar - deberia lanzar excepcion si nuevo username ya existe")
    void actualizar_deberiaLanzarExcepcionUsernameConflicto() {
        requestDTO.setUsername("otro_user");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(repository.existsByUsername("otro_user")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.actualizar(1L, requestDTO));
    }

    // ============ ELIMINAR ============

    @Test
    @DisplayName("eliminar - deberia eliminar usuario existente")
    void eliminar_deberiaEliminarUsuario() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar - deberia lanzar excepcion si no existe")
    void eliminar_deberiaLanzarExcepcionNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }

    // ============ BUSCAR POR USERNAME ============

    @Test
    @DisplayName("buscarPorUsername - deberia retornar usuario")
    void buscarPorUsername_deberiaRetornarUsuario() {
        when(repository.findByUsername("jperez")).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = service.buscarPorUsername("jperez");

        assertEquals("jperez", resultado.getUsername());
    }

    @Test
    @DisplayName("buscarPorUsername - deberia lanzar excepcion si no existe")
    void buscarPorUsername_deberiaLanzarExcepcion() {
        when(repository.findByUsername("inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.buscarPorUsername("inexistente"));
    }
}
