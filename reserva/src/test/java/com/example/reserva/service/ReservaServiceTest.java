package com.example.reserva.service;

import com.example.reserva.dto.*;
import com.example.reserva.exception.ReservaNotFoundException;
import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;
import com.example.reserva.webclient.ClienteClient;
import com.example.reserva.webclient.DisponibilidadClient;
import com.example.reserva.webclient.VehiculoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository repository;
    @Mock private ClienteClient clienteClient;
    @Mock private VehiculoClient vehiculoClient;
    @Mock private DisponibilidadClient disponibilidadClient;
    @InjectMocks private ReservaService service;

    private Reserva reserva;
    private ReservaRequestDTO dto;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setIdReserva(1L);
        reserva.setIdCliente(1L);
        reserva.setIdVehiculo(1L);
        reserva.setEstadoReserva("Confirmada");
        reserva.setTotalReserva(new BigDecimal("100000"));
        reserva.setFechaInicio(LocalDate.of(2025, 7, 1));
        reserva.setFechaTermino(LocalDate.of(2025, 7, 5));

        dto = new ReservaRequestDTO();
        dto.setIdCliente(1L);
        dto.setIdVehiculo(1L);
        dto.setFechaReserva(LocalDate.now());
        dto.setFechaInicio(LocalDate.of(2025, 7, 1));
        dto.setFechaTermino(LocalDate.of(2025, 7, 5));
        dto.setTotalDias(4);
        dto.setValorDia(new BigDecimal("25000"));
        dto.setTotalReserva(new BigDecimal("100000"));
        dto.setEstadoReserva("Confirmada");
        dto.setObservacionesReserva("Sin novedades");
    }

    @Test @DisplayName("crearReserva - exitoso")
    void crearReserva_exitoso() {
        when(clienteClient.obtenerCliente(1L)).thenReturn(new ClienteResponseDTO());
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(new VehiculoResponseDTO());
        when(disponibilidadClient.validarDisponibilidad(anyLong(), any(), any())).thenReturn(true);
        when(repository.save(any())).thenReturn(reserva);

        assertNotNull(service.crearReserva(dto));
    }

    @Test @DisplayName("crearReserva - no disponible")
    void crearReserva_noDisponible() {
        when(clienteClient.obtenerCliente(1L)).thenReturn(new ClienteResponseDTO());
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(new VehiculoResponseDTO());
        when(disponibilidadClient.validarDisponibilidad(anyLong(), any(), any())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.crearReserva(dto));
    }

    @Test @DisplayName("crearReserva - fecha termino antes de inicio")
    void crearReserva_fechaInvalida() {
        dto.setFechaTermino(LocalDate.of(2025, 6, 1));
        when(clienteClient.obtenerCliente(1L)).thenReturn(new ClienteResponseDTO());
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(new VehiculoResponseDTO());
        when(disponibilidadClient.validarDisponibilidad(anyLong(), any(), any())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.crearReserva(dto));
    }

    @Test @DisplayName("obtenerPorId")
    void obtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));
        assertEquals("Confirmada", service.obtenerPorId(1L).getEstadoReserva());
    }

    @Test @DisplayName("obtenerPorId - no encontrado")
    void obtenerPorId_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ReservaNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(reserva));
        assertEquals(1, service.listarTodas().size());
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));
        service.eliminarReserva(1L);
        verify(repository).delete(reserva);
    }
}
