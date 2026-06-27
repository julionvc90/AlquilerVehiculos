package com.example.multa.service;

import com.example.multa.dto.MultaRequestDTO;
import com.example.multa.dto.ReservaResponseDTO;
import com.example.multa.exception.MultaNotFoundException;
import com.example.multa.model.Multa;
import com.example.multa.repository.MultaRepository;
import com.example.multa.webclient.ReservaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MultaServiceTest {

    @Mock private MultaRepository repository;
    @Mock private ReservaClient reservaClient;
    @InjectMocks private MultaService service;

    private Multa multa;
    private MultaRequestDTO dto;

    @BeforeEach
    void setUp() {
        multa = new Multa();
        multa.setIdMulta(1L);
        multa.setIdReserva(1L);
        multa.setIdVehiculo(1L);
        multa.setMotivoMulta("Devolucion tardia");
        multa.setMontoMulta(new BigDecimal("50000"));
        multa.setEstadoMulta("Pendiente");

        dto = new MultaRequestDTO();
        dto.setIdReserva(1L);
        dto.setIdVehiculo(1L);
        dto.setMotivoMulta("Devolucion tardia");
        dto.setMontoMulta(new BigDecimal("50000"));
        dto.setEstadoMulta("Pendiente");
    }

    @Test @DisplayName("crearMulta - exitoso")
    void crearMulta_exitoso() {
        when(reservaClient.obtenerReserva(1L)).thenReturn(new ReservaResponseDTO());
        when(repository.save(any())).thenReturn(multa);
        assertNotNull(service.crearMulta(dto));
    }

    @Test @DisplayName("crearMulta - reserva no existe")
    void crearMulta_reservaNoExiste() {
        when(reservaClient.obtenerReserva(1L)).thenThrow(new RuntimeException("No existe"));
        assertThrows(RuntimeException.class, () -> service.crearMulta(dto));
    }

    @Test @DisplayName("obtenerPorId")
    void obtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(multa));
        assertEquals("Pendiente", service.obtenerPorId(1L).getEstadoMulta());
    }

    @Test @DisplayName("obtenerPorId - no encontrado")
    void obtenerPorId_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(MultaNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(multa));
        assertEquals(1, service.listarTodas().size());
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(multa));
        service.eliminarMulta(1L);
        verify(repository).delete(multa);
    }
}
